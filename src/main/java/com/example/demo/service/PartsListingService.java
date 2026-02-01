package com.example.demo.service;

import com.example.demo.dto.request.CreatePartsListingRequest;
import com.example.demo.dto.request.PartItemRequest;
import com.example.demo.dto.response.ListingWithQuotationsResponse;
import com.example.demo.dto.response.PartsListingResponse;
import com.example.demo.entity.PartItem;
import com.example.demo.entity.PartsListing;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PartsListingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PartsListingService {

    private final PartsListingRepository partsListingRepository;
    private final UserService userService;

    public PartsListingService(PartsListingRepository partsListingRepository, UserService userService) {
        this.partsListingRepository = partsListingRepository;
        this.userService = userService;
    }

    public PartsListingResponse createListing(Long insurerId, CreatePartsListingRequest request) {
        User insurer = userService.getInsurer(insurerId);

        PartsListing listing = new PartsListing();
        listing.setTitle(request.getTitle());
        listing.setClaimNumber(request.getClaimNumber());
        listing.setVehicleMake(request.getVehicleMake());
        listing.setVehicleModel(request.getVehicleModel());
        listing.setVehicleYear(request.getVehicleYear());
        listing.setVinNumber(request.getVinNumber());
        listing.setDescription(request.getDescription());
        listing.setInsurer(insurer);

        for (PartItemRequest itemRequest : request.getPartItems()) {
            PartItem partItem = new PartItem();
            partItem.setPartName(itemRequest.getPartName());
            partItem.setPartNumber(itemRequest.getPartNumber());
            partItem.setQuantity(itemRequest.getQuantity());
            partItem.setCondition(itemRequest.getCondition());
            partItem.setNotes(itemRequest.getNotes());
            listing.addPartItem(partItem);
        }

        PartsListing savedListing = partsListingRepository.save(listing);
        return PartsListingResponse.fromEntity(savedListing);
    }

    @Transactional(readOnly = true)
    public List<ListingWithQuotationsResponse> getListingsWithQuotations(Long insurerId) {
        userService.getInsurer(insurerId);
        List<PartsListing> listings = partsListingRepository.findByInsurerIdWithQuotations(insurerId);
        return listings.stream()
                .map(ListingWithQuotationsResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ListingWithQuotationsResponse getListingWithQuotations(Long insurerId, Long listingId) {
        userService.getInsurer(insurerId);
        PartsListing listing = partsListingRepository.findByIdWithPartItems(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("PartsListing", listingId));

        if (!listing.getInsurer().getId().equals(insurerId)) {
            throw new ResourceNotFoundException("PartsListing", listingId);
        }

        return ListingWithQuotationsResponse.fromEntity(listing);
    }

    @Transactional(readOnly = true)
    public List<PartsListingResponse> getAllActiveListings() {
        return partsListingRepository.findByActiveTrue().stream()
                .map(PartsListingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PartsListingResponse> getAvailableListingsForPartsShop(Long partsShopId) {
        userService.getPartsShop(partsShopId);
        return partsListingRepository.findAvailableListingsForPartsShop(partsShopId).stream()
                .map(PartsListingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PartsListingResponse getListingById(Long listingId) {
        PartsListing listing = partsListingRepository.findByIdWithPartItems(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("PartsListing", listingId));
        return PartsListingResponse.fromEntity(listing);
    }

    @Transactional(readOnly = true)
    public PartsListing getListingEntity(Long listingId) {
        return partsListingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("PartsListing", listingId));
    }
}
