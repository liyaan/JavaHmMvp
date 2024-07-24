package com.example.utils.component;

import ohos.agp.components.*;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

public class HeaderAndFooter extends ListContainer {
    private Context context;

    private final List<Component> mHeaderViewInfos = new ArrayList<>();
    private final List<Component> mFooterViewInfos = new ArrayList<>();

    private BaseItemProvider mAdapter;
    private BaseItemProvider mOriginalAdapter;

    private int mColumnNum = 1;

    public HeaderAndFooter(Context context) {
        super(context);
        initWithContext(context);
    }

    public HeaderAndFooter(Context context, AttrSet attrSet) {
        super(context, attrSet);
        initWithContext(context);
    }

    public HeaderAndFooter(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        this.context = context;
    }

    /**
     * 设置列数
     *
     * @param columnNum 列数
     */
    public void setNumColumns(int columnNum) {
        this.mColumnNum = columnNum;
        if (mAdapter != null) {
            if (mAdapter instanceof WrapProvider) {
                ((WrapProvider) mAdapter).setNumColumns(columnNum);
            }
            mAdapter.notifyDataChanged();
        }
    }

    /**
     * 添加头部视图
     *
     * @param v 头部视图
     */
    public void addHeaderView(Component v) {
        mHeaderViewInfos.add(v);

        if (mAdapter != null) {
            if (!(mAdapter instanceof WrapProvider)) {
                mAdapter = new WrapProvider(context, this, mHeaderViewInfos, mFooterViewInfos, mAdapter);
            }
        }
    }

    /**
     * 添加头部视图
     *
     * @param v            头部视图
     * @param data         数据
     * @param isSelectable 是否可选择
     */
    public void addHeaderView(Component v, Object data, boolean isSelectable) {
        addHeaderView(v);
    }

    /**
     * 添加底部视图
     *
     * @param v 底部视图
     */
    public void addFooterView(Component v) {
        mFooterViewInfos.add(v);

        if (mAdapter != null) {
            if (!(mAdapter instanceof WrapProvider)) {
                mAdapter = new WrapProvider(context, this, mHeaderViewInfos, mFooterViewInfos, mAdapter);
            }
        }
    }

    /**
     * 添加底部视图
     *
     * @param v            底部视图
     * @param data         数据
     * @param isSelectable 是否可选择
     */
    public void addFooterView(Component v, Object data, boolean isSelectable) {
        addFooterView(v);
    }

    /**
     * 移除头部视图
     *
     * @param v 头部视图
     */
    public void removeHeaderView(Component v) {
        mHeaderViewInfos.remove(v);
        if (mAdapter != null) {
            mAdapter.notifyDataChanged();
        }
    }

    /**
     * 移除底部视图
     *
     * @param v 底部视图
     */
    public void removeFooterView(Component v) {
        mFooterViewInfos.remove(v);
        if (mAdapter != null) {
            mAdapter.notifyDataChanged();
        }
    }

    /**
     * 获取头部视图数量
     *
     * @return 头部视图数量
     */
    public int getHeaderViewCount() {
        return mHeaderViewInfos.size();
    }

    /**
     * 获取底部视图数量
     *
     * @return 底部视图数量
     */
    public int getFooterViewCount() {
        return mFooterViewInfos.size();
    }

    /**
     * Return original adapter for convenience.
     *
     * @return mOriginalAdapter
     */
    public BaseItemProvider getOriginalAdapter() {
        return mOriginalAdapter;
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        if(layoutManager instanceof TableLayoutManager){
            mColumnNum = ((TableLayoutManager)layoutManager).getColumnCount();
            if(mColumnNum <= 0){
                mColumnNum = 1;
            }
            setNumColumns(mColumnNum);
            return;
        }
        super.setLayoutManager(layoutManager);
    }

    @Override
    public void setItemProvider(BaseItemProvider itemProvider) {
        mOriginalAdapter = itemProvider;
        if (mHeaderViewInfos.size() > 0 || mFooterViewInfos.size() > 0) {
            mAdapter = new WrapProvider(context, this, mHeaderViewInfos, mFooterViewInfos, itemProvider);
        } else {
            mAdapter = itemProvider;
        }
        if (mAdapter instanceof WrapProvider) {
            ((WrapProvider) mAdapter).setNumColumns(mColumnNum);
        }
        if (mAdapter!=null) mOriginalAdapter = mAdapter;
        super.setItemProvider(mAdapter);
    }

    @Override
    public void setItemClickedListener(ItemClickedListener listener) {
        if (mAdapter instanceof WrapProvider) {
            ((WrapProvider) mAdapter).setOnItemClickListener(listener);
        }
    }

    @Override
    public void setItemLongClickedListener(ItemLongClickedListener listener) {
        if (mAdapter instanceof WrapProvider) {
            ((WrapProvider) mAdapter).setOnItemLongClickListener(listener);
        }
    }
}
