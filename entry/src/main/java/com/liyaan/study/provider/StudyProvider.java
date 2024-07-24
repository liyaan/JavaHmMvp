package com.liyaan.study.provider;

import com.florent37.github.com.expansionpanel.ExpansionHeader;
import com.florent37.github.com.expansionpanel.ExpansionLayout;
import com.florent37.github.com.expansionpanel.viewgroup.ExpansionLayoutCollection;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.component.CustomBaseItemProvider;
import com.liyaan.study.component.CustomViewHolder;
import com.liyaan.study.entity.resp.TreeJsonDataBean;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

public class StudyProvider extends CustomBaseItemProvider<TreeJsonDataBean, StudyProvider.ViewHolder> {
//    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();
    private Context mContext;
    private ViewHolder mViewHolder;

    private int index=0;

    public StudyProvider(Context context,List<TreeJsonDataBean> data) {
        super(ResourceTable.Layout_item_study_list,data);
        this.mContext = context;
//        expansionsCollection.openOnlyOne(true);
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataChanged();
    }

    @Override
    public void hindViewComponent(int position, TreeJsonDataBean treeJsonDataBean, ViewHolder views) {
//        views.mHeaderIndicator.init(ResourceTable.Id_expansionLayout,
//                true, ResourceTable.Id_headerIndicator, 0, 0);
        views.setTextContent(ResourceTable.Id_sampleHeaderTv,treeJsonDataBean.getName());
        if(index==position) {
            views.setBgColor(ResourceTable.Id_sampleHeaderTv, new RgbColor(247,247,247));
        }else {
            views.setBgColor(ResourceTable.Id_sampleHeaderTv,new RgbColor(255, 255, 255));
        }
//        if (treeJsonDataBean.getChildren()!=null && treeJsonDataBean.getChildren().size()>0){
//            views.setTextContent(ResourceTable.Id_expansionLayoutTv,treeJsonDataBean.getChildren().get(0).getName());
//        }

//        views.mExpansionLayout.collapse(false);
//        views.mExpansionLayout.init(300);
//        expansionsCollection.add(views.mExpansionLayout);
    }

    protected static final class ViewHolder extends CustomViewHolder {
        ExpansionLayout mExpansionLayout;
        ExpansionHeader mHeaderIndicator;
        Text sampleHeaderTv;
        Text expansionLayoutTv;

        public ViewHolder(Component component) {
            super(component);
            sampleHeaderTv = (Text) component.findComponentById(ResourceTable.Id_sampleHeaderTv);
//            expansionLayoutTv = (Text) component.findComponentById(ResourceTable.Id_expansionLayoutTv);
//            mExpansionLayout = (ExpansionLayout) component.findComponentById(ResourceTable.Id_expansionLayout);
//            mHeaderIndicator = (ExpansionHeader) component.findComponentById(ResourceTable.Id_sampleHeader);
        }
    }

}
