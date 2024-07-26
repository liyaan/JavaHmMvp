package com.liyaan.study.entity.resp;

import java.util.List;

public class NaviJsonBean {

    private List<ArticleListBean> articles;
    private int cid;
    private String name;

    public List<ArticleListBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleListBean> articles) {
        this.articles = articles;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
