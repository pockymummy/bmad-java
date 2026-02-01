package com.example.demo.dto.response;

import com.example.demo.entity.PartsListing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PartsListingResponse {

    private Long id;
    private String title;
    private String claimNumber;
    private String vehicleMake;
    private String vehicleModel;
    private Integer vehicleYear;
    private String vinNumber;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;
    private Long insurerId;
    private String insurerCompanyName;
    private List<PartItemResponse> partItems;

    public PartsListingResponse() {
    }

    public static PartsListingResponse fromEntity(PartsListing listing) {
        PartsListingResponse response = new PartsListingResponse();
        response.setId(listing.getId());
        response.setTitle(listing.getTitle());
        response.setClaimNumber(listing.getClaimNumber());
        response.setVehicleMake(listing.getVehicleMake());
        response.setVehicleModel(listing.getVehicleModel());
        response.setVehicleYear(listing.getVehicleYear());
        response.setVinNumber(listing.getVinNumber());
        response.setDescription(listing.getDescription());
        response.setActive(listing.isActive());
        response.setCreatedAt(listing.getCreatedAt());
        response.setInsurerId(listing.getInsurer().getId());
        response.setInsurerCompanyName(listing.getInsurer().getCompanyName());
        response.setPartItems(listing.getPartItems().stream()
                .map(PartItemResponse::fromEntity)
                .collect(Collectors.toList()));
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(Long insurerId) {
        this.insurerId = insurerId;
    }

    public String getInsurerCompanyName() {
        return insurerCompanyName;
    }

    public void setInsurerCompanyName(String insurerCompanyName) {
        this.insurerCompanyName = insurerCompanyName;
    }

    public List<PartItemResponse> getPartItems() {
        return partItems;
    }

    public void setPartItems(List<PartItemResponse> partItems) {
        this.partItems = partItems;
    }
}
