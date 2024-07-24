package com.liyaan.study.provider;

import com.liyaan.study.ResourceTable;
import com.liyaan.study.component.CustomPageSlider;
import com.liyaan.study.entity.resp.ArticleListBean;
import com.liyaan.study.entity.resp.PageSliderBean;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

public class HomeProvider extends BaseItemProvider {
    private Component myComponent;
//    private Component mHeaderComponent;
    private Context mContext;
    private List<ArticleListBean> mArticleListBean;
//    private List<PageSliderBean> mList;
//    private PageProvider mPageProvider;
//    public HomeProvider(Context context, List<PageSliderBean> list) {
//        this.mContext = context;
//        this.mList = list;
//        mPageProvider = new PageProvider(mList, mContext);
//    }
    public HomeProvider(Context context) {
        this.mContext = context;
        mArticleListBean = new ArrayList<>();
    }

    public void addArticleListBean(List<ArticleListBean> articleListBean,int page) {
        if (page==0){
            if (this.mArticleListBean.size()>0) this.mArticleListBean.clear();
        }
        if (articleListBean.size()>0){
            this.mArticleListBean.addAll(articleListBean);
        }
        System.out.println("Cookies:"+articleListBean.size()+"  "+this.mArticleListBean.size());
//        notifyDataChanged();
    }

    @Override
    public void notifyDataChanged() {
        super.notifyDataChanged();
        System.out.println("Cookies:  "+" notifyDataChanged "+this.mArticleListBean.size()+"  "+getCount());
    }

    @Override
    public int getCount() {
        return mArticleListBean == null ? 0 : mArticleListBean.size();
    }

    @Override
    public Object getItem(int position) {
        if (mArticleListBean != null && position >= 0 && position < mArticleListBean.size()) {
            return mArticleListBean.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public Component getComponent(int position, final Component component, ComponentContainer componentContainer) {
//        ViewHolder viewHolder = null;
//        ViewHeaderHolder viewHeaderHolder = null;
        if (component == null) {
            myComponent = LayoutScatter.getInstance(componentContainer.getContext())
                    .parse(ResourceTable.Layout_item_pic, componentContainer, false);
//            mHeaderComponent = LayoutScatter.getInstance(componentContainer.getContext())
//                    .parse(ResourceTable.Layout_item_home_banner, null, false);

//            myComponent.setTag(viewHolder);
//            viewHeaderHolder = new ViewHeaderHolder();
//            viewHeaderHolder.mPageSlider = (CustomPageSlider) mHeaderComponent.findComponentById(ResourceTable.Id_page_slider);
//            viewHeaderHolder.mPageSliderIndicator = (PageSliderIndicator) mHeaderComponent.findComponentById(ResourceTable.Id_page_slider_indicator);
//            mHeaderComponent.setTag(viewHeaderHolder);
        } else {
//            if (mList.size()>0){
//
//            }else{
//                mHeaderComponent.setVisibility(Component.HIDE);
//                viewHolder = (ViewHolder) myComponent.getTag();
//            }
//            if (position==0){
//                mHeaderComponent = component;
//                viewHeaderHolder = (ViewHeaderHolder) mHeaderComponent.getTag();
//            }else{
//                myComponent = component;
//                viewHolder = (ViewHolder) myComponent.getTag();
//            }
            myComponent = component;
//            viewHolder = (ViewHolder) myComponent.getTag();
        }
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.mImageView = (Image) myComponent.findComponentById(ResourceTable.Id_avatar);
        viewHolder.mTitle = (Text) myComponent.findComponentById(ResourceTable.Id_title);
        viewHolder.mChapterName = (Text) myComponent.findComponentById(ResourceTable.Id_chapterName);
        viewHolder.mNiceShareDate = (Text) myComponent.findComponentById(ResourceTable.Id_niceShareDate);
//        if (mList.size()>0){
//
//        }else{
//            if (position == 1) {
//                viewHolder.mImageView.setPixelMap(ResourceTable.Media_a6);
//            } else {
//                viewHolder.mImageView.setPixelMap(ResourceTable.Media_a5);
//            }
//        }
//        if (position==0){
//            viewHeaderHolder.mPageSlider.setProvider(mPageProvider);
//            viewHeaderHolder.mPageSlider.setDataSize(mList.size());
////            viewHeaderHolder.mPageSlider.setPageSwitchTime(2000);
//            System.out.println("Cookies aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa "+position);
//            viewHeaderHolder.mPageSlider.startPlaying();
//            setPageSliderIndicator(viewHeaderHolder.mPageSliderIndicator,viewHeaderHolder.mPageSlider);
//
//        }else{
//            if (position == 1) {
//                viewHolder.mImageView.setPixelMap(ResourceTable.Media_a6);
//                System.out.println("Cookies bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb "+position +"   "+getCount());
//            } else {
//                viewHolder.mImageView.setPixelMap(ResourceTable.Media_a5);
//                System.out.println("Cookies cccccccccccccccccccccccccccccc "+position);
//            }
//        }
//            if (position == 1) {
//                viewHolder.mImageView.setPixelMap(ResourceTable.Media_a6);
//                System.out.println("Cookies bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb "+position +"   "+getCount());
//            } else {
//                viewHolder.mImageView.setPixelMap(ResourceTable.Media_a5);
//                System.out.println("Cookies cccccccccccccccccccccccccccccc "+position);
//            }
        System.out.println("Cookies cccccccccccccccccccccccccccccc "+position);
        viewHolder.mTitle.setText(mArticleListBean.get(position).getTitle());
        viewHolder.mChapterName.setText(mArticleListBean.get(position).getChapterName());
        viewHolder.mNiceShareDate.setText(mArticleListBean.get(position).getNiceShareDate());
//        if (position==0){
//            return mHeaderComponent;
//        }
        return myComponent;

    }

    protected static final class ViewHolder {
        Image mImageView;
        Text mTitle;
        Text mChapterName;
        Text mNiceShareDate;
    }
//    protected static final class ViewHeaderHolder {
//        CustomPageSlider mPageSlider;
//        PageSliderIndicator mPageSliderIndicator;
//    }

//    private void setPageSliderIndicator(PageSliderIndicator indicator,CustomPageSlider pageSlider){
//        //java
////        final ShapeElement normalElement = new ShapeElement();
////        normalElement.setRgbColor(RgbColor.fromArgbInt(0xADD8E6));
////        normalElement.setAlpha(168);
////        normalElement.setShape(ShapeElement.OVAL);
////        normalElement.setBounds(0,0,30,30);
////        ShapeElement selectedElement = new ShapeElement();
////        selectedElement.setRgbColor(RgbColor.fromArgbInt(0x00BFFF));
////        selectedElement.setAlpha(168);
////        selectedElement.setShape(ShapeElement.OVAL);
////        selectedElement.setBounds(0, 0, 40, 40);
//        ShapeElement normalElement = new ShapeElement(mContext, ResourceTable.Graphic_unselected_page_bg_element);
//        ShapeElement selectedElement = new ShapeElement(mContext, ResourceTable.Graphic_selected_page_bg_element);
//        pageSlider.setPageSliderIndicator(indicator,normalElement,selectedElement);
//
////        indicator.setItemElement(normalElement, selectedElement);
////        indicator.setItemOffset(30);
////        indicator.setPageSlider(pageSlider);
//
//    }
}
