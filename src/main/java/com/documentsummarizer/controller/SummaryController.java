package com.documentsummarizer.controller;

import com.documentsummarizer.dto.SummaryRequest;
import com.documentsummarizer.dto.SummaryResponse;
import com.documentsummarizer.service.SummarizationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SummaryController {

    // Placeholder for service injection
    private final SummarizationService summarizationService;

    // Constructor for dependency injection
    public SummaryController(SummarizationService summarizationService) {
        this.summarizationService = summarizationService;
    }

    @PostMapping("/summarize")
    public ResponseEntity<SummaryResponse> summarize(@Valid @RequestBody SummaryRequest request) {
        // Placeholder: Call the service layer to get the summary
        SummaryResponse response = summarizationService.generateSummary(request);
        return ResponseEntity.ok(response);
    }
}