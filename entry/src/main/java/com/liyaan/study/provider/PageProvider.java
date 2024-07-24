package com.liyaan.study.provider;


import com.bumptech.glide.Glide;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.entity.resp.PageSliderBean;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

public class PageProvider extends PageSliderProvider {
    // 数据源，每个页面对应list中的一项
    private List<PageSliderBean> mList;
    private Context mContext;

    public PageProvider(List<PageSliderBean> list, Context mContext) {
        this.mList = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        final PageSliderBean item = mList.get(i);
//        final Text label = new Text(mContext);
//        label.setTextAlignment(TextAlignment.CENTER);
//        label.setLayoutConfig(
//                new StackLayout.LayoutConfig(
//                        ComponentContainer.LayoutConfig.MATCH_PARENT,
//                        ComponentContainer.LayoutConfig.MATCH_PARENT
//                ));
//        label.setText(item.getTitle());
//        label.setTextColor(Color.BLACK);
//        label.setTextSize(50);
//        label.setMarginsLeftAndRight(24, 24);
//        label.setMarginsTopAndBottom(24, 24);
//        ShapeElement element = new ShapeElement(mContext, ResourceTable.Graphic_background_page);
//        label.setBackground(element);
//        componentContainer.addComponent(label);
        final Component component = LayoutScatter.getInstance(componentContainer.getContext())
                .parse(ResourceTable.Layout_item_page_slider, componentContainer, false);
        final Image image = (Image) component.findComponentById(ResourceTable.Id_pageSliderImage);
        final Text text = (Text) component.findComponentById(ResourceTable.Id_pageSliderText);
        Glide.with(mContext)
                .load(item.getImagePath()).into(image);
        text.setText(item.getTitle());
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
}
