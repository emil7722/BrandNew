package com.example.demo.Service;

import com.example.demo.Model.Bookmark;
import com.example.demo.Model.NewsItem;
import com.example.demo.Model.User;
import com.example.demo.Repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepo;

    public void addBookmark(User user, NewsItem newsItem) {
        if (!bookmarkRepo.existsByUserAndNewsItem(user, newsItem)) {
            Bookmark bookmark = new Bookmark();
            bookmark.setUser(user);
            bookmark.setNewsItem(newsItem);
            bookmarkRepo.save(bookmark);
        }
    }

    public void removeBookmark(User user, NewsItem newsItem) {
        bookmarkRepo.deleteByUserAndNewsItem(user, newsItem);
    }

    public List<Bookmark> getBookmarksByUser(User user) {
        return bookmarkRepo.findByUser(user);
    }
}