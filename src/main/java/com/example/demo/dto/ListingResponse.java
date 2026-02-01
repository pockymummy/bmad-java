package com.example.demo.dto;

import com.example.demo.entity.Listing;
import com.example.demo.entity.ListingStatus;

import java.time.LocalDateTime;

public class ListingResponse {

    private Long id;
    private String partName;
    private String description;
    private Integer quantityNeeded;
    private String vehicleReference;
    private String claimReference;
    private LocalDateTime createdAt;
    private ListingStatus status;

    public static ListingResponse fromEntity(Listing listing) {
        ListingResponse response = new ListingResponse();
        response.setId(listing.getId());
        response.setPartName(listing.getPartName());
        response.setDescription(listing.getDescription());
        response.setQuantityNeeded(listing.getQuantityNeeded());
        response.setVehicleReference(listing.getVehicleReference());
        response.setClaimReference(listing.getClaimReference());
        response.setCreatedAt(listing.getCreatedAt());
        response.setStatus(listing.getStatus());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantityNeeded() {
        return quantityNeeded;
    }

    public void setQuantityNeeded(Integer quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }

    public String getVehicleReference() {
        return vehicleReference;
    }

    public void setVehicleReference(String vehicleReference) {
        this.vehicleReference = vehicleReference;
    }

    public String getClaimReference() {
        return claimReference;
    }

    public void setClaimReference(String claimReference) {
        this.claimReference = claimReference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ListingStatus getStatus() {
        return status;
    }

    public void setStatus(ListingStatus status) {
        this.status = status;
    }
}
