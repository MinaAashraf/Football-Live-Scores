package com.football.matches.livescores.pojo;

import java.util.List;

public class ArticlesWithqTitle {
   List <Article> article ;
    String qtitle;

    public ArticlesWithqTitle(List<Article> article, String qtitle) {
        this.article = article;
        this.qtitle = qtitle;
    }

    public List<Article> getArticle() {
        return article;
    }

    public String getQtitle() {
        return qtitle;
    }
}
