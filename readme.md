# AI Feature: Document Summarizer

## 1. Overview

Build an AI-powered Document Summarizer that accepts large textual input (e.g., PDF, CSV text, plain text) and returns a concise summary. The feature should support multiple summary types such as short summary, bullet points, and detailed explanations.

---

## 2. Objective

* Reduce large content into meaningful summaries
* Improve readability and quick understanding
* Provide configurable summarization formats

---

## 3. Functional Requirements

### Input

* File upload (PDF, TXT, CSV) OR raw text input
* Optional parameters:

  * `summary_type` (short | detailed | bullet)
  * `max_length` (optional constraint)

### Output

* JSON response:

```json
{
  "status": "success",
  "summary_type": "bullet",
  "summary": [
    "Point 1",
    "Point 2",
    "Point 3"
  ]
}
```

---

## 4. Non-Functional Requirements

* Response time < 3 seconds (for moderate input)
* Scalable for large documents
* Handle invalid or empty input gracefully
* Secure file handling

---

## 5. High-Level Architecture

```
Client → Controller → Service Layer → AI Provider → Response Formatter → Client
```

### Components

* **Controller**: Handles API request
* **Service Layer**: Business logic and preprocessing
* **AI Provider**: Calls LLM API
* **Parser**: Extracts text from files
* **Formatter**: Structures output JSON

---

## 6. API Design

### Endpoint

```
POST /api/v1/summarize
```

### Request (Multipart/Form-Data or JSON)

```json
{
  "text": "Large input text...",
  "summary_type": "short"
}
```

### Response

```json
{
  "status": "success",
  "summary": "Concise summary of the document"
}
```

---

## 7. Backend Design (Spring Boot)

### Controller

```java
@PostMapping("/summarize")
public ResponseEntity<SummaryResponse> summarize(@RequestBody SummaryRequest request) {
    return ResponseEntity.ok(summaryService.generateSummary(request));
}
```

### Service

```java
public SummaryResponse generateSummary(SummaryRequest request) {
    String processedText = preprocess(request.getText());
    String aiResponse = aiClient.callSummarization(processedText, request.getSummaryType());
    return formatResponse(aiResponse);
}
```

---

## 8. Prompt Strategy

### Short Summary

```
Summarize the following text in 3 concise sentences:
```

### Bullet Summary

```
Summarize the following text into key bullet points:
```

### Detailed Summary

```
Provide a detailed explanation of the following text:
```

---

## 9. Error Handling

| Scenario           | Handling                 |
| ------------------ | ------------------------ |
| Empty input        | Return 400 Bad Request   |
| Large file         | Apply chunking strategy  |
| AI failure         | Retry + fallback message |
| Unsupported format | Return validation error  |

---

## 10. Optimization Techniques

* Chunk large documents before processing
* Cache repeated summaries
* Use async processing for large inputs
* Token limit management

---

## 11. Testing Strategy

### Unit Tests

* Service logic validation
* Input preprocessing

### Integration Tests

* API response validation
* AI client mocking

### Edge Cases

* Empty text
* Extremely large input
* Special characters

---

## 12. Future Enhancements

* Multi-language summarization
* Voice-based input
* Highlight key sections in original document
* Custom domain-specific summarization

---

## 13. Tech Stack

* Java + Spring Boot
* REST APIs
* LLM API (OpenAI / similar)
* Jackson (JSON processing)
* Apache Tika (for document parsing)

---

## 14. Time Breakdown (45 mins)

| Task                    | Time |
| ----------------------- | ---- |
| API + Controller        | 10m  |
| Service Logic           | 15m  |
| AI Integration          | 10m  |
| Testing + Edge Handling | 10m  |

---

## 15. Key Takeaways

* Focus on clean API design
* Optimize for performance and token usage
* Handle edge cases early
* Keep prompt design simple and deterministic

---

## 16. Implementation Status

### Backend Implementation Complete

The Spring Boot backend has been successfully implemented with the following components:

#### Project Structure
```
src/main/java/com/documentsummarizer/
├── DocumentSummarizerApplication.java    # Main Spring Boot application
├── controller/
│   └── SummaryController.java            # REST controller with POST /api/v1/summarize
├── dto/
│   ├── SummaryRequest.java               # Request DTO with validation
│   ├── SummaryResponse.java              # Response DTO
│   └── ErrorResponse.java                # Error response structure
├── service/
│   ├── SummarizationService.java         # Service interface
│   ├── SummarizationServiceImpl.java     # Service implementation
│   └── TextParserService.java           # Text preprocessing service
├── client/
│   └── MockAiClient.java                 # Mock AI client with realistic responses
└── exception/
    └── GlobalExceptionHandler.java       # Global exception handler
```

