package com.example.demo.Service;

import com.example.demo.Model.NewsItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    public List<NewsItem> getAllNews() {
        return List.of(
                new NewsItem(1L, "Tech in 2025", "TechCrunch", "technology",
                        "In 2025, AI and quantum computing are expected to merge, enabling faster simulations and advanced prediction models."),
                new NewsItem(2L, "Elections Coming", "BBC News", "politics",
                        "The upcoming elections are projected to have record-breaking turnout, with young voters leading the charge."),
                new NewsItem(3L, "Championship Final", "ESPN", "sports",
                        "The thrilling final saw an underdog team defeat last year's champions in a dramatic penalty shootout.")
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
