package com.liyaan.study.fraction;


import com.example.utils.base.BaseMvpFraction;
import com.example.utils.component.decoration.HeaderDecor;
import com.example.utils.component.decoration.Utils;
import com.example.utils.component.decoration.model.HeaderModel;
import com.example.utils.component.decoration.model.ItemModel;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.common.Consts;
import com.liyaan.study.common.OnItemOnClick;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.HosIndexJsonBean;
import com.liyaan.study.entity.resp.HosLinkJsonBean;
import com.liyaan.study.entity.resp.HosLinkJsonListBean;
import com.liyaan.study.main.hos.contract.HosStudyContract;
import com.liyaan.study.main.hos.presenter.HosStudyPresenter;

import com.liyaan.study.provider.StickyHosStudyAdapter;
import com.liyaan.study.slice.CommonWebViewAbilitySlice;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;


import java.util.ArrayList;
import java.util.List;

public class SchoolHdFraction extends BaseMvpFraction<HosStudyPresenter> implements HosStudyContract.View {
    private ListContainer mListContainer;
    private StickyHosStudyAdapter mStickyHosStudyAdapter;
    private List<HosIndexJsonBean> mListData = new ArrayList<>();
    private List<HosLinkJsonListBean> mListJsonData = new ArrayList<>();

    private List<ItemModel> mDataList = new ArrayList<>();
    private HeaderDecor mHeaderDecor;
    private Text mHeaderText;
    private AbilitySlice mAbilitySlice;

    public SchoolHdFraction(AbilitySlice abilitySlice) {
        this.mAbilitySlice = abilitySlice;
    }
    @Override
    public int getLayoutId() {
        return ResourceTable.Layout_fraction_hos_sticky_study_center;
    }

    @Override
    public void initView(Component component) {
        mListContainer = (ListContainer) component.findComponentById(ResourceTable.Id_listContainer);
        mHeaderText = (Text) component.findComponentById(ResourceTable.Id_sticky_text);
        mPresenter=new HosStudyPresenter();
        mPresenter.attachView(this);

        mStickyHosStudyAdapter = new StickyHosStudyAdapter(this, mDataList);
        mListContainer.setItemProvider(mStickyHosStudyAdapter);
        mHeaderDecor = new HeaderDecor(mListContainer, mHeaderText);

        mStickyHosStudyAdapter.setOnItemOnClick(new OnItemOnClick() {
            @Override
            public void itemOnClick(int position) {
                final Intent intent = new Intent();
                intent.setParam(Consts.WEB_URL, mListData.get(position).getLink());
                Utils.info("ListBean = "+mListData.get(position).getLink());
                mAbilitySlice.present(new CommonWebViewAbilitySlice(),intent);
            }
        });
    }

    @Override
    public void initOnActive() {
        mPresenter.getHosIndexJson();
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
    public void onSuccess(BaseObjectBean<HosLinkJsonBean> bean) {
        if (bean.getErrorCode()==0){
            if (bean.getData().getLinks().getArticleList().size()>0){
                mListJsonData.add(bean.getData().getLinks());
                mListJsonData.add(bean.getData().getOpen_sources());
                mListJsonData.add(bean.getData().getTools());

                mDataList.addAll(getStickyList(mListJsonData));
                mHeaderDecor.setDataList(mDataList);
                mStickyHosStudyAdapter.notifyDataChanged();
            }
        }
    }

    public  List<ItemModel> getStickyList(List<HosLinkJsonListBean> listJsonData) {
        List<ItemModel> items = new ArrayList<>();
        ItemModel itemModel = null;
        for (int i = 0; i < listJsonData.size(); i++) {

            itemModel = new HeaderModel(listJsonData.get(i).getName(), "");
            items.add(itemModel);
            mListData.add(new HosIndexJsonBean());
            if (listJsonData.get(i).getArticleList().size()>0){
                mListData.addAll(listJsonData.get(i).getArticleList());
                for (int j =0;j<listJsonData.get(i).getArticleList().size();j++){
                    itemModel = new ItemModel(listJsonData.get(i)
                            .getArticleList().get(j).getTitle(),"j="+j, false);
                    items.add(itemModel);
                }
            }
//            if (i == 1 || i == 8 || i == 16 || i == 24 || i == 32 || i == 40 || i == 48 || i == 56) {
//                itemModel = new HeaderModel("Header " + headerNum, "");
//
//            } else {
//                itemModel = new ItemModel("Item " + itemNum, "Item description at " + i, false);
//                itemNum++;
//            }

        }
        return items;
    }
}