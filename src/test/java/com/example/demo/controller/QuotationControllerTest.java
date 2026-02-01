package com.example.demo.controller;

import com.example.demo.BaseIntegrationTest;
import com.example.demo.dto.CreateListingRequest;
import com.example.demo.dto.CreateQuotationRequest;
import com.example.demo.dto.ReviewQuotationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
class QuotationControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long listingId;

    @BeforeEach
    void setUp() throws Exception {
        // Create a listing to use in tests
        CreateListingRequest listingRequest = new CreateListingRequest();
        listingRequest.setPartName("Brake Pad");
        listingRequest.setDescription("Front brake pad");
        listingRequest.setQuantityNeeded(4);

        String response = mockMvc.perform(post("/api/v1/listings")
                        .header("X-Role", "insurer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listingRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        listingId = objectMapper.readTree(response).get("id").asLong();
    }

    private CreateQuotationRequest createValidQuotationRequest() {
        CreateQuotationRequest request = new CreateQuotationRequest();
        request.setPartsShopName("AutoParts Inc.");
        request.setPrice(new BigDecimal("150.00"));
        request.setAvailability("In Stock");
        request.setDeliveryTimeDays(3);
        request.setNotes("Premium quality parts");
        return request;
    }

    @Test
    void createQuotation_asPartsshop_returns201() throws Exception {
        CreateQuotationRequest request = createValidQuotationRequest();

        mockMvc.perform(post("/api/v1/listings/" + listingId + "/quotations")
                        .header("X-Role", "partsshop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.partsShopName").value("AutoParts Inc."))
                .andExpect(jsonPath("$.price").value(150.00))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createQuotation_asInsurer_returns403() throws Exception {
        CreateQuotationRequest request = createValidQuotationRequest();

        mockMvc.perform(post("/api/v1/listings/" + listingId + "/quotations")
                        .header("X-Role", "insurer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createQuotation_forNonExistentListing_returns404() throws Exception {
        CreateQuotationRequest request = createValidQuotationRequest();

        mockMvc.perform(post("/api/v1/listings/99999/quotations")
                        .header("X-Role", "partsshop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createQuotation_withNegativePrice_returns400() throws Exception {
        CreateQuotationRequest request = createValidQuotationRequest();
        request.setPrice(new BigDecimal("-50.00"));

        mockMvc.perform(post("/api/v1/listings/" + listingId + "/quotations")
                        .header("X-Role", "partsshop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getQuotations_asExaminer_returns200() throws Exception {
        // Create a quotation first
        CreateQuotationRequest request = createValidQuotationRequest();
        mockMvc.perform(post("/api/v1/listings/" + listingId + "/quotations")
                        .header("X-Role", "partsshop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/listings/" + listingId + "/quotations")
                        .header("X-Role", "examiner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].partsShopName").value("AutoParts Inc."));
    }

    @Test
    void getQuotations_asInsurer_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/listings/" + listingId + "/quotations")
                        .header("X-Role", "insurer"))
                .andExpect(status().isOk());
    }

    @Test
    void getQuotations_asPartsshop_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/listings/" + listingId + "/quotations")
                        .header("X-Role", "partsshop"))
                .andExpect(status().isForbidden());
    }

    @Test
    void approveQuotation_asExaminer_returns200() throws Exception {
        // Create a quotation
        CreateQuotationRequest quotationRequest = createValidQuotationRequest();
        String response = mockMvc.perform(post("/api/v1/listings/" + listingId + "/quotations")
                        .header("X-Role", "partsshop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quotationRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long quotationId = objectMapper.readTree(response).get("id").asLong();

        // Approve the quotation
        ReviewQuotationRequest reviewRequest = new ReviewQuotationRequest();
        reviewRequest.setApproved(true);

        mockMvc.perform(post("/api/v1/quotations/" + quotationId + "/review")
                        .header("X-Role", "examiner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void disapproveQuotation_withReason_asExaminer_returns200() throws Exception {
        // Create a quotation
        CreateQuotationRequest quotationRequest = createValidQuotationRequest();
        String response = mockMvc.perform(post("/api/v1/listings/" + listingId + "/quotations")
                        .header("X-Role", "partsshop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quotationRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long quotationId = objectMapper.readTree(response).get("id").asLong();

        // Disapprove with reason
        ReviewQuotationRequest reviewRequest = new ReviewQuotationRequest();
        reviewRequest.setApproved(false);
        reviewRequest.setReason("Price too high");

        mockMvc.perform(post("/api/v1/quotations/" + quotationId + "/review")
                        .header("X-Role", "examiner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DISAPPROVED"))
                .andExpect(jsonPath("$.rejectionReason").value("Price too high"));
    }

    @Test
    void disapproveQuotation_withoutReason_returns400() throws Exception {
        // Create a quotation
        CreateQuotationRequest quotationRequest = createValidQuotationRequest();
        String response = mockMvc.perform(post("/api/v1/listings/" + listingId + "/quotations")
                        .header("X-Role", "partsshop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quotationRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long quotationId = objectMapper.readTree(response).get("id").asLong();

        // Try to disapprove without reason
        ReviewQuotationRequest reviewRequest = new ReviewQuotationRequest();
        reviewRequest.setApproved(false);
        // No reason provided

        mockMvc.perform(post("/api/v1/quotations/" + quotationId + "/review")
                        .header("X-Role", "examiner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reviewQuotation_asInsurer_returns403() throws Exception {
        // Create a quotation
        CreateQuotationRequest quotationRequest = createValidQuotationRequest();
        String response = mockMvc.perform(post("/api/v1/listings/" + listingId + "/quotations")
                        .header("X-Role", "partsshop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quotationRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long quotationId = objectMapper.readTree(response).get("id").asLong();

        ReviewQuotationRequest reviewRequest = new ReviewQuotationRequest();
        reviewRequest.setApproved(true);

        mockMvc.perform(post("/api/v1/quotations/" + quotationId + "/review")
                        .header("X-Role", "insurer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void reviewQuotation_asPartsshop_returns403() throws Exception {
        // Create a quotation
        CreateQuotationRequest quotationRequest = createValidQuotationRequest();
        String response = mockMvc.perform(post("/api/v1/listings/" + listingId + "/quotations")
                        .header("X-Role", "partsshop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quotationRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long quotationId = objectMapper.readTree(response).get("id").asLong();

        ReviewQuotationRequest reviewRequest = new ReviewQuotationRequest();
        reviewRequest.setApproved(true);

        mockMvc.perform(post("/api/v1/quotations/" + quotationId + "/review")
                        .header("X-Role", "partsshop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isForbidden());
    }
}
