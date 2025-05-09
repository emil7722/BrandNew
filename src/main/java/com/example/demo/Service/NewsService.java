package com.example.demo.Service;

import com.example.demo.Model.NewsItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    public List<NewsItem> getAllNews() {
        return List.of(
                new NewsItem(1L, "Tech in 2025", "TechCrunch", "technology"),
                new NewsItem(2L, "Elections Coming", "BBC News", "politics"),
                new NewsItem(3L, "Championship Final", "ESPN", "sports")
        );
    }

    public List<NewsItem> getByCategory(String category) {
        return getAllNews().stream()
                .filter(n -> n.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public NewsItem getById(Long id) {
        return getAllNews().stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
