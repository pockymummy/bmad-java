package com.example.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PartItemRequest {

    @NotBlank(message = "Part name is required")
    private String partName;

    private String partNumber;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity = 1;

    private String condition;

    private String notes;

    public PartItemRequest() {
    }

    public PartItemRequest(String partName, String partNumber, Integer quantity, String condition) {
        this.partName = partName;
        this.partNumber = partNumber;
        this.quantity = quantity;
        this.condition = condition;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
