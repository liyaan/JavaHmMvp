package com.liyaan.study.provider;

import com.example.utils.component.decoration.holder.HeaderHolder;
import com.example.utils.component.decoration.holder.ItemHolder;
import com.example.utils.component.decoration.model.ItemModel;
import com.example.utils.component.decoration.model.Model;
import com.liyaan.study.common.OnItemOnClick;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;
import ohos.biometrics.authentication.IFaceAuthentication;

import java.util.List;


public class StickyHosStudyAdapter extends BaseItemProvider {
    private Context context;
    private List<ItemModel> data;

    /**
     * Sticky滑动列表构造函数
     *
     * @param context 上下文
     * @param dataSource 数据源
     */
    public StickyHosStudyAdapter(Context context, List<ItemModel> dataSource) {
        this.context = context;
        this.data = dataSource;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return data != null ? data.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        Model model = data.get(position);
        Component itemView = null;
        if (model.isHeader()) {
            itemView = LayoutScatter.getInstance(context).parse(model.getResource(position), null, false);
            if (itemView != null) {
                HeaderHolder headerHolder = new HeaderHolder(context, itemView);
                headerHolder.headerText.setText(model.getTitle());
            }
        } else {
            itemView = LayoutScatter.getInstance(context).parse(model.getResource(position), null, false);
            if (itemView != null) {
                ItemHolder itemHolder = new ItemHolder(context, itemView);
                itemHolder.itemText.setText(model.getTitle());
            }
            itemView.setClickedListener(v->{
                if (mOnItemOnClick!=null){
                    mOnItemOnClick.itemOnClick(position);
                }
            });
        }
        /**
         * 动态修改字体颜色，必须在6位字体颜色值前+FF
         * itemHolder.itemText.setTextColor(new Color(0xFF3F51B5));
         */
//        if (position == 0 && itemView != null) {
//            itemView.setMarginTop(48);
//        }

        return itemView;
    }

    private OnItemOnClick mOnItemOnClick;

    public void setOnItemOnClick(OnItemOnClick onItemOnClick) {
        this.mOnItemOnClick = onItemOnClick;
    }
}
