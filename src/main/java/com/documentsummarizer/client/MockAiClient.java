package com.documentsummarizer.client;

import com.documentsummarizer.dto.SummaryRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class MockAiClient {

    private final Random random = new Random();

    public String getSummary(String text, SummaryRequest.SummaryType summaryType) {
        // Simulate processing delay (between 300-800ms for realism)
        simulateProcessingDelay();

        // Analyze text length to adjust response
        int textLength = text.length();
        boolean isLongText = textLength > 500;
        boolean isShortText = textLength < 100;

        return switch (summaryType) {
            case SHORT -> generateShortSummary(text, isLongText, isShortText);
            case BULLET -> generateBulletSummary(text, isLongText, isShortText);
            case DETAILED -> generateDetailedSummary(text, isLongText, isShortText);
        };
    }

    private void simulateProcessingDelay() {
        try {
            // Random delay between 300-800ms
            int delay = 300 + random.nextInt(500);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String generateShortSummary(String text, boolean isLongText, boolean isShortText) {
        String[] shortSummaries = {
            "This text discusses " + extractTopic(text) + ". The main point is " + getMainPoint(text) + ".",
            "Key takeaway: " + getKeyTakeaway(text) + ". This is important because " + getReason(text) + ".",
            "In summary, the text focuses on " + extractTopic(text) + " with emphasis on " + getEmphasis(text) + ".",
            "The core message is about " + extractTopic(text) + ". The author argues that " + getArgument(text) + ".",
            "Briefly, this covers " + extractTopic(text) + ". The conclusion suggests " + getConclusion(text) + "."
        };
        
        if (isLongText) {
            return "This lengthy document about " + extractTopic(text) + " can be summarized as: " + 
                   shortSummaries[random.nextInt(shortSummaries.length)];
        } else if (isShortText) {
            return "The short input text discusses " + extractTopic(text) + ". Main idea: " + getMainPoint(text) + ".";
        }
        
        return shortSummaries[random.nextInt(shortSummaries.length)];
    }

    private String generateBulletSummary(String text, boolean isLongText, boolean isShortText) {
        List<String> bulletPoints;
        
        if (isLongText) {
            bulletPoints = Arrays.asList(
                "• Primary theme: " + extractTopic(text),
                "• Key argument: " + getArgument(text),
                "• Supporting evidence: " + getEvidence(text),
                "• Main conclusion: " + getConclusion(text),
                "• Implications: " + getImplications(text)
            );
        } else if (isShortText) {
            bulletPoints = Arrays.asList(
                "• Main point: " + getMainPoint(text),
                "• Context: " + getContext(text),
                "• Significance: " + getSignificance(text)
            );
        } else {
            bulletPoints = Arrays.asList(
                "• First key point: " + getKeyPoint(text, 1),
                "• Second important aspect: " + getKeyPoint(text, 2),
                "• Third consideration: " + getKeyPoint(text, 3),
                "• Final observation: " + getObservation(text)
            );
        }
        
        return String.join("\n", bulletPoints);
    }

    private String generateDetailedSummary(String text, boolean isLongText, boolean isShortText) {
        String baseSummary = "This comprehensive analysis of the provided text reveals several important aspects. ";
        
        if (isLongText) {
            return baseSummary + 
                   "The document, which spans approximately " + estimateWordCount(text) + " words, primarily addresses " + 
                   extractTopic(text) + ". The author presents a detailed examination of " + getSubject(text) + 
                   ", exploring various dimensions including " + getDimensions(text) + ". " +
                   "Key arguments are supported by " + getSupport(text) + ", leading to the conclusion that " + 
                   getConclusion(text) + ". This analysis provides valuable insights for understanding " + 
                   extractTopic(text) + " in greater depth.";
        } else if (isShortText) {
            return "Despite its brevity, this text offers meaningful content about " + extractTopic(text) + 
                   ". The author conveys that " + getMainPoint(text) + ", which is significant because " + 
                   getReason(text) + ". While concise, the text effectively communicates its message about " + 
                   extractTopic(text) + ".";
        } else {
            return baseSummary + 
                   "The text explores " + extractTopic(text) + " through multiple perspectives. " +
                   "It begins by establishing " + getContext(text) + ", then proceeds to analyze " + 
                   getAnalysis(text) + ". The discussion highlights " + getHighlights(text) + 
                   ", ultimately arriving at the insight that " + getInsight(text) + ". " +
                   "This detailed summary captures the essence and nuances of the original content.";
        }
    }

    // Helper methods to generate realistic text components
    private String extractTopic(String text) {
        String[] topics = {"technology", "business", "science", "education", "health", "environment", "politics", "culture"};
        return topics[random.nextInt(topics.length)];
    }

    private String getMainPoint(String text) {
        String[] points = {
            "improving efficiency through innovation",
            "addressing challenges with creative solutions", 
            "exploring new opportunities in emerging markets",
            "understanding complex systems through analysis",
            "balancing competing priorities effectively"
        };
        return points[random.nextInt(points.length)];
    }

    private String getKeyTakeaway(String text) {
        String[] takeaways = {
            "collaboration enhances outcomes",
            "data-driven decisions yield better results",
            "adaptability is crucial in changing environments",
            "sustainability should be a core consideration",
            "user experience determines long-term success"
        };
        return takeaways[random.nextInt(takeaways.length)];
    }

    private String getReason(String text) {
        String[] reasons = {
            "it aligns with current trends and research",
            "historical evidence supports this approach",
            "it addresses fundamental human needs",
            "economic factors make this necessary",
            "technological advancements enable new possibilities"
        };
        return reasons[random.nextInt(reasons.length)];
    }

    private String getEmphasis(String text) {
        String[] emphases = {
            "practical applications",
            "theoretical foundations", 
            "ethical considerations",
            "implementation strategies",
            "measurable outcomes"
        };
        return emphases[random.nextInt(emphases.length)];
    }

    private String getArgument(String text) {
        String[] arguments = {
            "systematic change requires coordinated effort",
            "innovation must be balanced with stability",
            "long-term planning outweighs short-term gains",
            "inclusive approaches produce more robust solutions",
            "transparency builds trust and engagement"
        };
        return arguments[random.nextInt(arguments.length)];
    }

    private String getConclusion(String text) {
        String[] conclusions = {
            "further research and development is warranted",
            "a balanced approach yields optimal results",
            "stakeholder collaboration is essential",
            "continuous improvement should be prioritized",
            "strategic alignment drives success"
        };
        return conclusions[random.nextInt(conclusions.length)];
    }

    private String getEvidence(String text) {
        String[] evidences = {
            "empirical studies and data analysis",
            "case studies from similar contexts",
            "expert opinions and industry benchmarks",
            "historical precedents and patterns",
            "statistical models and projections"
        };
        return evidences[random.nextInt(evidences.length)];
    }

    private String getImplications(String text) {
        String[] implications = {
            "potential for widespread adoption",
            "impact on related fields and industries",
            "considerations for policy and regulation",
            "opportunities for further innovation",
            "challenges in implementation and scaling"
        };
        return implications[random.nextInt(implications.length)];
    }

    private String getContext(String text) {
        String[] contexts = {
            "the current landscape and prevailing conditions",
            "historical background and evolution of the topic",
            "theoretical frameworks and conceptual models",
            "practical constraints and real-world limitations",
            "stakeholder perspectives and competing interests"
        };
        return contexts[random.nextInt(contexts.length)];
    }

    private String getSignificance(String text) {
        String[] significances = {
            "its relevance to contemporary issues",
            "potential impact on future developments",
            "contribution to existing knowledge",
            "practical applications and utility",
            "theoretical implications and insights"
        };
        return significances[random.nextInt(significances.length)];
    }

    private String getKeyPoint(String text, int index) {
        String[][] keyPoints = {
            {"identifying core problems", "recognizing patterns", "defining objectives"},
            {"analyzing root causes", "evaluating alternatives", "assessing risks"},
            {"developing solutions", "implementing strategies", "measuring outcomes"},
            {"refining approaches", "scaling successes", "addressing limitations"}
        };
        int safeIndex = Math.min(index - 1, keyPoints.length - 1);
        String[] points = keyPoints[safeIndex];
        return points[random.nextInt(points.length)];
    }

    private String getObservation(String text) {
        String[] observations = {
            "the need for ongoing evaluation and adjustment",
            "the importance of stakeholder engagement",
            "the value of interdisciplinary approaches",
            "the potential for unexpected outcomes",
            "the requirement for adequate resources and support"
        };
        return observations[random.nextInt(observations.length)];
    }

    private String estimateWordCount(String text) {
        int words = text.split("\\s+").length;
        // Round to nearest 50 for realism
        int rounded = ((words + 25) / 50) * 50;
        return String.valueOf(rounded);
    }

    private String getSubject(String text) {
        String[] subjects = {
            "underlying mechanisms and processes",
            "key variables and their interactions",
            "structural components and their functions",
            "dynamic relationships and dependencies",
            "emergent properties and behaviors"
        };
        return subjects[random.nextInt(subjects.length)];
    }

    private String getDimensions(String text) {
        String[] dimensions = {
            "technical, social, and economic factors",
            "short-term and long-term implications",
            "local, regional, and global impacts",
            "quantitative and qualitative aspects",
            "theoretical and practical considerations"
        };
        return dimensions[random.nextInt(dimensions.length)];
    }

    private String getSupport(String text) {
        String[] supports = {
            "extensive research and empirical data",
            "expert analysis and professional judgment",
            "comparative studies and benchmarking",
            "theoretical models and conceptual frameworks",
            "pilot projects and experimental results"
        };
        return supports[random.nextInt(supports.length)];
    }

    private String getAnalysis(String text) {
        String[] analyses = {
            "underlying causes and contributing factors",
            "potential solutions and their feasibility",
            "stakeholder interests and priorities",
            "risk factors and mitigation strategies",
            "performance metrics and evaluation criteria"
        };
        return analyses[random.nextInt(analyses.length)];
    }

    private String getHighlights(String text) {
        String[] highlights = {
            "critical insights and key findings",
            "innovative approaches and novel methods",
            "significant challenges and obstacles",
            "promising opportunities and advantages",
            "important limitations and caveats"
        };
        return highlights[random.nextInt(highlights.length)];
    }

    private String getInsight(String text) {
        String[] insights = {
            "holistic understanding leads to better decisions",
            "adaptation is more effective than rigid planning",
            "diversity of thought enhances problem-solving",
            "sustainability requires systemic change",
            "technology should serve human needs, not dictate them"
        };
        return insights[random.nextInt(insights.length)];
    }
}