package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;

public class QuotationDecisionRequest {

    @NotNull(message = "Approved status is required")
    private Boolean approved;

    private String reviewNotes;

    public QuotationDecisionRequest() {
    }

    public QuotationDecisionRequest(Boolean approved, String reviewNotes) {
        this.approved = approved;
        this.reviewNotes = reviewNotes;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }
}
