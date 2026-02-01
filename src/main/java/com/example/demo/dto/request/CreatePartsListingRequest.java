package com.example.demo.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreatePartsListingRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Claim number is required")
    private String claimNumber;

    private String vehicleMake;

    private String vehicleModel;

    private Integer vehicleYear;

    private String vinNumber;

    private String description;

    @NotEmpty(message = "At least one part item is required")
    @Valid
    private List<PartItemRequest> partItems;

    public CreatePartsListingRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public Integer getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(Integer vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PartItemRequest> getPartItems() {
        return partItems;
    }

    public void setPartItems(List<PartItemRequest> partItems) {
        this.partItems = partItems;
    }
}
