# Document Summarizer API Testing Guide

This document provides examples for testing the Document Summarizer API using curl commands and Postman collection.

## API Endpoint

```
POST http://localhost:9090/api/v1/summarize
Content-Type: application/json
```

## curl Examples

### 1. Short Summary Request

```bash
curl -X POST http://localhost:9090/api/v1/summarize \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Artificial intelligence is transforming many industries by automating tasks and providing insights from data. Machine learning algorithms can analyze patterns and make predictions that were previously impossible. This technology is being applied in healthcare, finance, education, and many other sectors.",
    "summaryType": "SHORT",
    "maxLength": 150
  }'
```

### 2. Bullet Summary Request

```bash
curl -X POST http://localhost:9090/api/v1/summarize \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications. Key features include embedded servers, auto-configuration, production-ready features, and no code generation. It simplifies the development process and reduces boilerplate code.",
    "summaryType": "BULLET",
    "maxLength": 300
  }'
```

### 3. Detailed Summary Request

```bash
curl -X POST http://localhost:9090/api/v1/summarize \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Climate change refers to long-term shifts in temperatures and weather patterns. These shifts may be natural, but since the 1800s, human activities have been the main driver of climate change, primarily due to the burning of fossil fuels like coal, oil and gas. Burning fossil fuels generates greenhouse gas emissions that act like a blanket wrapped around the Earth, trapping the sun'\''s heat and raising temperatures.",
    "summaryType": "DETAILED",
    "maxLength": 500
  }'
```

### 4. Validation Error (Empty Text)

```bash
curl -X POST http://localhost:9090/api/v1/summarize \
  -H "Content-Type: application/json" \
  -d '{
    "text": "",
    "summaryType": "SHORT",
    "maxLength": 100
  }'
```

### 5. Validation Error (Missing Summary Type)

```bash
curl -X POST http://localhost:9090/api/v1/summarize \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Sample text for testing",
    "maxLength": 100
  }'
```

### 6. Text Too Short (Minimum 10 characters)

```bash
curl -X POST http://localhost:9090/api/v1/summarize \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Short",
    "summaryType": "SHORT",
    "maxLength": 100
  }'
```

## Expected Responses

### Success Response

```json
{
  "status": "success",
  "summaryType": "BULLET",
  "summary": "• Primary theme: technology\n• Key argument: systematic change requires coordinated effort\n• Supporting evidence: empirical studies and data analysis\n• Main conclusion: further research and development is warranted\n• Implications: potential for widespread adoption"
}
```

### Error Response (Validation)

```json
{
  "status": "error",
  "message": "Validation failed",
  "errors": [
    "text: must not be blank",
    "text: size must be between 10 and 10000"
  ],
  "timestamp": "2026-04-15T14:20:00.123456Z"
}
```

### Error Response (Internal Server Error)

```json
{
  "status": "error",
  "message": "An unexpected error occurred",
  "timestamp": "2026-04-15T14:20:00.123456Z"
}
```

## Postman Collection

You can import the following JSON into Postman:

```json
{
  "info": {
    "name": "Document Summarizer API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Short Summary",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"text\": \"Artificial intelligence is transforming many industries by automating tasks and providing insights from data. Machine learning algorithms can analyze patterns and make predictions that were previously impossible. This technology is being applied in healthcare, finance, education, and many other sectors.\",\n  \"summaryType\": \"SHORT\",\n  \"maxLength\": 150\n}"
        },
        "url": {
          "raw": "http://localhost:9090/api/v1/summarize",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9090",
          "path": ["api", "v1", "summarize"]
        }
      }
    },
    {
      "name": "Bullet Summary",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"text\": \"Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications. Key features include embedded servers, auto-configuration, production-ready features, and no code generation. It simplifies the development process and reduces boilerplate code.\",\n  \"summaryType\": \"BULLET\",\n  \"maxLength\": 300\n}"
        },
        "url": {
          "raw": "http://localhost:9090/api/v1/summarize",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9090",
          "path": ["api", "v1", "summarize"]
        }
      }
    },
    {
      "name": "Detailed Summary",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"text\": \"Climate change refers to long-term shifts in temperatures and weather patterns. These shifts may be natural, but since the 1800s, human activities have been the main driver of climate change, primarily due to the burning of fossil fuels like coal, oil and gas. Burning fossil fuels generates greenhouse gas emissions that act like a blanket wrapped around the Earth, trapping the sun's heat and raising temperatures.\",\n  \"summaryType\": \"DETAILED\",\n  \"maxLength\": 500\n}"
        },
        "url": {
          "raw": "http://localhost:9090/api/v1/summarize",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9090",
          "path": ["api", "v1", "summarize"]
        }
      }
    },
    {
      "name": "Validation Error - Empty Text",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"text\": \"\",\n  \"summaryType\": \"SHORT\",\n  \"maxLength\": 100\n}"
        },
        "url": {
          "raw": "http://localhost:9090/api/v1/summarize",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9090",
          "path": ["api", "v1", "summarize"]
        }
      }
    },
    {
      "name": "Validation Error - Missing Summary Type",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"text\": \"Sample text for testing\",\n  \"maxLength\": 100\n}"
        },
        "url": {
          "raw": "http://localhost:9090/api/v1/summarize",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9090",
          "path": ["api", "v1", "summarize"]
        }
      }
    }
  ]
}
```

## Testing with PowerShell (Windows)

If you're using PowerShell on Windows, use these commands:

```powershell
$headers = @{
    "Content-Type" = "application/json"
}

$body = @{
    text = "Artificial intelligence is transforming many industries by automating tasks and providing insights from data."
    summaryType = "SHORT"
    maxLength = 150
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:9090/api/v1/summarize" -Method Post -Headers $headers -Body $body
```

## Testing Notes

1. **Start the application** before testing:
   ```bash
   mvn spring-boot:run
   ```

2. **Response Time**: The mock AI client simulates processing delay (300-800ms) for realism.

3. **Validation Rules**:
   - `text`: Required, minimum 10 characters, maximum 10000 characters
   - `summaryType`: Required, must be one of: SHORT, BULLET, DETAILED
   - `maxLength`: Optional, must be positive integer if provided

4. **Testing Edge Cases**:
   - Very long text (over 1000 characters)
   - Special characters and Unicode
   - Multiple paragraphs
   - HTML tags (they will be stripped by the text parser)

5. **Performance**: The mock implementation handles all requests synchronously. In production, consider async processing for large documents.