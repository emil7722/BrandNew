// NewsApiResponse.java
package com.example.demo;

import java.util.List;

public class NewsApiResponse {
    private List<NewsApiArticle> articles;

    public List<NewsApiArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsApiArticle> articles) {
        this.articles = articles;
    }
}

