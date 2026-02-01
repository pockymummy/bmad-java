package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateListingRequest {

    @NotBlank(message = "Part name is required")
    @Size(max = 255, message = "Part name must not exceed 255 characters")
    private String partName;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantityNeeded;

    @Size(max = 100, message = "Vehicle reference must not exceed 100 characters")
    private String vehicleReference;

    @Size(max = 100, message = "Claim reference must not exceed 100 characters")
    private String claimReference;

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
}
