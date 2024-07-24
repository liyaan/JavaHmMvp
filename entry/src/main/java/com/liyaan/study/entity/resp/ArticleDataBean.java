package com.liyaan.study.entity.resp;

import java.util.List;

/**
 * Description： 首页文章列表
 */
public class ArticleDataBean {

    private int total;
    private int curPage;
    private int offset;
    private int pageCount;
    private int size;
    private List<ArticleListBean> datas;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<ArticleListBean> getDatas() {
        return datas;
    }

    public void setDatas(List<ArticleListBean> datas) {
        this.datas = datas;
    }
}
