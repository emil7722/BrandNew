package com.example.demo.Service;

import com.example.demo.Model.NewsItem;
import com.example.demo.NewsApiArticle;
import com.example.demo.NewsApiResponse;
import com.example.demo.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private final List<String> categories = Arrays.asList("technology", "sports", "business", "science", "health", "entertainment", "general");

    public void fetchAndStoreNews() {
        for (String category : categories) {
            for (int page = 1; page <= 2; page++) { // fetch 2 pages per category
                String url = String.format(
                        "https://newsapi.org/v2/top-headlines?country=us&category=%s&pageSize=20&page=%d&apiKey=%s",
                        category, page, apiKey
                );

                NewsApiResponse response = restTemplate.getForObject(url, NewsApiResponse.class);

                if (response != null && response.getArticles() != null) {
                    for (NewsApiArticle article : response.getArticles()) {
                        if (article.getTitle() == null || article.getSource() == null || article.getSource().getName() == null)
                            continue;

                        NewsItem item = new NewsItem();
                        item.setTitle(article.getTitle());
                        item.setCategory(category); // dynamically assign category
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
            }
        }
    }
}