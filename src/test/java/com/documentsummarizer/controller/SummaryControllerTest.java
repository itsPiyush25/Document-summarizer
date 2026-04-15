package com.documentsummarizer.controller;

import com.documentsummarizer.dto.SummaryRequest;
import com.documentsummarizer.dto.SummaryResponse;
import com.documentsummarizer.exception.GlobalExceptionHandler;
import com.documentsummarizer.service.SummarizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SummaryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SummarizationService summarizationService;

    @InjectMocks
    private SummaryController summaryController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Set up MockMvc with standalone setup including GlobalExceptionHandler
        mockMvc = MockMvcBuilders.standaloneSetup(summaryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void summarize_ValidRequest_ReturnsOk() throws Exception {
        // Given
        SummaryRequest request = new SummaryRequest();
        request.setText("This is a sample text for summarization.");
        request.setSummaryType(SummaryRequest.SummaryType.SHORT);
        request.setMaxLength(100);

        SummaryResponse mockResponse = new SummaryResponse();
        mockResponse.setStatus("success");
        mockResponse.setSummaryType("SHORT"); // Changed from enum to string
        mockResponse.setSummary("This is a short summary of the text.");

        when(summarizationService.generateSummary(any(SummaryRequest.class)))
                .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.summaryType").value("SHORT"))
                .andExpect(jsonPath("$.summary").value("This is a short summary of the text."));
    }

    @Test
    void summarize_BulletType_ReturnsBulletSummary() throws Exception {
        // Given
        SummaryRequest request = new SummaryRequest();
        request.setText("Sample text with multiple points.");
        request.setSummaryType(SummaryRequest.SummaryType.BULLET);
        request.setMaxLength(200);

        SummaryResponse mockResponse = new SummaryResponse();
        mockResponse.setStatus("success");
        mockResponse.setSummaryType("BULLET"); // Changed from enum to string
        mockResponse.setSummary("• Point 1\n• Point 2\n• Point 3");

        when(summarizationService.generateSummary(any(SummaryRequest.class)))
                .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.summaryType").value("BULLET"))
                .andExpect(jsonPath("$.summary").value("• Point 1\n• Point 2\n• Point 3"));
    }

    @Test
    void summarize_DetailedType_ReturnsDetailedSummary() throws Exception {
        // Given
        SummaryRequest request = new SummaryRequest();
        request.setText("A longer text that requires detailed analysis.");
        request.setSummaryType(SummaryRequest.SummaryType.DETAILED);
        request.setMaxLength(500);

        SummaryResponse mockResponse = new SummaryResponse();
        mockResponse.setStatus("success");
        mockResponse.setSummaryType("DETAILED"); // Changed from enum to string
        mockResponse.setSummary("This is a detailed summary with comprehensive analysis.");

        when(summarizationService.generateSummary(any(SummaryRequest.class)))
                .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.summaryType").value("DETAILED"))
                .andExpect(jsonPath("$.summary").value("This is a detailed summary with comprehensive analysis."));
    }

    @Test
    void summarize_EmptyText_ReturnsValidationError() throws Exception {
        // Given
        SummaryRequest request = new SummaryRequest();
        request.setText(""); // Empty text should trigger validation
        request.setSummaryType(SummaryRequest.SummaryType.SHORT);
        request.setMaxLength(100);

        // When & Then - Validation should fail before reaching service
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void summarize_MissingText_ReturnsValidationError() throws Exception {
        // Given - Create JSON without text field
        String requestJson = """
            {
                "summaryType": "SHORT",
                "maxLength": 100
            }
            """;

        // When & Then - Validation should fail
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void summarize_NullSummaryType_ReturnsValidationError() throws Exception {
        // Given
        SummaryRequest request = new SummaryRequest();
        request.setText("Sample text");
        request.setSummaryType(null); // Null should trigger validation
        request.setMaxLength(100);

        // When & Then
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void summarize_InvalidSummaryType_ReturnsValidationError() throws Exception {
        // Given - Invalid enum value
        String requestJson = """
            {
                "text": "Sample text",
                "summaryType": "INVALID_TYPE",
                "maxLength": 100
            }
            """;

        // When & Then - Jackson should fail to deserialize
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void summarize_ServiceThrowsException_ReturnsInternalServerError() throws Exception {
        // Given
        SummaryRequest request = new SummaryRequest();
        request.setText("Sample text");
        request.setSummaryType(SummaryRequest.SummaryType.SHORT);
        request.setMaxLength(100);

        when(summarizationService.generateSummary(any(SummaryRequest.class)))
                .thenThrow(new RuntimeException("Service error"));

        // When & Then - Should return 500 with error response
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").isString());
    }
}