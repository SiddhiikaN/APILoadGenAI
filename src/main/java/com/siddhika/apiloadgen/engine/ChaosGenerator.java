package com.siddhika.apiloadgen.engine;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

public class ChaosGenerator {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String GROQ_API_KEY = dotenv.get("GROQ_API_KEY");
    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";

    public String generate() {
        try {
            String body = """
                {
                    "model": "openai/gpt-oss-20b",
                    "messages": [
                        {
                            "role": "user",
                            "content": "Generate a random chaotic JSON payload for API stress testing. Return only raw JSON, no explanation."
                        }
                    ]
                }
                """;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GROQ_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + GROQ_API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return extractContent(response.body());

        } catch (Exception e) {
            return "{\"error\": \"chaos generation failed\"}";
        }
    }

    private String extractContent(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            return root.get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            return "{\"fallback\": true}";
        }
    }
}