package com.liyaan.study.fraction;

import com.example.utils.base.BaseMvpFraction;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.TreeJsonDataBean;
import com.liyaan.study.entity.resp.TreeJsonDataChildren;
import com.liyaan.study.main.home.contract.HomeContract;
import com.liyaan.study.main.home.presenter.HomePresenter;
import com.liyaan.study.main.study.contract.StudyContract;
import com.liyaan.study.main.study.presenter.StudyPresenter;
import com.liyaan.study.provider.StudyChildProvider;
import com.liyaan.study.provider.StudyProvider;
import com.yan.zrefreshview.ZRefreshView;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.biometrics.authentication.IFaceAuthentication;

import java.util.ArrayList;
import java.util.List;

public class StudyFraction extends BaseMvpFraction<StudyPresenter> implements StudyContract.View {

    private ZRefreshView mZRefreshView;
    private ListContainer mListContainer;
    private ListContainer mListChildContainer;

    private StudyProvider mStudyProvider;
    private StudyChildProvider mStudyChildProvider;
    private List<TreeJsonDataBean> mList = new ArrayList<>();
    private List<TreeJsonDataChildren> mChildList = new ArrayList<>();

    private Text mListChildContainerTv;
    private int mPosition=0;
    @Override
    public int getLayoutId() {
        return ResourceTable.Layout_fraction_study_center;
    }

    @Override
    public void initView(Component component) {
        mPresenter=new StudyPresenter();
        mPresenter.attachView(this);
        mZRefreshView = (ZRefreshView) component.findComponentById(ResourceTable.Id_zrefresh_view);
        mListContainer = (ListContainer) component.findComponentById(ResourceTable.Id_listContainer);
        mListChildContainer = (ListContainer) component.findComponentById(ResourceTable.Id_listChildContainer);
        mListChildContainerTv = (Text) component.findComponentById(ResourceTable.Id_listChildContainerTv);
    }

    @Override
    public void initOnActive() {
        mStudyProvider = new StudyProvider(this,mList);
        mListContainer.setItemProvider(mStudyProvider);
        mStudyChildProvider = new StudyChildProvider(this,mChildList);
        mListChildContainer.setItemProvider(mStudyChildProvider);

        mListContainer.setItemClickedListener((listContainer, component, i, l) -> {
            if (mPosition!=i){
                mPosition = i;
                selectTitleContent(i);

                System.out.println("mPosition = "+mPosition);
            }

            System.out.println("mPosition2222 = "+mPosition);
        });


        mZRefreshView.setOnRefreshListener(() -> {
            mZRefreshView.finishRefreshing();
        });

        mZRefreshView.setLoadMoreListener(() -> {
            mZRefreshView.finishLoadMore();
        });
        mPresenter.getTreeJson();
    }

    private void selectTitleContent(int i) {
        if (mChildList.size()>0)
            mChildList.clear();
        mListChildContainerTv.setText(mList.get(i).getName());
        if (mList.get(i).getChildren().size()>0){
            mChildList.addAll(mList.get(i).getChildren());
        }
        mStudyChildProvider.notifyDataChanged();
        mStudyProvider.setIndex(i);
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
    public void onSuccess(BaseObjectBean<List<TreeJsonDataBean>> bean) {
        if (bean.getErrorCode()==0){
            if (bean.getData().size()>0){
                mList.addAll(bean.getData());
                selectTitleContent(0);
                mStudyProvider.notifyDataChanged();
            }
        }
    }
}