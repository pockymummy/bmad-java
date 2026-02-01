package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CreateQuotationRequest {

    @NotBlank(message = "Parts shop name is required")
    @Size(max = 255, message = "Parts shop name must not exceed 255 characters")
    private String partsShopName;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @Size(max = 100, message = "Availability must not exceed 100 characters")
    private String availability;

    @PositiveOrZero(message = "Delivery time must be zero or positive")
    private Integer deliveryTimeDays;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

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
}
