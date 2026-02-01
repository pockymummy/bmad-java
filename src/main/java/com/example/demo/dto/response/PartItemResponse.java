package com.example.demo.dto.response;

import com.example.demo.entity.PartItem;

public class PartItemResponse {

    private Long id;
    private String partName;
    private String partNumber;
    private Integer quantity;
    private String condition;
    private String notes;

    public PartItemResponse() {
    }

    public static PartItemResponse fromEntity(PartItem partItem) {
        PartItemResponse response = new PartItemResponse();
        response.setId(partItem.getId());
        response.setPartName(partItem.getPartName());
        response.setPartNumber(partItem.getPartNumber());
        response.setQuantity(partItem.getQuantity());
        response.setCondition(partItem.getCondition());
        response.setNotes(partItem.getNotes());
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
