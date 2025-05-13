package com.example.demo.Controller;

import com.example.demo.Model.NewsItem;
import com.example.demo.Model.User;
import com.example.demo.Service.*;
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

    @Autowired
    private UserService userService;

    @Autowired
    private NewsApiService newsApiService;

    @Autowired
    private AimlApiService aimlApiService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        newsApiService.fetchAndStoreNews();
        model.addAttribute("newsList", newsService.getAllNews());
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "homepage";
    }

    @GetMapping("/category/{category}")
    public String byCategory(@PathVariable String category, Model model, HttpSession session) {
        model.addAttribute("newsList", newsService.getByCategory(category));
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "homepage";
    }

    @GetMapping("/news/{id}")
    public String newsDetail(@PathVariable Long id, Model model, HttpSession session) {
        NewsItem item = newsService.getById(id);
        if (item == null) return "404";
        model.addAttribute("news", item);
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "news";
    }

    @PostMapping("/news/{id}/summarize")
    public String summarize(@PathVariable Long id, Model model, HttpSession session) {
        NewsItem item = newsService.getById(id);
        if (item == null) return "404";

        model.addAttribute("news", item);
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));

        String summary = aimlApiService.summarizeText(item.getContent());
        model.addAttribute("summary", summary);

        return "news";
    }

    @GetMapping("/admin/news/create")
    public String createNewsForm(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loggedInUser");
        User user = userService.getByEmail(email).orElse(null);

        if (user == null || !email.equals("admin@brandnews.com") || !user.getPassword().equals("Manhattan2")) {
            return "redirect:/"; // unauthorized
        }

        model.addAttribute("newsItem", new NewsItem());
        return "create-news";
    }

    @PostMapping("/admin/news/create")
    public String handleNewsSubmit(@ModelAttribute NewsItem newsItem, HttpSession session) {
        String email = (String) session.getAttribute("loggedInUser");
        User user = userService.getByEmail(email).orElse(null);

        if (user == null || !email.equals("admin@brandnews.com") || !user.getPassword().equals("Manhattan2")) {
            return "redirect:/"; // block unauthorized post
        }

        newsService.addNews(newsItem);
        return "redirect:/";
    }

    private boolean isAdmin(HttpSession session) {
        String email = (String) session.getAttribute("loggedInUser");
        if (email == null) return false;
        User user = userService.getByEmail(email).orElse(null);
        return user != null && email.equals("admin@brandnews.com") && user.getPassword().equals("Manhattan2");
    }
}