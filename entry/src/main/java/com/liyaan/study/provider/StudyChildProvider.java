package com.liyaan.study.provider;

import com.florent37.github.com.expansionpanel.ExpansionHeader;
import com.florent37.github.com.expansionpanel.ExpansionLayout;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.component.CustomBaseItemProvider;
import com.liyaan.study.component.CustomViewHolder;
import com.liyaan.study.entity.resp.TreeJsonDataBean;
import com.liyaan.study.entity.resp.TreeJsonDataChildren;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.List;

public class StudyChildProvider extends CustomBaseItemProvider<TreeJsonDataChildren, StudyChildProvider.ViewHolder> {
//    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();
    private Context mContext;
    private ViewHolder mViewHolder;

    public StudyChildProvider(Context context, List<TreeJsonDataChildren> data) {
        super(ResourceTable.Layout_item_study_list,data);
        this.mContext = context;
//        expansionsCollection.openOnlyOne(true);
    }


    @Override
    public void hindViewComponent(int position, TreeJsonDataChildren treeJsonDataBean, ViewHolder views) {
//        views.mHeaderIndicator.init(ResourceTable.Id_expansionLayout,
//                true, ResourceTable.Id_headerIndicator, 0, 0);
        views.setTextContent(ResourceTable.Id_sampleHeaderTv,treeJsonDataBean.getName());
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
