package com.documentsummarizer.controller;

import com.documentsummarizer.dto.SummaryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SummaryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void summarize_ValidShortRequest_ReturnsSuccess() throws Exception {
        // Given
        SummaryRequest request = new SummaryRequest();
        request.setText("Artificial intelligence is transforming many industries by automating tasks and providing insights from data. Machine learning algorithms can analyze patterns and make predictions.");
        request.setSummaryType(SummaryRequest.SummaryType.SHORT);
        request.setMaxLength(150);

        // When & Then
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.summaryType").value("SHORT"))
                .andExpect(jsonPath("$.summary").isNotEmpty())
                .andExpect(jsonPath("$.summary").isString());
    }

    @Test
    void summarize_ValidBulletRequest_ReturnsBulletSummary() throws Exception {
        // Given
        SummaryRequest request = new SummaryRequest();
        request.setText("Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications. Key features include: embedded servers, auto-configuration, production-ready features, and no code generation.");
        request.setSummaryType(SummaryRequest.SummaryType.BULLET);
        request.setMaxLength(300);

        // When & Then
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.summaryType").value("BULLET"))
                .andExpect(jsonPath("$.summary").isNotEmpty());
        // Note: For bullet points, summary can be a string or array, so we just check it's not empty
    }

    @Test
    void summarize_ValidDetailedRequest_ReturnsDetailedSummary() throws Exception {
        // Given
        SummaryRequest request = new SummaryRequest();
        request.setText("Climate change refers to long-term shifts in temperatures and weather patterns. These shifts may be natural, but since the 1800s, human activities have been the main driver of climate change, primarily due to the burning of fossil fuels like coal, oil and gas. Burning fossil fuels generates greenhouse gas emissions that act like a blanket wrapped around the Earth, trapping the sun's heat and raising temperatures.");
        request.setSummaryType(SummaryRequest.SummaryType.DETAILED);
        request.setMaxLength(500);

        // When & Then
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.summaryType").value("DETAILED"))
                .andExpect(jsonPath("$.summary").isNotEmpty())
                .andExpect(jsonPath("$.summary").isString());
    }

    @Test
    void summarize_EmptyText_ReturnsValidationError() throws Exception {
        // Given
        SummaryRequest request = new SummaryRequest();
        request.setText("");
        request.setSummaryType(SummaryRequest.SummaryType.SHORT);
        request.setMaxLength(100);

        // When & Then
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400)) // HTTP status code as integer
                .andExpect(jsonPath("$.message").isString()) // Error message
                .andExpect(jsonPath("$.timestamp").isNumber()); // Timestamp
    }

    @Test
    void summarize_TextTooShort_ReturnsValidationError() throws Exception {
        // Given - Text with only 2 characters (minimum is 10 based on @Size(min = 10))
        SummaryRequest request = new SummaryRequest();
        request.setText("Hi");
        request.setSummaryType(SummaryRequest.SummaryType.SHORT);
        request.setMaxLength(100);

        // When & Then
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400)); // HTTP status code as integer
    }

    @Test
    void summarize_MissingSummaryType_ReturnsValidationError() throws Exception {
        // Given - Create JSON without summaryType
        String requestJson = """
            {
                "text": "This is a sample text for testing validation.",
                "maxLength": 100
            }
            """;

        // When & Then
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400)); // HTTP status code as integer
    }

    @Test
    void summarize_MaxLengthExceeded_StillProcesses() throws Exception {
        // Given - Very long text that exceeds typical limits
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longText.append("This is sentence number ").append(i).append(". ");
        }
        
        SummaryRequest request = new SummaryRequest();
        request.setText(longText.toString());
        request.setSummaryType(SummaryRequest.SummaryType.SHORT);
        request.setMaxLength(50); // Very small max length

        // When & Then - Should still process but summary may be truncated by mock AI
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void summarize_WithSpecialCharacters_ProcessesCorrectly() throws Exception {
        // Given - Text with special characters
        SummaryRequest request = new SummaryRequest();
        request.setText("Text with special chars: @#$%^&*()! Test email: test@example.com. URL: https://example.com/path?query=value");
        request.setSummaryType(SummaryRequest.SummaryType.SHORT);
        request.setMaxLength(200);

        // When & Then
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void summarize_MultilineText_ProcessesCorrectly() throws Exception {
        // Given - Text with multiple lines
        String multilineText = """
            First paragraph.
            
            Second paragraph with more details.
            
            Third paragraph concludes the document.
            """;
        
        SummaryRequest request = new SummaryRequest();
        request.setText(multilineText);
        request.setSummaryType(SummaryRequest.SummaryType.BULLET);
        request.setMaxLength(300);

        // When & Then
        mockMvc.perform(post("/api/v1/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.summaryType").value("BULLET"));
    }
}