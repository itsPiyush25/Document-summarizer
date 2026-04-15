package com.documentsummarizer.service;

import org.springframework.stereotype.Service;

@Service
public class TextParserService {

    /**
     * Processes raw text input.
     * Currently, it only trims whitespace.
     * This method can be extended to handle more complex cleaning,
     * or to parse text from files (PDF, TXT, CSV).
     *
     * @param text The raw text input.
     * @return The processed text.
     */
    public String parseText(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Input text cannot be null or empty.");
        }
        // Basic cleaning: trim leading/trailing whitespace
        // More sophisticated cleaning can be added here (e.g., removing special characters, normalizing spaces)
        return text.trim();
    }

    /**
     * Placeholder for future file parsing logic.
     *
     * @param fileData The byte array of the file.
     * @param fileType The type of the file (e.g., "pdf", "txt", "csv").
     * @return The extracted text from the file.
     */
    public String parseFile(byte[] fileData, String fileType) {
        // TODO: Implement file parsing using Apache Tika or similar library
        // This will involve:
        // 1. Detecting file type if not provided.
        // 2. Using a parser (like Tika's AutoDetectParser) to extract text.
        // 3. Handling potential exceptions during parsing.
        throw new UnsupportedOperationException("File parsing is not yet implemented.");
    }
}