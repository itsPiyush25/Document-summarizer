package com.documentsummarizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryResponse {

    private String status;
    private String summaryType;
    private Object summary; // Can be a String for short/detailed or a List<String> for bullet points
}