package com.documentsummarizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SummaryRequest {

    @NotBlank(message = "Text cannot be blank.")
    @Size(min = 10, message = "Text must be at least 10 characters long.")
    private String text;

    @NotNull(message = "Summary type cannot be null.")
    private SummaryType summaryType;

    private Integer maxLength; // Optional max length for the summary

    public enum SummaryType {
        SHORT, BULLET, DETAILED
    }
}