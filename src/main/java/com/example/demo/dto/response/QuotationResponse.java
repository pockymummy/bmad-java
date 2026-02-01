package com.example.demo.dto.response;

import com.example.demo.entity.Quotation;
import com.example.demo.enums.QuotationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class QuotationResponse {

    private Long id;
    private QuotationStatus status;
    private BigDecimal totalAmount;
    private String notes;
    private String reviewNotes;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
    private Long listingId;
    private String listingTitle;
    private Long partsShopId;
    private String partsShopCompanyName;
    private Long examinerId;
    private String examinerName;
    private List<QuotationItemResponse> items;

    public QuotationResponse() {
    }

    public static QuotationResponse fromEntity(Quotation quotation) {
        return fromEntity(quotation, true);
    }

    public static QuotationResponse fromEntity(Quotation quotation, boolean includeItems) {
        QuotationResponse response = new QuotationResponse();
        response.setId(quotation.getId());
        response.setStatus(quotation.getStatus());
        response.setTotalAmount(quotation.getTotalAmount());
        response.setNotes(quotation.getNotes());
        response.setReviewNotes(quotation.getReviewNotes());
        response.setCreatedAt(quotation.getCreatedAt());
        response.setReviewedAt(quotation.getReviewedAt());
        response.setListingId(quotation.getPartsListing().getId());
        response.setListingTitle(quotation.getPartsListing().getTitle());
        response.setPartsShopId(quotation.getPartsShop().getId());
        response.setPartsShopCompanyName(quotation.getPartsShop().getCompanyName());
        if (quotation.getExaminer() != null) {
            response.setExaminerId(quotation.getExaminer().getId());
            response.setExaminerName(quotation.getExaminer().getUsername());
        }
        if (includeItems) {
            response.setItems(quotation.getQuotationItems().stream()
                    .map(QuotationItemResponse::fromEntity)
                    .collect(Collectors.toList()));
        }
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuotationStatus getStatus() {
        return status;
    }

    public void setStatus(QuotationStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
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

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public String getListingTitle() {
        return listingTitle;
    }

    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }

    public Long getPartsShopId() {
        return partsShopId;
    }

    public void setPartsShopId(Long partsShopId) {
        this.partsShopId = partsShopId;
    }

    public String getPartsShopCompanyName() {
        return partsShopCompanyName;
    }

    public void setPartsShopCompanyName(String partsShopCompanyName) {
        this.partsShopCompanyName = partsShopCompanyName;
    }

    public Long getExaminerId() {
        return examinerId;
    }

    public void setExaminerId(Long examinerId) {
        this.examinerId = examinerId;
    }

    public String getExaminerName() {
        return examinerName;
    }

    public void setExaminerName(String examinerName) {
        this.examinerName = examinerName;
    }

    public List<QuotationItemResponse> getItems() {
        return items;
    }

    public void setItems(List<QuotationItemResponse> items) {
        this.items = items;
    }
}
