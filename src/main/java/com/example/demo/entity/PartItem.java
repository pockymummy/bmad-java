package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "part_items")
public class PartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_name", nullable = false)
    private String partName;

    @Column(name = "part_number")
    private String partNumber;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(name = "required_condition")
    private String condition;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parts_listing_id", nullable = false)
    private PartsListing partsListing;

    public PartItem() {
    }

    public PartItem(String partName, String partNumber, Integer quantity, String condition) {
        this.partName = partName;
        this.partNumber = partNumber;
        this.quantity = quantity;
        this.condition = condition;
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

    public PartsListing getPartsListing() {
        return partsListing;
    }

    public void setPartsListing(PartsListing partsListing) {
        this.partsListing = partsListing;
    }
}
