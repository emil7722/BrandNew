package com.example.demo.Controller;

import com.example.demo.Model.NewsItem;
import com.example.demo.Model.User;
import com.example.demo.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private BookmarkService bookmarkService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())
                ? auth.getName()
                : null;
        newsApiService.fetchAndStoreNews();
        model.addAttribute("newsList", newsService.getAllNews());
        model.addAttribute("loggedInUser", email);
        return "homepage";
    }

    @GetMapping("/category/{category}")
    public String byCategory(@PathVariable String category, Model model, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())
                ? auth.getName()
                : null;
        model.addAttribute("newsList", newsService.getByCategory(category));
        model.addAttribute("loggedInUser", email);
        return "homepage";
    }

    @GetMapping("/news/{id}")
    public String newsDetail(@PathVariable Long id, Model model) {
        NewsItem item = newsService.getById(id);
        if (item == null) return "404";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())
                ? auth.getName()
                : null;

        model.addAttribute("news", item);
        model.addAttribute("loggedInUser", email);
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
    public String createNewsForm(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByEmail(email).orElse(null);

        if (user == null || !email.equals("admin@brandnews.com") || !user.getRole().equalsIgnoreCase("ROLE_ADMIN")) {
            return "redirect:/"; // Unauthorized
        }

        model.addAttribute("loggedInUser", email);
        model.addAttribute("newsItem", new NewsItem());
        return "create-news";
    }

    @PostMapping("/admin/news/create")
    public String handleNewsSubmit(@ModelAttribute NewsItem newsItem, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByEmail(email).orElse(null);

        if (user == null || !email.equals("admin@brandnews.com") || !user.getRole().equalsIgnoreCase("ROLE_ADMIN")) {
            return "redirect:/"; // Unauthorized
        }

        model.addAttribute("loggedInUser", email);
        newsService.addNews(newsItem);
        return "redirect:/";
    }

    @PostMapping("/news/{id}/bookmark")
    public String bookmarkNews(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())
                ? auth.getName() : null;

        if (email != null) {
            User user = userService.getByEmail(email).orElse(null);
            NewsItem newsItem = newsService.getById(id);

            if (user != null && newsItem != null) {
                bookmarkService.addBookmark(user, newsItem);
            }
        }

        return "redirect:/news/" + id;
    }


    private boolean isAdmin(HttpSession session) {
        String email = (String) session.getAttribute("loggedInUser");
        if (email == null) return false;
        User user = userService.getByEmail(email).orElse(null);
        return user != null && email.equals("admin@brandnews.com") && user.getPassword().equals("Manhattan2");
    }
}