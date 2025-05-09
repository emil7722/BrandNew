package com.example.demo.Controller;

import com.example.demo.Model.NewsItem;
import com.example.demo.Service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("newsList", newsService.getAllNews());
        return "homepage"; // renders templates/index.html
    }

    @GetMapping("/category/{category}")
    public String byCategory(@PathVariable String category, Model model) {
        List<NewsItem> filtered = newsService.getByCategory(category);
        model.addAttribute("newsList", filtered);
        return "homepage";
    }

    @GetMapping("/news/{id}")
    public String newsDetail(@PathVariable Long id, Model model) {
        NewsItem item = newsService.getById(id);
        if (item == null) return "404";
        model.addAttribute("news", item);
        return "news"; // renders templates/news.html
    }
}
