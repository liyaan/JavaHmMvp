package com.liyaan.study.entity.resp;

import java.util.List;

public class HosLinkJsonListBean {

   private List<HosIndexJsonBean> articleList;

   private String name;
   private int courseId;
   private int id;
   private int parentChapterId;

    public List<HosIndexJsonBean> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<HosIndexJsonBean> articleList) {
        this.articleList = articleList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentChapterId() {
        return parentChapterId;
    }

    public void setParentChapterId(int parentChapterId) {
        this.parentChapterId = parentChapterId;
    }
}
