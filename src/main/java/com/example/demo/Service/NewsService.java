package com.example.demo.Service;

import com.example.demo.Model.NewsItem;
import com.example.demo.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepo;

    public List<NewsItem> getAllNews() {
        return newsRepo.findAllByOrderByPublishedAtDesc();
    }

    public List<NewsItem> getByCategory(String category) {
        return newsRepo.findByCategory(category);
    }

    public NewsItem getById(Long id) {
        return newsRepo.findById(id).orElse(null);
    }

    public void addNews(NewsItem item) {
        newsRepo.save(item);
    }
}