#### Key Features Implemented

1. **REST API Endpoint**: `POST /api/v1/summarize`
2. **Request Validation**:
   - `text`: Required, minimum 10 characters
   - `summaryType`: Required enum (SHORT, BULLET, DETAILED)
   - `maxLength`: Optional integer
3. **Mock AI Integration**: Simulates AI responses with realistic delays and varied content
4. **Error Handling**: Global exception handler with structured error responses
5. **Text Preprocessing**: Basic text cleaning and normalization
6. **Comprehensive Testing**: Unit tests, integration tests, and edge case coverage

#### API Details

**Request Example:**
```json
{
  "text": "Artificial intelligence is transforming industries by automating tasks...",
  "summaryType": "BULLET",
  "maxLength": 200
}
```

**Response Example:**
```json
{
  "status": "success",
  "summaryType": "BULLET",
  "summary": "• Primary theme: technology\n• Key argument: systematic change requires coordinated effort\n• Supporting evidence: empirical studies and data analysis\n• Main conclusion: further research and development is warranted\n• Implications: potential for widespread adoption"
}
```

#### Running the Application

1. **Prerequisites**:
   - Java 17 or higher
   - Maven 3.6+

2. **Build and Run**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. **Application Properties**:
   - Server runs on port 9090
   - Spring Boot 3.2.5
   - Auto-configured with Spring Web, Validation, Jackson

4. **Testing**:
   ```bash
   mvn test
   ```

#### Testing Coverage

- **Unit Tests**: Controller, Service, and Mock AI client tests
- **Integration Tests**: Full API endpoint testing
- **Edge Cases**: Empty text, invalid enum values, validation errors
- **Mock AI Responses**: Realistic, varied responses based on input length and type

#### Next Steps for Production

1. **Replace Mock AI Client** with real AI provider (OpenAI, Anthropic, etc.)
2. **Implement file parsing** for PDF, CSV, DOCX formats
3. **Add authentication** and rate limiting
4. **Implement caching** for repeated summaries
5. **Add monitoring** and logging
6. **Deploy to cloud** (AWS, Azure, GCP)

---

## 17. UI Tasks (Frontend Integration)

### Objective

Provide a simple user interface to interact with the Document Summarizer API for better usability and demonstration.

---

### UI Development Tasks

#### 1. Input Handling

* Create a textarea for raw text input
* Add file upload support (PDF, TXT, CSV)
* Validate empty input before submission

#### 2. User Controls

* Dropdown for selecting `summary_type`:

  * Short
  * Bullet
  * Detailed
* Optional input for `max_length`

#### 3. API Integration

* Connect UI with backend endpoint: `POST /api/v1/summarize`
* Send request payload in JSON format
* Handle API success and error responses

#### 4. Loading & Feedback

* Show loader/spinner while API is processing
* Disable button during request execution
* Display error messages for failures

#### 5. Output Rendering

* Display summary in readable format:

  * Paragraph (short/detailed)
  * Bullet list (bullet type)
* Add "Copy to Clipboard" functionality

#### 6. UI/UX Enhancements

* Responsive layout (mobile + desktop)
* Clean and minimal design
* Proper spacing and readability
* Input/output section separation

#### 7. Optional Features (Nice to Have)

* Drag & drop file upload
* Download summary as `.txt` or `.pdf`
* Maintain history of previous summaries
* Token usage indicator (if available)

---

### Suggested Tech Stack (UI)

* React (preferred)
* Tailwind CSS (for styling)
* Axios / Fetch API (for HTTP calls)

---

### Time Allocation (UI within 45 mins scope)

| Task                    | Time |
| ----------------------- | ---- |
| Basic UI setup          | 10m  |
| API integration         | 10m  |
| Loader + error handling | 5m   |
| Output rendering        | 5m   |
| Minor styling           | 5m   |

---

### Key Considerations

* Keep UI minimal and functional
* Ensure smooth API communication
* Focus on clarity of output presentation
* Avoid over-engineering within time constraint
