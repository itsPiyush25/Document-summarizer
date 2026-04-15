package com.documentsummarizer.client;

import com.documentsummarizer.dto.SummaryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MockAiClientTest {

    @Autowired
    private MockAiClient mockAiClient;

    @Test
    public void getShortSummary_ShouldReturnShortSummaryString() {
        String text = "This is a long text that needs to be summarized. It contains multiple sentences to ensure proper processing.";
        String summary = mockAiClient.getSummary(text, SummaryRequest.SummaryType.SHORT);

        assertNotNull(summary);
        assertFalse(summary.isEmpty());
        // Check that it's a reasonable length for a short summary
        assertTrue(summary.length() > 10, "Summary should be longer than 10 characters");
        // Check that it contains some common summary indicators or ends with punctuation
        assertTrue(summary.endsWith(".") || summary.toLowerCase().contains("summary") || 
                   summary.toLowerCase().contains("text") || summary.toLowerCase().contains("about"),
                   "Summary should be a complete sentence or contain summary-related words");
    }

    @Test
    public void getBulletSummary_ShouldReturnBulletPointString() {
        String text = "This is a long text that needs to be summarized. It contains multiple sentences to ensure proper processing.";
        String summary = mockAiClient.getSummary(text, SummaryRequest.SummaryType.BULLET);

        assertNotNull(summary);
        assertFalse(summary.isEmpty());
        // Check that it contains bullet points or newlines (bullet summaries are multi-line)
        assertTrue(summary.contains("\n") || summary.contains("•") || summary.contains("-") || 
                   summary.contains("First") || summary.contains("Second") || summary.contains("Third"),
                   "Bullet summary should contain list indicators or ordinal indicators");
        // Check reasonable length
        assertTrue(summary.length() > 20, "Bullet summary should be longer than 20 characters");
    }

    @Test
    public void getDetailedSummary_ShouldReturnDetailedSummaryString() {
        String text = "This is a long text that needs to be summarized. It contains multiple sentences to ensure proper processing.";
        String summary = mockAiClient.getSummary(text, SummaryRequest.SummaryType.DETAILED);

        assertNotNull(summary);
        assertFalse(summary.isEmpty());
        // Detailed summaries should be longer than short ones
        assertTrue(summary.length() > 30, "Detailed summary should be longer than 30 characters");
        // Check for comprehensive language or ends with punctuation
        assertTrue(summary.endsWith(".") || summary.toLowerCase().contains("analysis") || 
                   summary.toLowerCase().contains("detailed") || summary.toLowerCase().contains("comprehensive") ||
                   summary.toLowerCase().contains("explain") || summary.toLowerCase().contains("discuss"),
                   "Detailed summary should be comprehensive and end with proper punctuation");
    }
}