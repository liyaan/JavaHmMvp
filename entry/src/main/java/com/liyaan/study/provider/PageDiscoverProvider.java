package com.liyaan.study.provider;


import com.bumptech.glide.Glide;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.common.OnItemOnClick;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.ArticleListBean;
import com.liyaan.study.entity.resp.PageSliderBean;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

public class PageDiscoverProvider extends PageSliderProvider {
    // 数据源，每个页面对应list中的一项
    private List<ArticleListBean> mList;
    private Context mContext;

    public PageDiscoverProvider(List<ArticleListBean> list, Context mContext) {
        this.mList = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        final ArticleListBean item = mList.get(i);
        final Component component = LayoutScatter.getInstance(componentContainer.getContext())
                .parse(ResourceTable.Layout_item_discover_header, componentContainer, false);
        final Text pageTitle = (Text) component.findComponentById(ResourceTable.Id_discover_page_title);
        final Text niceDate = (Text) component.findComponentById(ResourceTable.Id_discover_niceDate);
        pageTitle.setText(item.getTitle());
        niceDate.setText(item.getNiceShareDate());
        component.setClickedListener(component1 -> {
            if (mOnItemOnClick!=null){
                mOnItemOnClick.itemOnClick(i);
            }
        });
        componentContainer.addComponent(component);
        return component;
    }

    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        componentContainer.removeComponent((Component) o);
    }

    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        return false;
    }


    private OnItemOnClick mOnItemOnClick;

    public void setOnItemOnClick(OnItemOnClick onItemOnClick) {
        this.mOnItemOnClick = onItemOnClick;
    }
}
