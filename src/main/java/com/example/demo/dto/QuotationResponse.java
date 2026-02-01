package com.example.demo.dto;

import com.example.demo.entity.Quotation;
import com.example.demo.entity.QuotationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class QuotationResponse {

    private Long id;
    private Long listingId;
    private String partsShopName;
    private BigDecimal price;
    private String availability;
    private Integer deliveryTimeDays;
    private String notes;
    private QuotationStatus status;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;

    public static QuotationResponse fromEntity(Quotation quotation) {
        QuotationResponse response = new QuotationResponse();
        response.setId(quotation.getId());
        response.setListingId(quotation.getListing().getId());
        response.setPartsShopName(quotation.getPartsShopName());
        response.setPrice(quotation.getPrice());
        response.setAvailability(quotation.getAvailability());
        response.setDeliveryTimeDays(quotation.getDeliveryTimeDays());
        response.setNotes(quotation.getNotes());
        response.setStatus(quotation.getStatus());
        response.setRejectionReason(quotation.getRejectionReason());
        response.setCreatedAt(quotation.getCreatedAt());
        response.setReviewedAt(quotation.getReviewedAt());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public String getPartsShopName() {
        return partsShopName;
    }

    public void setPartsShopName(String partsShopName) {
        this.partsShopName = partsShopName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Integer getDeliveryTimeDays() {
        return deliveryTimeDays;
    }

    public void setDeliveryTimeDays(Integer deliveryTimeDays) {
        this.deliveryTimeDays = deliveryTimeDays;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public QuotationStatus getStatus() {
        return status;
    }

    public void setStatus(QuotationStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
