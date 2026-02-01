package com.example.demo.controller;

import com.example.demo.dto.request.CreatePartsListingRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.ListingWithQuotationsResponse;
import com.example.demo.dto.response.PartsListingResponse;
import com.example.demo.service.PartsListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/insurer")
@Tag(name = "Insurer", description = "Endpoints for insurance company users")
public class InsurerController {

    private final PartsListingService partsListingService;

    public InsurerController(PartsListingService partsListingService) {
        this.partsListingService = partsListingService;
    }

    @PostMapping("/{insurerId}/listings")
    @Operation(summary = "Create a new parts listing",
               description = "Creates a new parts listing with the required parts for a claim")
    public ResponseEntity<ApiResponse<PartsListingResponse>> createListing(
            @PathVariable Long insurerId,
            @Valid @RequestBody CreatePartsListingRequest request) {
        PartsListingResponse response = partsListingService.createListing(insurerId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Parts listing created successfully", response));
    }

    @GetMapping("/{insurerId}/listings")
    @Operation(summary = "Get all listings with quotations",
               description = "Retrieves all parts listings created by the insurer with their quotations")
    public ResponseEntity<ApiResponse<List<ListingWithQuotationsResponse>>> getListingsWithQuotations(
            @PathVariable Long insurerId) {
        List<ListingWithQuotationsResponse> listings = partsListingService.getListingsWithQuotations(insurerId);
        return ResponseEntity.ok(ApiResponse.success(listings));
    }

    @GetMapping("/{insurerId}/listings/{listingId}")
    @Operation(summary = "Get specific listing with quotations",
               description = "Retrieves a specific parts listing with all its quotations")
    public ResponseEntity<ApiResponse<ListingWithQuotationsResponse>> getListingById(
            @PathVariable Long insurerId,
            @PathVariable Long listingId) {
        ListingWithQuotationsResponse listing = partsListingService.getListingWithQuotations(insurerId, listingId);
        return ResponseEntity.ok(ApiResponse.success(listing));
    }
}
