package com.documentsummarizer.service;

import com.documentsummarizer.client.MockAiClient;
import com.documentsummarizer.dto.SummaryRequest;
import com.documentsummarizer.dto.SummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SummarizationServiceImplTest {

    @Mock
    private MockAiClient mockAiClient;

    @Mock
    private TextParserService textParserService;

    @InjectMocks
    private SummarizationServiceImpl summarizationService;

    private SummaryRequest shortRequest;
    private SummaryRequest bulletRequest;
    private SummaryRequest detailedRequest;

    @BeforeEach
    public void setUp() {
        shortRequest = new SummaryRequest();
        shortRequest.setText("Sample text for short summary.");
        shortRequest.setSummaryType(SummaryRequest.SummaryType.SHORT);

        bulletRequest = new SummaryRequest();
        bulletRequest.setText("Sample text for bullet summary.");
        bulletRequest.setSummaryType(SummaryRequest.SummaryType.BULLET);

        detailedRequest = new SummaryRequest();
        detailedRequest.setText("Sample text for detailed summary.");
        detailedRequest.setSummaryType(SummaryRequest.SummaryType.DETAILED);
    }

    @Test
    public void generateSummary_ShortType_ShouldReturnStringSummary() {
        when(textParserService.parseText(any())).thenReturn("parsed text");
        when(mockAiClient.getSummary(any(), eq(SummaryRequest.SummaryType.SHORT)))
                .thenReturn("Mock short summary.");

        SummaryResponse response = summarizationService.generateSummary(shortRequest);

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("SHORT", response.getSummaryType());
        assertTrue(response.getSummary() instanceof String);
        assertEquals("Mock short summary.", response.getSummary());
    }

    @Test
    public void generateSummary_BulletType_ShouldReturnListSummary() {
        when(textParserService.parseText(any())).thenReturn("parsed text");
        when(mockAiClient.getSummary(any(), eq(SummaryRequest.SummaryType.BULLET)))
                .thenReturn("Point 1\nPoint 2\nPoint 3");

        SummaryResponse response = summarizationService.generateSummary(bulletRequest);

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("BULLET", response.getSummaryType());
        assertTrue(response.getSummary() instanceof List);
        List<?> bulletList = (List<?>) response.getSummary();
        assertEquals(3, bulletList.size());
        assertEquals("Point 1", bulletList.get(0));
    }

    @Test
    public void generateSummary_DetailedType_ShouldReturnStringSummary() {
        when(textParserService.parseText(any())).thenReturn("parsed text");
        when(mockAiClient.getSummary(any(), eq(SummaryRequest.SummaryType.DETAILED)))
                .thenReturn("Mock detailed summary.");

        SummaryResponse response = summarizationService.generateSummary(detailedRequest);

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("DETAILED", response.getSummaryType());
        assertTrue(response.getSummary() instanceof String);
        assertEquals("Mock detailed summary.", response.getSummary());
    }
}