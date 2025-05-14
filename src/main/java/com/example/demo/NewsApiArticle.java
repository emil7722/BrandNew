// NewsApiArticle.java
package com.example.demo;

import java.util.Date;

public class NewsApiArticle {
    private String title;
    private String description;
    private NewsSource source;
    private String url;
    private String urlToImage;
    private Date publishedAt; // make sure it's parsed correctly from ISO 8601 string

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public NewsSource getSource() { return source; }
    public void setSource(NewsSource source) { this.source = source; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getUrlToImage() { return urlToImage; }
    public void setUrlToImage(String urlToImage) { this.urlToImage = urlToImage; }

    public static class NewsSource {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }
}
