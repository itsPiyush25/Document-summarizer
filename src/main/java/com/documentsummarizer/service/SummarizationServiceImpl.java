package com.documentsummarizer.service;

import com.documentsummarizer.client.MockAiClient;
import com.documentsummarizer.dto.SummaryRequest;
import com.documentsummarizer.dto.SummaryResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class SummarizationServiceImpl implements SummarizationService {

    private final MockAiClient mockAiClient;
    private final TextParserService textParserService;

    // Constructor for dependency injection
    public SummarizationServiceImpl(MockAiClient mockAiClient, TextParserService textParserService) {
        this.mockAiClient = mockAiClient;
        this.textParserService = textParserService;
    }

    @Override
    public SummaryResponse generateSummary(SummaryRequest request) {
        // Pre-process the input text
        String processedText = textParserService.parseText(request.getText());

        String rawSummary = mockAiClient.getSummary(processedText, request.getSummaryType());

        Object formattedSummary;
        if (SummaryRequest.SummaryType.BULLET.equals(request.getSummaryType())) {
            // Split the bullet points string into a list of strings
            formattedSummary = Arrays.asList(rawSummary.split("\n"));
        } else {
            // For short and detailed, the summary is a single string
            formattedSummary = rawSummary;
        }

        return new SummaryResponse("success", request.getSummaryType().name(), formattedSummary);
    }
}