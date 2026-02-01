package com.example.demo.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateQuotationRequest {

    @NotNull(message = "Listing ID is required")
    private Long listingId;

    private String notes;

    @NotEmpty(message = "At least one quotation item is required")
    @Valid
    private List<QuotationItemRequest> items;

    public CreateQuotationRequest() {
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<QuotationItemRequest> getItems() {
        return items;
    }

    public void setItems(List<QuotationItemRequest> items) {
        this.items = items;
    }
}
