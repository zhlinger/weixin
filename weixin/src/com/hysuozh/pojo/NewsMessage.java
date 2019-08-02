package com.hysuozh.pojo;

import java.util.List;

/**
 * create by SGOD
 */
public class NewsMessage extends BaseMessage {
    private int ArticleCount;
    private List<News> Articles;

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        this.ArticleCount = articleCount;
    }

    public List<News> getArticles() {
        return Articles;
    }

    public void setArticles(List<News> Articles) {
        this.Articles = Articles;
    }
}
