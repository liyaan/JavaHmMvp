package com.liyaan.study.fraction;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.utils.base.BaseMvpFraction;
import com.example.utils.component.HeaderAndFooter;
import com.example.utils.dialog.ToastUtils;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.common.RefreshHelper;
import com.liyaan.study.component.CustomPageSlider;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.ArticleListBean;
import com.liyaan.study.entity.resp.PageSliderBean;
import com.liyaan.study.main.home.contract.HomeContract;
import com.liyaan.study.main.home.presenter.HomePresenter;
import com.liyaan.study.provider.HomeProvider;
import com.liyaan.study.provider.PageProvider;
import com.liyaan.study.utils.PageSliderUtils;
import com.ryan.ohos.extension.nested.component.NestedListContainer;
import com.yan.zrefreshview.ZRefreshView;
import ohos.agp.components.*;
import ohos.agp.utils.TextAlignment;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class HomeFraction extends BaseMvpFraction<HomePresenter> implements HomeContract.View {
    private HeaderAndFooter mListContainer;
    private ZRefreshView mHomeRefresh;
    private List<PageSliderBean> mList;
    private HomeProvider mHomeProvider;

    private Component mHeaderComponent;

    private CustomPageSlider mPageSlider;
    private PageSliderIndicator mPageSliderIndicator;
    private PageProvider mPageProvider;

    private List<ArticleListBean> mArticleListBean;
    private int page = 0;

    @Override
    public int getLayoutId() {
        return ResourceTable.Layout_fraction_home;
    }

    @Override
    public void initView(Component component) {
        mListContainer = (HeaderAndFooter) component.findComponentById(ResourceTable.Id_listContainer);
        mHomeRefresh = (ZRefreshView) component.findComponentById(ResourceTable.Id_zrefresh_view);
        mPresenter=new HomePresenter();
        mPresenter.attachView(this);
        mHeaderComponent = LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_item_home_banner, null, false);
        mPageSlider =
                (CustomPageSlider) mHeaderComponent.findComponentById(ResourceTable.Id_page_slider);
        mPageSliderIndicator =
                (PageSliderIndicator) mHeaderComponent.findComponentById(ResourceTable.Id_page_slider_indicator);
        mListContainer.addHeaderView(mHeaderComponent);

        mList = new ArrayList<>();
        mArticleListBean = new ArrayList<>();

        mPageProvider = new PageProvider(mList, this);
//        mListContainer.setNumColumns(1);
        mHomeProvider = new HomeProvider(this);
        mListContainer.setItemProvider(mHomeProvider);

    }

    @Override
    public void initOnActive() {

        mHomeRefresh.setOnRefreshListener(new ZRefreshView.RefreshListener() {
            @Override
            public void onPullRefreshing() {
                page = 0;
                mPresenter.getArticleList(page);
//                                mHomeRefresh.finishRefreshing();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(2000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        getUITaskDispatcher().asyncDispatch(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });
//                    }
//                }).start();
            }


        });

        mHomeRefresh.setLoadMoreListener(() -> {
            page+=1;
            mPresenter.getArticleList(page);
//                            mHomeProvider.notifyDataChanged();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    getUITaskDispatcher().asyncDispatch(new Runnable() {
//                        @Override
//                        public void run() {
//
//
//                        }
//                    });
//                }
//            }).start();
        });

        mPresenter.banner();
        mPresenter.getArticleList(page);
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
    public void onSuccess(BaseObjectBean<List<PageSliderBean>> bean) {
        if (bean.getErrorCode()==0){
            if (mList.size()>0)mList.clear();
            mList.addAll(bean.getData());
            if (mList.size()>0){
                PageSliderUtils.settingPageSlider(mPageSlider,mPageSliderIndicator,mPageProvider,mList.size());
//                mPageProvider.notifyDataChanged();
            }
        }
    }

    @Override
    public void onSuccessArticleList(BaseObjectBean<ArticleDataBean> bean) {
        if (bean.getErrorCode()==0){
            if (bean.getData().getDatas().size()>0){
//                if (page==0){
//                    mHomeProvider.addArticleListPageBean(bean.getData().getDatas(),page);
//
//                }else{
//
//                }
                mHomeProvider.addArticleListBean(bean.getData().getDatas(),page);
                mListContainer.getOriginalAdapter().notifyDataChanged();
                if (page==0){
                    mHomeRefresh.finishRefreshing();
                }else{
                    mHomeRefresh.finishLoadMore();
                }


//                mArticleListBean.addAll(bean.getData().getDatas());
//                System.out.println("Cookies:"+mArticleListBean.size());
//                mHomeProvider.notifyDataInvalidated();
            }
        }
    }
}