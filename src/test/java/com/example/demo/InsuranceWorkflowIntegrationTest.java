package com.example.demo;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.*;
import com.example.demo.entity.User;
import com.example.demo.enums.QuotationStatus;
import com.example.demo.enums.UserRole;
import com.example.demo.repository.PartsListingRepository;
import com.example.demo.repository.QuotationRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class InsuranceWorkflowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartsListingRepository partsListingRepository;

    @Autowired
    private QuotationRepository quotationRepository;

    private User insurer;
    private User partsShop;
    private User examiner;

    @BeforeEach
    void setUp() {
        quotationRepository.deleteAll();
        partsListingRepository.deleteAll();
        userRepository.deleteAll();

        insurer = userRepository.save(new User("insurer1", UserRole.INSURER, "ABC Insurance Co."));
        partsShop = userRepository.save(new User("partsshop1", UserRole.PARTS_SHOP, "Best Auto Parts"));
        examiner = userRepository.save(new User("examiner1", UserRole.EXAMINER, "Quality Examiners"));
    }

    @Test
    void testCompleteInsuranceWorkflow() throws Exception {
        // Step 1: Insurer creates a parts listing
        CreatePartsListingRequest listingRequest = new CreatePartsListingRequest();
        listingRequest.setTitle("Front Bumper Replacement - Claim #12345");
        listingRequest.setClaimNumber("CLM-2024-12345");
        listingRequest.setVehicleMake("Toyota");
        listingRequest.setVehicleModel("Camry");
        listingRequest.setVehicleYear(2022);
        listingRequest.setVinNumber("1HGBH41JXMN109186");
        listingRequest.setDescription("Front-end collision, bumper and headlight replacement needed");

        PartItemRequest part1 = new PartItemRequest("Front Bumper", "TOY-FB-2022", 1, "New");
        PartItemRequest part2 = new PartItemRequest("Left Headlight Assembly", "TOY-HL-L-2022", 1, "New or Refurbished");
        listingRequest.setPartItems(List.of(part1, part2));

        MvcResult createListingResult = mockMvc.perform(post("/api/v1/insurer/{insurerId}/listings", insurer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Front Bumper Replacement - Claim #12345"))
                .andExpect(jsonPath("$.data.partItems").isArray())
                .andReturn();

        ApiResponse<PartsListingResponse> listingResponse = objectMapper.readValue(
                createListingResult.getResponse().getContentAsString(),
                new TypeReference<ApiResponse<PartsListingResponse>>() {});
        Long listingId = listingResponse.getData().getId();
        assertNotNull(listingId);

        // Step 2: Parts shop views available listings
        mockMvc.perform(get("/api/v1/parts-shop/listings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(listingId))
                .andExpect(jsonPath("$.data[0].title").value("Front Bumper Replacement - Claim #12345"));

        // Step 3: Parts shop creates a quotation
        CreateQuotationRequest quotationRequest = new CreateQuotationRequest();
        quotationRequest.setListingId(listingId);
        quotationRequest.setNotes("All parts available. Can deliver within 3 business days.");

        QuotationItemRequest quotedPart1 = new QuotationItemRequest();
        quotedPart1.setPartName("Front Bumper");
        quotedPart1.setPartNumber("TOY-FB-2022");
        quotedPart1.setQuantity(1);
        quotedPart1.setUnitPrice(new BigDecimal("450.00"));
        quotedPart1.setBrand("Toyota OEM");
        quotedPart1.setWarrantyMonths(24);
        quotedPart1.setCondition("New");

        QuotationItemRequest quotedPart2 = new QuotationItemRequest();
        quotedPart2.setPartName("Left Headlight Assembly");
        quotedPart2.setPartNumber("TOY-HL-L-2022");
        quotedPart2.setQuantity(1);
        quotedPart2.setUnitPrice(new BigDecimal("320.00"));
        quotedPart2.setBrand("Aftermarket Premium");
        quotedPart2.setWarrantyMonths(12);
        quotedPart2.setCondition("New");

        quotationRequest.setItems(List.of(quotedPart1, quotedPart2));

        MvcResult createQuotationResult = mockMvc.perform(post("/api/v1/parts-shop/{partsShopId}/quotations", partsShop.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quotationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.totalAmount").value(770.00))
                .andReturn();

        ApiResponse<QuotationResponse> quotationResponse = objectMapper.readValue(
                createQuotationResult.getResponse().getContentAsString(),
                new TypeReference<ApiResponse<QuotationResponse>>() {});
        Long quotationId = quotationResponse.getData().getId();
        assertNotNull(quotationId);
        assertEquals(QuotationStatus.PENDING, quotationResponse.getData().getStatus());

        // Step 4: Examiner views pending quotations
        mockMvc.perform(get("/api/v1/examiner/{examinerId}/quotations/pending", examiner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(quotationId))
                .andExpect(jsonPath("$.data[0].status").value("PENDING"));

        // Step 5: Examiner approves the quotation
        QuotationDecisionRequest decision = new QuotationDecisionRequest();
        decision.setApproved(true);
        decision.setReviewNotes("Pricing is competitive. Approved for processing.");

        mockMvc.perform(patch("/api/v1/examiner/{examinerId}/quotations/{quotationId}/review",
                        examiner.getId(), quotationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(decision)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("APPROVED"))
                .andExpect(jsonPath("$.data.reviewNotes").value("Pricing is competitive. Approved for processing."));

        // Step 6: Insurer views their listings with approved quotation status
        MvcResult insurerListingsResult = mockMvc.perform(get("/api/v1/insurer/{insurerId}/listings", insurer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].quotations[0].status").value("APPROVED"))
                .andReturn();

        ApiResponse<List<ListingWithQuotationsResponse>> insurerListings = objectMapper.readValue(
                insurerListingsResult.getResponse().getContentAsString(),
                new TypeReference<ApiResponse<List<ListingWithQuotationsResponse>>>() {});

        assertEquals(1, insurerListings.getData().size());
        assertEquals(1, insurerListings.getData().get(0).getQuotations().size());
        assertEquals(QuotationStatus.APPROVED, insurerListings.getData().get(0).getQuotations().get(0).getStatus());
    }

    @Test
    void testPartsShopCannotQuoteSameListingTwice() throws Exception {
        // Create a listing
        CreatePartsListingRequest listingRequest = new CreatePartsListingRequest();
        listingRequest.setTitle("Test Listing");
        listingRequest.setClaimNumber("CLM-TEST-001");
        listingRequest.setPartItems(List.of(new PartItemRequest("Test Part", "TP-001", 1, "New")));

        MvcResult result = mockMvc.perform(post("/api/v1/insurer/{insurerId}/listings", insurer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listingRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        ApiResponse<PartsListingResponse> listingResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<ApiResponse<PartsListingResponse>>() {});
        Long listingId = listingResponse.getData().getId();

        // Create first quotation - should succeed
        CreateQuotationRequest quotationRequest = new CreateQuotationRequest();
        quotationRequest.setListingId(listingId);
        QuotationItemRequest item = new QuotationItemRequest();
        item.setPartName("Test Part");
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("100.00"));
        quotationRequest.setItems(List.of(item));

        mockMvc.perform(post("/api/v1/parts-shop/{partsShopId}/quotations", partsShop.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quotationRequest)))
                .andExpect(status().isCreated());

        // Try to create second quotation - should fail
        mockMvc.perform(post("/api/v1/parts-shop/{partsShopId}/quotations", partsShop.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quotationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("You have already submitted a quotation for this listing"));
    }

    @Test
    void testExaminerCannotReviewNonPendingQuotation() throws Exception {
        // Create listing and quotation
        CreatePartsListingRequest listingRequest = new CreatePartsListingRequest();
        listingRequest.setTitle("Test Listing");
        listingRequest.setClaimNumber("CLM-TEST-002");
        listingRequest.setPartItems(List.of(new PartItemRequest("Test Part", "TP-002", 1, "New")));

        MvcResult listingResult = mockMvc.perform(post("/api/v1/insurer/{insurerId}/listings", insurer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listingRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        ApiResponse<PartsListingResponse> listingResponse = objectMapper.readValue(
                listingResult.getResponse().getContentAsString(),
                new TypeReference<ApiResponse<PartsListingResponse>>() {});

        CreateQuotationRequest quotationRequest = new CreateQuotationRequest();
        quotationRequest.setListingId(listingResponse.getData().getId());
        QuotationItemRequest item = new QuotationItemRequest();
        item.setPartName("Test Part");
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("100.00"));
        quotationRequest.setItems(List.of(item));

        MvcResult quotationResult = mockMvc.perform(post("/api/v1/parts-shop/{partsShopId}/quotations", partsShop.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quotationRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        ApiResponse<QuotationResponse> quotationResponse = objectMapper.readValue(
                quotationResult.getResponse().getContentAsString(),
                new TypeReference<ApiResponse<QuotationResponse>>() {});
        Long quotationId = quotationResponse.getData().getId();

        // First review - should succeed
        QuotationDecisionRequest decision = new QuotationDecisionRequest(true, "Approved");
        mockMvc.perform(patch("/api/v1/examiner/{examinerId}/quotations/{quotationId}/review",
                        examiner.getId(), quotationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(decision)))
                .andExpect(status().isOk());

        // Second review - should fail because it's already approved
        mockMvc.perform(patch("/api/v1/examiner/{examinerId}/quotations/{quotationId}/review",
                        examiner.getId(), quotationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(decision)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Can only review quotations with PENDING status")));
    }

    @Test
    void testRoleValidation() throws Exception {
        // Try to create listing with non-insurer user
        CreatePartsListingRequest listingRequest = new CreatePartsListingRequest();
        listingRequest.setTitle("Test Listing");
        listingRequest.setClaimNumber("CLM-TEST-003");
        listingRequest.setPartItems(List.of(new PartItemRequest("Test Part", "TP-003", 1, "New")));

        mockMvc.perform(post("/api/v1/insurer/{insurerId}/listings", partsShop.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listingRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("not a INSURER")));
    }

    @Test
    void testValidationErrors() throws Exception {
        // Try to create listing without required fields
        CreatePartsListingRequest invalidRequest = new CreatePartsListingRequest();

        mockMvc.perform(post("/api/v1/insurer/{insurerId}/listings", insurer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data.title").value("Title is required"))
                .andExpect(jsonPath("$.data.claimNumber").value("Claim number is required"))
                .andExpect(jsonPath("$.data.partItems").value("At least one part item is required"));
    }
}
