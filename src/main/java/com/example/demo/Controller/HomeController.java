package com.example.demo.Controller;

import com.example.demo.Model.NewsItem;
import com.example.demo.Service.NewsService;
import jakarta.servlet.http.HttpSession;
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
    public String home(Model model, HttpSession session) {
        model.addAttribute("newsList", newsService.getAllNews());
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "homepage";
    }

    @GetMapping("/category/{category}")
    public String byCategory(@PathVariable String category, Model model, HttpSession session) {
        List<NewsItem> filtered = newsService.getByCategory(category);
        model.addAttribute("newsList", filtered);
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "homepage";
    }

    @GetMapping("/news/{id}")
    public String newsDetail(@PathVariable Long id, Model model) {
        NewsItem item = newsService.getById(id);
        if (item == null) return "404";
        model.addAttribute("news", item);
        return "news";
    }
}
