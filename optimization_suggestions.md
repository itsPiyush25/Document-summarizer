# Optimization Suggestions for AI Feature

Based on the analysis of `optimizing.md` and the current implementation, here are concrete suggestions to address the identified problems without breaking the existing flow.

## Problems Identified

1. **High token usage** – Sending full text to LLM each time.
2. **Slow response** – Latency from AI API and processing delays.
3. **Irrelevant output** – Summaries may miss key points or include fluff.
4. **Repeated prompts** – Same text generates same prompt multiple times.
5. **No caching** – No reuse of previous summaries.

## Suggested Changes

### 1. Prompt Optimization Strategies

**Current State**: The mock client uses hard‑coded templates; real LLM prompts would need to be designed.

**Improvements**:

- **Structured Prompt Templates**: Create separate prompt templates for each summary type (SHORT, BULLET, DETAILED) with clear instructions.
  - Example for BULLET: “Provide 3‑5 bullet points covering the main theme, key arguments, evidence, conclusion, and implications. Each bullet must start with '•' and be concise.”
  - Include **output format constraints** (e.g., “Maximum 200 characters”, “Use plain language”).

- **System Role Definition**: Start each prompt with a system message that sets the AI’s role: “You are a expert document summarizer. Your task is to extract the most important information and present it clearly.”

- **Few‑Shot Examples**: For better accuracy, include 1‑2 example inputs and desired outputs in the prompt (especially for BULLET and DETAILED). This guides the LLM to follow the expected structure.

- **Dynamic Prompt Adjustments**: Based on text length, adjust the prompt. For very long texts, instruct the model to focus on the first 1000 words or to summarize section by section.

**Implementation Note**: These changes can be encapsulated in a `PromptBuilder` class that constructs the prompt string based on `summaryType` and `maxLength`. The `MockAiClient` can be extended to simulate these improved prompts, while the real AI client would use the same builder.

### 2. Token Reduction Techniques

**Current State**: The entire input text is sent to the mock client (no token limit). With a real LLM, token cost scales with input size.

**Improvements**:

- **Text Chunking**: For texts exceeding a threshold (e.g., 3000 tokens), split into logical chunks (paragraphs, sections), summarize each chunk separately, then combine the chunk summaries into a final summary (map‑reduce approach).

- **Extractive Pre‑processing**: Use a simple extractive summarization algorithm (e.g., TextRank, TF‑IDF) to select the most important sentences before sending to the LLM. This reduces token count while preserving key content.

- **Smart Truncation**: If the text is too long, truncate to the first `maxLength` characters but keep the concluding paragraphs (where conclusions often appear). Better: keep the first 30% and last 30% of the text.

- **Token Counting**: Integrate a tokenizer (e.g., using `tiktoken` for OpenAI) to count tokens before sending and warn/truncate automatically.

**Implementation Note**: Add a `TextPreprocessor` service that performs chunking/extraction before calling the AI client. The existing `TextParserService` can be extended for this purpose.

### 3. Caching Mechanism

**Current State**: No caching; identical requests cause repeated AI calls.

**Improvements**:

- **In‑Memory Cache**: Use a simple `ConcurrentHashMap` to store summaries keyed by a hash of (text + summaryType + maxLength). Set a TTL (e.g., 1 hour) to avoid stale entries.

- **Distributed Cache**: For scalability, integrate Redis or Caffeine cache. Spring Boot’s `@Cacheable` annotation can be added to the service method.

- **Cache Invalidation**: Invalidate cache entries when the underlying AI model changes or when a user explicitly requests a fresh summary.

- **Cache‑Aside Pattern**: Check cache before calling AI; if miss, call AI and store result.

**Implementation Note**: Add a `CacheService` bean and inject it into `SummarizationServiceImpl`. Use `@Cacheable` on `generateSummary` method. Ensure the cache key includes all relevant parameters.

### 4. Latency Reduction

**Current State**: Mock client simulates 300‑800 ms delay; real AI calls can be several seconds.

**Improvements**:

- **Parallel Processing**: For chunked summaries, process chunks in parallel (using `CompletableFuture` or Reactor) and combine results.

- **Async API Endpoint**: Change the REST endpoint to be asynchronous (`@Async` or `DeferredResult`). Return a job ID immediately, let the client poll or use WebSocket for completion.

- **Prefetching**: If the user is typing a long document, start processing after a short idle period (debounce) to have the summary ready sooner.

- **Optimized Network Calls**: Use HTTP/2, keep‑alive connections, and batch requests if multiple summaries are needed.

- **Fallback to Quick Summary**: If the AI call times out, provide a fallback extractive summary (using the pre‑processing step) to guarantee a response.

**Implementation Note**: Modify `SummaryController` to support async responses. The frontend would need to handle polling or SSE. The mock client’s delay can be reduced for development.

### 5. Accuracy Improvement (Relevant Output)

**Current State**: Mock client randomly picks from a list of generic phrases, which may not match the input content.

**Improvements**:

- **Content‑Aware Generation**: For real LLM, the prompt should explicitly reference the input text’s key terms. Use named‑entity recognition or keyword extraction to highlight important entities in the prompt.

- **User Feedback Loop**: Collect thumbs‑up/down on summaries and log them for fine‑tuning prompts.

- **Post‑Processing Validation**: Run a simple check that the generated summary does not exceed `maxLength` and contains no offensive language.

- **Multi‑Model Voting**: If cost permits, call two different LLM models (or the same model with different temperature) and pick the better summary (requires a quality metric).

**Implementation Note**: Enhance `MockAiClient` to generate more context‑aware summaries by extracting a few keywords from the input text and incorporating them into the response. This can be done by adding a simple keyword extractor.

## Implementation Roadmap

To avoid breaking the existing flow, implement changes incrementally:

1. **Phase 1 – Prompt & Token Optimization**
   - Create `PromptBuilder` and integrate with `MockAiClient` (no behavior change for existing tests).
   - Add `TextPreprocessor` with chunking and extractive summarization (optional feature flag).

2. **Phase 2 – Caching**
   - Add Spring Cache with a simple in‑memory provider. Cache can be disabled by default via configuration.
   - Ensure cache keys are consistent; existing API remains unchanged.

3. **Phase 3 – Async & Latency**
   - Introduce async endpoint (`/api/v1/summarize/async`) alongside the synchronous one.
   - Keep the original endpoint unchanged for backward compatibility.

4. **Phase 4 – Accuracy & Feedback**
   - Add keyword extraction to mock client.
   - Add a feedback endpoint (`POST /api/v1/summary/feedback`) to collect user ratings.

## Expected Benefits

- **Token Usage**: Reduced by 30‑70% via chunking and extractive pre‑processing.
- **Response Time**: Cached responses served in <50 ms; async processing improves perceived latency.
- **Relevance**: Improved prompts and content‑aware generation increase output quality.
- **Cost**: Lower token consumption directly reduces AI API costs.
- **Scalability**: Caching and async processing allow handling more concurrent users.

## Risks & Mitigations

- **Cache Stampede**: Use locking or probabilistic expiration. Mitigate with a short TTL.
- **Increased Complexity**: Keep the simple mock client as a fallback; use feature toggles.
- **User Experience**: Async endpoints require frontend changes; provide a seamless fallback to synchronous mode.

## Conclusion

These suggestions are designed to be implemented step‑by‑step without disrupting the current working system. Each improvement can be tested independently and rolled out with minimal risk. The architecture already supports swapping the AI client and adding new services, making these optimizations feasible.