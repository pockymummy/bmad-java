package com.example.demo.service;

import com.example.demo.dto.CreateQuotationRequest;
import com.example.demo.dto.QuotationResponse;
import com.example.demo.dto.ReviewQuotationRequest;
import com.example.demo.entity.Listing;
import com.example.demo.entity.ListingStatus;
import com.example.demo.entity.Quotation;
import com.example.demo.entity.QuotationStatus;
import com.example.demo.repository.QuotationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final ListingService listingService;

    public QuotationService(QuotationRepository quotationRepository, ListingService listingService) {
        this.quotationRepository = quotationRepository;
        this.listingService = listingService;
    }

    public QuotationResponse createQuotation(Long listingId, CreateQuotationRequest request) {
        Listing listing = listingService.getListingEntityById(listingId);

        if (listing.getStatus() != ListingStatus.OPEN) {
            throw new IllegalStateException("Cannot submit quotation to a closed listing");
        }

        Quotation quotation = new Quotation();
        quotation.setListing(listing);
        quotation.setPartsShopName(request.getPartsShopName());
        quotation.setPrice(request.getPrice());
        quotation.setAvailability(request.getAvailability());
        quotation.setDeliveryTimeDays(request.getDeliveryTimeDays());
        quotation.setNotes(request.getNotes());

        Quotation saved = quotationRepository.save(quotation);
        return QuotationResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<QuotationResponse> getQuotationsForListing(Long listingId) {
        // Verify listing exists
        listingService.getListingEntityById(listingId);

        return quotationRepository.findByListingId(listingId)
                .stream()
                .map(QuotationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuotationResponse getQuotationById(Long id) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quotation not found with id: " + id));
        return QuotationResponse.fromEntity(quotation);
    }

    public QuotationResponse reviewQuotation(Long quotationId, ReviewQuotationRequest request) {
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new EntityNotFoundException("Quotation not found with id: " + quotationId));

        if (quotation.getStatus() != QuotationStatus.PENDING) {
            throw new IllegalStateException("Quotation has already been reviewed");
        }

        if (Boolean.TRUE.equals(request.getApproved())) {
            quotation.setStatus(QuotationStatus.APPROVED);
            quotation.setRejectionReason(null);
        } else {
            if (request.getReason() == null || request.getReason().isBlank()) {
                throw new IllegalArgumentException("Reason is required when disapproving a quotation");
            }
            quotation.setStatus(QuotationStatus.DISAPPROVED);
            quotation.setRejectionReason(request.getReason());
        }

        quotation.setReviewedAt(LocalDateTime.now());
        Quotation saved = quotationRepository.save(quotation);
        return QuotationResponse.fromEntity(saved);
    }
}
