package com.example.demo.config;

import com.example.demo.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoleInterceptorTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void missingRoleHeader_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/listings"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void emptyRoleHeader_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/listings")
                        .header("X-Role", ""))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void invalidRoleHeader_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/listings")
                        .header("X-Role", "admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    void validRoleHeader_insurer_passesThrough() throws Exception {
        mockMvc.perform(get("/api/v1/listings")
                        .header("X-Role", "insurer"))
                .andExpect(status().isOk());
    }

    @Test
    void validRoleHeader_partsshop_passesThrough() throws Exception {
        mockMvc.perform(get("/api/v1/listings")
                        .header("X-Role", "partsshop"))
                .andExpect(status().isOk());
    }

    @Test
    void validRoleHeader_examiner_passesThrough() throws Exception {
        mockMvc.perform(get("/api/v1/listings")
                        .header("X-Role", "examiner"))
                .andExpect(status().isOk());
    }

    @Test
    void validRoleHeader_caseInsensitive() throws Exception {
        mockMvc.perform(get("/api/v1/listings")
                        .header("X-Role", "INSURER"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/listings")
                        .header("X-Role", "Partsshop"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/listings")
                        .header("X-Role", "EXAMINER"))
                .andExpect(status().isOk());
    }
}
