package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AimlApiService {

    @Value("${aiml.api.key}")
    private String apiKey;

    private final String baseUrl = "https://api.aimlapi.com/v1";

    public String summarizeText(String text) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", new Object[]{
                        Map.of("role", "system", "content", "You are a helpful assistant that summarizes news articles."),
                        Map.of("role", "user", "content", "Summarize the following news article in 8–9 clear and informative sentences:\n\n" + text)
                }
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/chat/completions", request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                Map<String, Object> choice = ((List<Map<String, Object>>) responseBody.get("choices")).get(0);
                Map<String, Object> message = (Map<String, Object>) choice.get("message");
                return (String) message.get("content");
            } else {
                return "No summary available.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error during summarization.";
        }
    }
}