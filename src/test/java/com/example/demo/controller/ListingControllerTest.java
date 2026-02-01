package com.example.demo.controller;

import com.example.demo.BaseIntegrationTest;
import com.example.demo.dto.CreateListingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
class ListingControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateListingRequest createValidRequest() {
        CreateListingRequest request = new CreateListingRequest();
        request.setPartName("Brake Pad");
        request.setDescription("Front brake pad for Toyota Camry 2020");
        request.setQuantityNeeded(4);
        request.setVehicleReference("Toyota Camry 2020");
        request.setClaimReference("CLM-2024-001");
        return request;
    }

    @Test
    void createListing_asInsurer_returns201() throws Exception {
        CreateListingRequest request = createValidRequest();

        mockMvc.perform(post("/api/v1/listings")
                        .header("X-Role", "insurer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.partName").value("Brake Pad"))
                .andExpect(jsonPath("$.quantityNeeded").value(4))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void createListing_asPartsshop_returns403() throws Exception {
        CreateListingRequest request = createValidRequest();

        mockMvc.perform(post("/api/v1/listings")
                        .header("X-Role", "partsshop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createListing_asExaminer_returns403() throws Exception {
        CreateListingRequest request = createValidRequest();

        mockMvc.perform(post("/api/v1/listings")
                        .header("X-Role", "examiner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllListings_asAnyValidRole_returns200() throws Exception {
        // First create a listing
        CreateListingRequest request = createValidRequest();
        mockMvc.perform(post("/api/v1/listings")
                        .header("X-Role", "insurer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Test with each role
        for (String role : new String[]{"insurer", "partsshop", "examiner"}) {
            mockMvc.perform(get("/api/v1/listings")
                            .header("X-Role", role))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }
    }

    @Test
    void getListingById_existingId_returns200() throws Exception {
        // Create a listing first
        CreateListingRequest request = createValidRequest();
        String response = mockMvc.perform(post("/api/v1/listings")
                        .header("X-Role", "insurer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/v1/listings/" + id)
                        .header("X-Role", "insurer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.partName").value("Brake Pad"));
    }

    @Test
    void getListingById_nonExistentId_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/listings/99999")
                        .header("X-Role", "insurer"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createListing_withInvalidData_returns400() throws Exception {
        CreateListingRequest request = new CreateListingRequest();
        // Missing required fields

        mockMvc.perform(post("/api/v1/listings")
                        .header("X-Role", "insurer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
