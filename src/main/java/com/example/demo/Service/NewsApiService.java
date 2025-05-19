package com.example.demo.Service;

import com.example.demo.Model.NewsItem;
import com.example.demo.NewsApiArticle;
import com.example.demo.NewsApiResponse;
import com.example.demo.Repository.NewsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class NewsApiService {

    @Value("${newsapi.key}")
    private String apiKey;

    @Autowired
    private NewsRepository newsRepo;

    @Autowired
    private RestTemplate restTemplate;

    private final List<String> categories = Arrays.asList(
            "technology", "sports", "business", "science", "health", "entertainment", "general"
    );

    @PostConstruct
    public void init() {
        System.out.println("📢 NEWS API KEY (env): " + (apiKey != null ? apiKey : "❌ NOT SET"));
    }

    public void fetchAndStoreNews() {
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("❌ NEWS API KEY is missing — aborting news fetch.");
            return;
        }

        for (String category : categories) {
            for (int page = 1; page <= 2; page++) {
                String url = String.format(
                        "https://newsapi.org/v2/top-headlines?country=us&category=%s&pageSize=20&page=%d&apiKey=%s",
                        category, page, apiKey
                );

                try {
                    ResponseEntity<NewsApiResponse> response = restTemplate.getForEntity(url, NewsApiResponse.class);
                    System.out.println("🌐 NewsAPI response [" + category + " page " + page + "]: " + response.getStatusCode());

                    if (response.getBody() != null && response.getBody().getArticles() != null) {
                        for (NewsApiArticle article : response.getBody().getArticles()) {
                            if (article.getTitle() == null || article.getSource() == null || article.getSource().getName() == null)
                                continue;

                            NewsItem item = new NewsItem();
                            item.setTitle(article.getTitle());
                            item.setCategory(category);
                            item.setSource(article.getSource().getName());
                            item.setImageUrl(article.getUrlToImage());
                            item.setUrl(article.getUrl());
                            item.setPublishedAt(article.getPublishedAt());
                            item.setContent(article.getDescription() != null ? article.getDescription() : "No content");

                            if (!newsRepo.existsByTitleAndSource(item.getTitle(), item.getSource())) {
                                newsRepo.save(item);
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println("❌ Error calling NewsAPI [" + category + " page " + page + "]: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}