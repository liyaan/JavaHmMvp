package com.liyaan.study.fraction;


import com.example.utils.base.BaseMvpFraction;
import com.example.utils.component.decoration.HeaderDecor;
import com.example.utils.component.decoration.Utils;
import com.example.utils.component.decoration.model.HeaderModel;
import com.example.utils.component.decoration.model.ItemModel;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.common.Consts;
import com.liyaan.study.component.CustomPageSlider;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.*;
import com.liyaan.study.main.discover.contract.DiscoverContract;
import com.liyaan.study.main.discover.presenter.DiscoverPresenter;
import com.liyaan.study.provider.PageDiscoverProvider;
import com.liyaan.study.provider.StickyHosStudyAdapter;
import com.liyaan.study.slice.CommonWebViewAbilitySlice;
import com.liyaan.study.utils.PageSliderUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFraction extends BaseMvpFraction<DiscoverPresenter> implements DiscoverContract.View {

    private CustomPageSlider mPageSlider;
    private PageDiscoverProvider mPageDiscoverProvider;
    private ListContainer mListContainer;
    private StickyHosStudyAdapter mStickyHosStudyAdapter;
    private List<ArticleListBean> mPageList = new ArrayList<>();

    private HeaderDecor mHeaderDecor;
    private Text mHeaderText;
    private AbilitySlice mAbilitySlice;
    private List<ItemModel> mDataList = new ArrayList<>();

    private List<ArticleListBean> mListData = new ArrayList<>();

    public DiscoverFraction(AbilitySlice abilitySlice) {
        this.mAbilitySlice = abilitySlice;
    }

    @Override
    public int getLayoutId() {
        return ResourceTable.Layout_fraction_discover;
    }

    @Override
    public void initView(Component component) {
        mPageSlider =
                (CustomPageSlider) component.findComponentById(ResourceTable.Id_page_slider);
        mListContainer = (ListContainer) component.findComponentById(ResourceTable.Id_listContainer);
        mHeaderText = (Text) component.findComponentById(ResourceTable.Id_sticky_text);
        mPageDiscoverProvider = new PageDiscoverProvider(mPageList,this);
        mStickyHosStudyAdapter = new StickyHosStudyAdapter(this, mDataList);
        mListContainer.setItemProvider(mStickyHosStudyAdapter);
        mHeaderDecor = new HeaderDecor(mListContainer, mHeaderText,AttrHelper.vp2px(120,this));
        mListContainer.setItemClickedListener((listContainer, component1, i, l) -> {
            final Intent intent = new Intent();
            intent.setParam(Consts.WEB_URL, mListData.get(i).getLink());
            mAbilitySlice.present(new CommonWebViewAbilitySlice(),intent);
        });
        mPageDiscoverProvider.setOnItemOnClick(i -> {
            final Intent intent = new Intent();
            intent.setParam(Consts.WEB_URL, mPageList.get(i).getLink());
            mAbilitySlice.present(new CommonWebViewAbilitySlice(),intent);
        });
    }

    @Override
    public void initOnActive() {
        mPresenter = new DiscoverPresenter();
        mPresenter.attachView(this);

        mPresenter.getTopArticleList();
        mPresenter.getNaviJson();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(String errMessage) {

    }

    @Override
    public void onSuccess(BaseObjectBean<List<ArticleListBean>> bean) {
        if (bean.getErrorCode()==0){
            if (mPageList.size()>0)mPageList.clear();
            mPageList.addAll(bean.getData());
            Utils.info("MPAGELIST="+mPageList.size());
            Utils.info("MPAGELIST="+mPageList.toString());
            PageSliderUtils.settingPageSlider(mPageSlider,null,mPageDiscoverProvider,mPageList.size());
        }
    }

    @Override
    public void onSuccessNavi(BaseObjectBean<List<NaviJsonBean>> bean) {
        if (bean.getErrorCode()==0){
            if (bean.getData().size()>0){

                mDataList.addAll( getStickyList(bean.getData()));
                mHeaderDecor.setDataList(mDataList);
                mStickyHosStudyAdapter.notifyDataChanged();
            }
        }
    }

    public  List<ItemModel> getStickyList(List<NaviJsonBean> listJsonData) {
        List<ItemModel> items = new ArrayList<>();
        ItemModel itemModel = null;
        for (int i = 0; i < listJsonData.size(); i++) {

            itemModel = new HeaderModel(listJsonData.get(i).getName(), "");
            items.add(itemModel);
            mListData.add(new ArticleListBean());
            if (listJsonData.get(i).getArticles().size()>0) {
                mListData.addAll(listJsonData.get(i).getArticles());
                for (int j = 0; j < listJsonData.get(i).getArticles().size(); j++) {
                    itemModel = new ItemModel(listJsonData.get(i)
                            .getArticles().get(j).getTitle(), "j=" + j, false);
                    items.add(itemModel);
                }
            }

        }
        return items;
    }
}