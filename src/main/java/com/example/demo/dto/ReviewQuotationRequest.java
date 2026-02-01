package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public class ReviewQuotationRequest {

    @NotNull(message = "Approved field is required")
    private Boolean approved;

    private String reason;

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
