package com.example.demo.Repository;

import com.example.demo.Model.NewsItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<NewsItem, Long> {
    List<NewsItem> findByCategory(String category);
}