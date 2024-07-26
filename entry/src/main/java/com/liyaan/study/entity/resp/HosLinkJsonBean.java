package com.liyaan.study.entity.resp;

import java.util.List;

public class HosLinkJsonBean {

   private HosLinkJsonListBean links;
   private HosLinkJsonListBean open_sources;
   private HosLinkJsonListBean tools;

    public HosLinkJsonListBean getLinks() {
        return links;
    }

    public void setLinks(HosLinkJsonListBean links) {
        this.links = links;
    }

    public HosLinkJsonListBean getOpen_sources() {
        return open_sources;
    }

    public void setOpen_sources(HosLinkJsonListBean open_sources) {
        this.open_sources = open_sources;
    }

    public HosLinkJsonListBean getTools() {
        return tools;
    }

    public void setTools(HosLinkJsonListBean tools) {
        this.tools = tools;
    }
}
