package com.example.demo.service;

import com.example.demo.dto.CreateListingRequest;
import com.example.demo.dto.ListingResponse;
import com.example.demo.entity.Listing;
import com.example.demo.repository.ListingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ListingService {

    private final ListingRepository listingRepository;

    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public ListingResponse createListing(CreateListingRequest request) {
        Listing listing = new Listing();
        listing.setPartName(request.getPartName());
        listing.setDescription(request.getDescription());
        listing.setQuantityNeeded(request.getQuantityNeeded());
        listing.setVehicleReference(request.getVehicleReference());
        listing.setClaimReference(request.getClaimReference());

        Listing saved = listingRepository.save(listing);
        return ListingResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<ListingResponse> getAllListings() {
        return listingRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ListingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ListingResponse getListingById(Long id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Listing not found with id: " + id));
        return ListingResponse.fromEntity(listing);
    }

    @Transactional(readOnly = true)
    public Listing getListingEntityById(Long id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Listing not found with id: " + id));
    }
}
