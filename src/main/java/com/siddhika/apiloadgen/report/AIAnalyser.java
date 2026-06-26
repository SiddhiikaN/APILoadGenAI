package com.siddhika.apiloadgen.report;

import com.siddhika.apiloadgen.engine.MetricsCollector;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

public class AIAnalyser {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String GROQ_API_KEY = dotenv.get("GROQ_API_KEY");
    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";

    private final MetricsCollector metrics;

    public AIAnalyser(MetricsCollector metrics) {
        this.metrics = metrics;
    }

    public void analyse() {
        try {
            String summary = String.format(
                    "Total: %d, Success: %d, Errors: %d, AvgLatency: %dms, p95: %dms, p99: %dms",
                    metrics.getTotalRequests(), metrics.getSuccessCount(), metrics.getErrorCount(),
                    metrics.getAverageLatency(), metrics.getPercentile(95), metrics.getPercentile(99));

            String escapedSummary = summary.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
                    .replace("\r", "\\r");

            String body = "{\"model\": \"llama-3.3-70b-versatile\", \"messages\": [{\"role\": \"user\", \"content\": \"Analyze this load test result and give a short summary with recommendations: "
                    + escapedSummary + "\"}]}";

            // System.out.println("Request body: " + body);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GROQ_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + GROQ_API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // System.out.println("Raw response: " + response.body());
            System.out.println("\n========== AI Analysis ==========");
            System.out.println(extractAnalysis(response.body()));
            System.out.println("=================================\n");

        } catch (Exception e) {
            System.out.println("AI Analysis failed: " + e.getMessage());
        }
    }

    private String extractAnalysis(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            return root.get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            return "Could not parse AI response";
        }
    }
}