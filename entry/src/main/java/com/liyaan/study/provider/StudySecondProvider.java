package com.liyaan.study.provider;


import com.liyaan.study.ResourceTable;
import com.liyaan.study.component.CustomBaseItemProvider;
import com.liyaan.study.component.CustomViewHolder;
import com.liyaan.study.entity.resp.ArticleListBean;
import com.liyaan.study.entity.resp.HosIndexJsonBean;
import ohos.agp.components.Component;
import java.util.List;

public class StudySecondProvider extends CustomBaseItemProvider<ArticleListBean, StudySecondProvider.ViewHolder> {

    public StudySecondProvider(List<ArticleListBean> data) {
        super(ResourceTable.Layout_item_hos_study_list,data);

    }


    @Override
    public void hindViewComponent(int position, ArticleListBean treeJsonDataBean, ViewHolder views) {

        views.setTextContent(ResourceTable.Id_item_title,treeJsonDataBean.getTitle());
//        views.setTextContent(ResourceTable.Id_item_chapterName,treeJsonDataBean.getChapterName());
//        views.setTextContent(ResourceTable.Id_item_author,treeJsonDataBean.getAuthor());
        views.setTextContent(ResourceTable.Id_item_niceShareDate,treeJsonDataBean.getNiceShareDate());

    }

    protected static final class ViewHolder extends CustomViewHolder {

        public ViewHolder(Component component) {
            super(component);

        }
    }

}
