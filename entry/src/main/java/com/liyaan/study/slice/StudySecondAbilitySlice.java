package com.liyaan.study.slice;

import com.example.utils.base.BaseMvpAbilitySlice;
import com.example.utils.component.decoration.Utils;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.common.Consts;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.ArticleListBean;
import com.liyaan.study.login.presenter.LoginPresenter;
import com.liyaan.study.main.study.second.contract.StudySecondContract;
import com.liyaan.study.main.study.second.presenter.StudySecondPresenter;
import com.liyaan.study.provider.StudySecondProvider;
import com.yan.zrefreshview.ZRefreshView;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;

import java.util.ArrayList;
import java.util.List;

public class StudySecondAbilitySlice extends BaseMvpAbilitySlice<StudySecondPresenter> implements StudySecondContract.View{
    private ZRefreshView mZRefreshView;
    private ListContainer mListContainer;

    private int page = 0;
    private int cid;

    private StudySecondProvider mStudySecondProvider;

    private List<ArticleListBean> mListData = new ArrayList<>();
    @Override
    public void onSuccess(BaseObjectBean<ArticleDataBean> bean) {
        if (bean.getErrorCode()==0){
            if (bean.getData().getDatas().size()>0){
                if (page==0){
                    if (mListData.size()>0) mListData.clear();
                }
                mListData.addAll(bean.getData().getDatas());
                mStudySecondProvider.notifyDataChanged();

            }
        }
        if (page==0){
            mZRefreshView.finishRefreshing();
        }else{
            mZRefreshView.finishLoadMore();
        }
    }

    @Override
    public int getLayoutId() {
        return ResourceTable.Layout_ability_study_second;
    }

    @Override
    public void onStartIntent(Intent intent) {
        cid = (int) intent.getParams().getParam("cid");
    }

    @Override
    public void initView() {
        mPresenter=new StudySecondPresenter();
        mPresenter.attachView(this);

        mZRefreshView = (ZRefreshView) findComponentById(ResourceTable.Id_zrefresh_view);
        mListContainer = (ListContainer) findComponentById(ResourceTable.Id_listContainer);

        mListContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int position, long l) {
                final Intent intent = new Intent();
                intent.setParam(Consts.WEB_URL, mListData.get(position).getLink());
                present(new CommonWebViewAbilitySlice(),intent);
            }
        });
    }

    @Override
    public void initOnActive() {
        mPresenter.getTreeJsonArticleList(page,cid);
        mStudySecondProvider = new StudySecondProvider(mListData);
        mListContainer.setItemProvider(mStudySecondProvider);

        mZRefreshView.setOnRefreshListener(new ZRefreshView.RefreshListener() {
            @Override
            public void onPullRefreshing() {
                page = 0;
                mPresenter.getTreeJsonArticleList(page,cid);

            }


        });

        mZRefreshView.setLoadMoreListener(() -> {
            page+=1;
            mPresenter.getTreeJsonArticleList(page,cid);
        });

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
}