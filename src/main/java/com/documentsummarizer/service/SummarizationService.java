package com.documentsummarizer.service;

import com.documentsummarizer.dto.SummaryRequest;
import com.documentsummarizer.dto.SummaryResponse;

public interface SummarizationService {
    SummaryResponse generateSummary(SummaryRequest request);
}