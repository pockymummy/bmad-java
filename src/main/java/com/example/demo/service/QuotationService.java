package com.example.demo.service;

import com.example.demo.dto.request.CreateQuotationRequest;
import com.example.demo.dto.request.QuotationItemRequest;
import com.example.demo.dto.response.QuotationResponse;
import com.example.demo.entity.PartsListing;
import com.example.demo.entity.Quotation;
import com.example.demo.entity.QuotationItem;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.QuotationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final UserService userService;
    private final PartsListingService partsListingService;

    public QuotationService(QuotationRepository quotationRepository,
                           UserService userService,
                           PartsListingService partsListingService) {
        this.quotationRepository = quotationRepository;
        this.userService = userService;
        this.partsListingService = partsListingService;
    }

    public QuotationResponse createQuotation(Long partsShopId, CreateQuotationRequest request) {
        User partsShop = userService.getPartsShop(partsShopId);
        PartsListing listing = partsListingService.getListingEntity(request.getListingId());

        if (!listing.isActive()) {
            throw new BusinessRuleException("Cannot create quotation for inactive listing");
        }

        if (quotationRepository.existsByPartsListingIdAndPartsShopId(listing.getId(), partsShopId)) {
            throw new BusinessRuleException("You have already submitted a quotation for this listing");
        }

        Quotation quotation = new Quotation();
        quotation.setPartsListing(listing);
        quotation.setPartsShop(partsShop);
        quotation.setNotes(request.getNotes());

        for (QuotationItemRequest itemRequest : request.getItems()) {
            QuotationItem item = new QuotationItem();
            item.setPartName(itemRequest.getPartName());
            item.setPartNumber(itemRequest.getPartNumber());
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(itemRequest.getUnitPrice());
            item.setBrand(itemRequest.getBrand());
            item.setWarrantyMonths(itemRequest.getWarrantyMonths());
            item.setCondition(itemRequest.getCondition());
            item.setNotes(itemRequest.getNotes());
            quotation.addQuotationItem(item);
        }

        quotation.calculateTotalAmount();
        Quotation savedQuotation = quotationRepository.save(quotation);
        return QuotationResponse.fromEntity(savedQuotation);
    }

    @Transactional(readOnly = true)
    public List<QuotationResponse> getQuotationsByPartsShop(Long partsShopId) {
        userService.getPartsShop(partsShopId);
        return quotationRepository.findByPartsShopId(partsShopId).stream()
                .map(QuotationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuotationResponse getQuotationById(Long quotationId) {
        Quotation quotation = quotationRepository.findByIdWithDetails(quotationId)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation", quotationId));
        return QuotationResponse.fromEntity(quotation);
    }

    @Transactional(readOnly = true)
    public Quotation getQuotationEntity(Long quotationId) {
        return quotationRepository.findByIdWithDetails(quotationId)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation", quotationId));
    }
}
