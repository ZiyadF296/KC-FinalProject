package com.example.astrospace;

public class News {
    public final int id;
    public final String title;
    public final String url;
    public final String imageUrl;
    public final String newsSite;
    public final String summary;
    public final String publishedAt;
    public final String updatedAt;
    public final boolean featured;

    public News(int id, String title, String url, String imageUrl,
                String newsSite, String summary, String publishedAt,
                String updatedAt, boolean featured) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.imageUrl = imageUrl;
        this.newsSite = newsSite;
        this.summary = summary;
        this.publishedAt = publishedAt;
        this.updatedAt = updatedAt;
        this.featured = featured;
    }
}