package com.example.demo.controller;

import com.example.demo.dto.request.CreateQuotationRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PartsListingResponse;
import com.example.demo.dto.response.QuotationResponse;
import com.example.demo.service.PartsListingService;
import com.example.demo.service.QuotationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parts-shop")
@Tag(name = "Parts Shop", description = "Endpoints for parts shop users")
public class PartsShopController {

    private final PartsListingService partsListingService;
    private final QuotationService quotationService;

    public PartsShopController(PartsListingService partsListingService,
                               QuotationService quotationService) {
        this.partsListingService = partsListingService;
        this.quotationService = quotationService;
    }

    @GetMapping("/listings")
    @Operation(summary = "Get all active listings",
               description = "Retrieves all active parts listings available for quotation")
    public ResponseEntity<ApiResponse<List<PartsListingResponse>>> getAllActiveListings() {
        List<PartsListingResponse> listings = partsListingService.getAllActiveListings();
        return ResponseEntity.ok(ApiResponse.success(listings));
    }

    @GetMapping("/{partsShopId}/listings/available")
    @Operation(summary = "Get available listings for parts shop",
               description = "Retrieves active listings that the parts shop hasn't quoted yet")
    public ResponseEntity<ApiResponse<List<PartsListingResponse>>> getAvailableListings(
            @PathVariable Long partsShopId) {
        List<PartsListingResponse> listings = partsListingService.getAvailableListingsForPartsShop(partsShopId);
        return ResponseEntity.ok(ApiResponse.success(listings));
    }

    @GetMapping("/listings/{listingId}")
    @Operation(summary = "Get listing details",
               description = "Retrieves details of a specific parts listing")
    public ResponseEntity<ApiResponse<PartsListingResponse>> getListingById(
            @PathVariable Long listingId) {
        PartsListingResponse listing = partsListingService.getListingById(listingId);
        return ResponseEntity.ok(ApiResponse.success(listing));
    }

    @PostMapping("/{partsShopId}/quotations")
    @Operation(summary = "Create a quotation",
               description = "Creates a new quotation for a parts listing with pricing")
    public ResponseEntity<ApiResponse<QuotationResponse>> createQuotation(
            @PathVariable Long partsShopId,
            @Valid @RequestBody CreateQuotationRequest request) {
        QuotationResponse response = quotationService.createQuotation(partsShopId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Quotation created successfully", response));
    }

    @GetMapping("/{partsShopId}/quotations")
    @Operation(summary = "Get shop's quotations",
               description = "Retrieves all quotations submitted by the parts shop")
    public ResponseEntity<ApiResponse<List<QuotationResponse>>> getShopQuotations(
            @PathVariable Long partsShopId) {
        List<QuotationResponse> quotations = quotationService.getQuotationsByPartsShop(partsShopId);
        return ResponseEntity.ok(ApiResponse.success(quotations));
    }
}
