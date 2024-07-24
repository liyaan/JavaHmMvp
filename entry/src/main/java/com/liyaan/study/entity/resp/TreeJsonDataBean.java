package com.liyaan.study.entity.resp;

import java.util.List;

/**
 * Description： 首页文章列表
 */
public class TreeJsonDataBean {

    private String name;
    private int type;
    private int id;
    private int courseId;
    private int order;
    private int visible;

    private List<TreeJsonDataChildren> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public List<TreeJsonDataChildren> getChildren() {
        return children;
    }

    public void setChildren(List<TreeJsonDataChildren> children) {
        this.children = children;
    }
}
