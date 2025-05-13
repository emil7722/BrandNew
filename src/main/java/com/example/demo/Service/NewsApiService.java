package com.example.demo.Service;

import com.example.demo.Model.NewsItem;
import com.example.demo.NewsApiArticle;
import com.example.demo.NewsApiResponse;
import com.example.demo.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NewsApiService {

    @Value("${newsapi.key}")
    private String apiKey;

    @Autowired
    private NewsRepository newsRepo;

    @Autowired
    private RestTemplate restTemplate;

    private final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines?country=us&pageSize=10&apiKey=%s";

    public void fetchAndStoreNews() {
        String url = String.format(NEWS_API_URL, apiKey);

        NewsApiResponse response = restTemplate.getForObject(url, NewsApiResponse.class);

        if (response != null && response.getArticles() != null) {
            for (NewsApiArticle article : response.getArticles()) {
                NewsItem item = new NewsItem();
                item.setTitle(article.getTitle());
                item.setCategory("general");
                item.setSource(article.getSource().getName());
                item.setImageUrl(article.getUrlToImage());
                item.setUrl(article.getUrl());
                item.setContent(article.getDescription() != null ? article.getDescription() : "No content");

                if (!newsRepo.existsByTitleAndSource(item.getTitle(), item.getSource())) {
                    newsRepo.save(item);
                }
            }
        }
    }
}
