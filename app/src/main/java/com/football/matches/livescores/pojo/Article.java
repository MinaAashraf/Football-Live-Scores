package com.football.matches.livescores.pojo;

public class Article extends GenralTeamNews {
    String title, description, url, urlToImage, publishedAt, content , author;
    Source source;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public Source getSource() {
        return source;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
