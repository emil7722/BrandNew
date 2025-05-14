package com.example.demo.Repository;

import com.example.demo.Model.Bookmark;
import com.example.demo.Model.User;
import com.example.demo.Model.NewsItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUser(User user);
    boolean existsByUserAndNewsItem(User user, NewsItem newsItem);
    void deleteByUserAndNewsItem(User user, NewsItem newsItem);
}