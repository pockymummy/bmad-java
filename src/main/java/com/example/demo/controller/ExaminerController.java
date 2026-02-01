package com.example.demo.controller;

import com.example.demo.dto.request.QuotationDecisionRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.QuotationResponse;
import com.example.demo.service.ExaminerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/examiner")
@Tag(name = "Examiner", description = "Endpoints for examiner users")
public class ExaminerController {

    private final ExaminerService examinerService;

    public ExaminerController(ExaminerService examinerService) {
        this.examinerService = examinerService;
    }

    @GetMapping("/{examinerId}/quotations/pending")
    @Operation(summary = "Get pending quotations",
               description = "Retrieves all quotations pending review")
    public ResponseEntity<ApiResponse<List<QuotationResponse>>> getPendingQuotations(
            @PathVariable Long examinerId) {
        List<QuotationResponse> quotations = examinerService.getPendingQuotations(examinerId);
        return ResponseEntity.ok(ApiResponse.success(quotations));
    }

    @GetMapping("/{examinerId}/quotations/{quotationId}")
    @Operation(summary = "Get quotation details",
               description = "Retrieves details of a specific quotation for review")
    public ResponseEntity<ApiResponse<QuotationResponse>> getQuotationDetails(
            @PathVariable Long examinerId,
            @PathVariable Long quotationId) {
        QuotationResponse quotation = examinerService.getQuotationDetails(examinerId, quotationId);
        return ResponseEntity.ok(ApiResponse.success(quotation));
    }

    @PatchMapping("/{examinerId}/quotations/{quotationId}/review")
    @Operation(summary = "Review quotation",
               description = "Approve or disapprove a pending quotation")
    public ResponseEntity<ApiResponse<QuotationResponse>> reviewQuotation(
            @PathVariable Long examinerId,
            @PathVariable Long quotationId,
            @Valid @RequestBody QuotationDecisionRequest decision) {
        QuotationResponse response = examinerService.reviewQuotation(examinerId, quotationId, decision);
        String message = decision.getApproved() ? "Quotation approved" : "Quotation disapproved";
        return ResponseEntity.ok(ApiResponse.success(message, response));
    }

    @GetMapping("/{examinerId}/quotations/reviewed")
    @Operation(summary = "Get reviewed quotations",
               description = "Retrieves history of quotations reviewed by this examiner")
    public ResponseEntity<ApiResponse<List<QuotationResponse>>> getReviewedQuotations(
            @PathVariable Long examinerId) {
        List<QuotationResponse> quotations = examinerService.getReviewedQuotations(examinerId);
        return ResponseEntity.ok(ApiResponse.success(quotations));
    }
}
