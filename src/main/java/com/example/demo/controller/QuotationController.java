package com.example.demo.controller;

import com.example.demo.config.Role;
import com.example.demo.config.RoleContext;
import com.example.demo.dto.CreateQuotationRequest;
import com.example.demo.dto.QuotationResponse;
import com.example.demo.dto.ReviewQuotationRequest;
import com.example.demo.service.QuotationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Quotations", description = "Parts quotation management")
public class QuotationController {

    private final QuotationService quotationService;

    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @PostMapping("/listings/{listingId}/quotations")
    @Operation(summary = "Create a quotation for a listing", description = "Only PARTSSHOP role can create quotations")
    public ResponseEntity<QuotationResponse> createQuotation(
            @PathVariable Long listingId,
            @Valid @RequestBody CreateQuotationRequest request) {

        Role role = RoleContext.getRole();
        if (role != Role.PARTSSHOP) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only parts shops can create quotations");
        }

        QuotationResponse response = quotationService.createQuotation(listingId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/listings/{listingId}/quotations")
    @Operation(summary = "Get all quotations for a listing", description = "Only EXAMINER and INSURER roles can view quotations")
    public ResponseEntity<List<QuotationResponse>> getQuotationsForListing(
            @PathVariable Long listingId) {

        Role role = RoleContext.getRole();
        if (role != Role.EXAMINER && role != Role.INSURER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only examiners and insurers can view quotations");
        }

        List<QuotationResponse> quotations = quotationService.getQuotationsForListing(listingId);
        return ResponseEntity.ok(quotations);
    }

    @GetMapping("/quotations/{id}")
    @Operation(summary = "Get a specific quotation", description = "Only EXAMINER and INSURER roles can view quotations")
    public ResponseEntity<QuotationResponse> getQuotationById(@PathVariable Long id) {
        Role role = RoleContext.getRole();
        if (role != Role.EXAMINER && role != Role.INSURER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only examiners and insurers can view quotations");
        }

        QuotationResponse quotation = quotationService.getQuotationById(id);
        return ResponseEntity.ok(quotation);
    }

    @PostMapping("/quotations/{id}/review")
    @Operation(summary = "Review (approve/disapprove) a quotation", description = "Only EXAMINER role can review quotations")
    public ResponseEntity<QuotationResponse> reviewQuotation(
            @PathVariable Long id,
            @Valid @RequestBody ReviewQuotationRequest request) {

        Role role = RoleContext.getRole();
        if (role != Role.EXAMINER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only examiners can review quotations");
        }

        QuotationResponse response = quotationService.reviewQuotation(id, request);
        return ResponseEntity.ok(response);
    }
}
