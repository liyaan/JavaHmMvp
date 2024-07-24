package com.liyaan.study.holder;


import com.example.utils.component.more.ViewHolder;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.entity.resp.ArticleListBean;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.app.Context;

public class HomeHolder extends ViewHolder<ArticleListBean> {

    private Image mImageView;
    private Text mTitle;
    private Text mChapterName;
    private Text mNiceShareDate;
    public HomeHolder(Context context) {
        super(context);
    }

    @Override
    public void convert(ArticleListBean articleListBean, int position) {
        mTitle.setText(articleListBean.getTitle());
        mChapterName.setText(articleListBean.getChapterName());
        mNiceShareDate.setText(articleListBean.getNiceShareDate());
    }

    @Override
    protected int getLayoutId(ArticleListBean articleListBean, int position) {
        return ResourceTable.Layout_item_pic;
    }

    @Override
    protected void findComponent(ArticleListBean articleListBean, int position) {
        mImageView = getComponent(ResourceTable.Id_avatar);
        mTitle = getComponent(ResourceTable.Id_title);
        mChapterName = getComponent(ResourceTable.Id_chapterName);
        mNiceShareDate = getComponent(ResourceTable.Id_niceShareDate);
    }
}
