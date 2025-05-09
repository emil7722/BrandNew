package com.example.demo.Model;

public class NewsItem {
    private Long id;
    private String title;
    private String source;
    private String category;
    private String content;

    public NewsItem(Long id, String title, String source, String category, String content) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.category = category;
        this.content = content;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getSource() { return source; }
    public String getCategory() { return category; }
    public String getContent() {return content; }
}
