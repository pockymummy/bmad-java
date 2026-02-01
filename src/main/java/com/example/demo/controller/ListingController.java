package com.example.demo.controller;

import com.example.demo.config.Role;
import com.example.demo.config.RoleContext;
import com.example.demo.dto.CreateListingRequest;
import com.example.demo.dto.ListingResponse;
import com.example.demo.service.ListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/listings")
@Tag(name = "Listings", description = "Insurance parts listing management")
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @PostMapping
    @Operation(summary = "Create a new listing", description = "Only INSURER role can create listings")
    public ResponseEntity<ListingResponse> createListing(
            @Valid @RequestBody CreateListingRequest request) {

        Role role = RoleContext.getRole();
        if (role != Role.INSURER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only insurers can create listings");
        }

        ListingResponse response = listingService.createListing(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all listings", description = "All roles can view listings")
    public ResponseEntity<List<ListingResponse>> getAllListings() {
        List<ListingResponse> listings = listingService.getAllListings();
        return ResponseEntity.ok(listings);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get listing by ID", description = "All roles can view a specific listing")
    public ResponseEntity<ListingResponse> getListingById(@PathVariable Long id) {
        ListingResponse listing = listingService.getListingById(id);
        return ResponseEntity.ok(listing);
    }
}
