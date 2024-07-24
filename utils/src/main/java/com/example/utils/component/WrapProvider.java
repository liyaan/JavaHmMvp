package com.example.utils.component;

import ohos.agp.components.*;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

import static ohos.agp.utils.LayoutAlignment.TOP;

public class WrapProvider extends BaseItemProvider {
    private static final int NORMAL_TYPE = 0;
    private static final int HEADER_TYPE = 1;
    private static final int FOOTER_TYPE = 2;

    private int height;

    private final Context mContext;

    private final ListContainer mListContainer;

    private ListContainer.ItemClickedListener mOnItemClickListener;
    private ListContainer.ItemLongClickedListener mOnItemLongClickListener;

    private final BaseItemProvider provider;
    private final List<Component> mHeaderViewInfos;
    private final List<Component> mFooterViewInfos;

    private int mNumColumns = 1;

    public WrapProvider(Context context, ListContainer listContainer,
                        List<Component> headerViewInfos, List<Component> footerViewInfos,
                        BaseItemProvider provider) {
        this.mContext = context;
        this.mListContainer = listContainer;
        this.provider = provider;

        if (headerViewInfos == null) {
            mHeaderViewInfos = new ArrayList<>();
        } else {
            mHeaderViewInfos = headerViewInfos;
        }

        if (footerViewInfos == null) {
            mFooterViewInfos = new ArrayList<>();
        } else {
            mFooterViewInfos = footerViewInfos;
        }
    }

    public void setNumColumns(int numColumns) {
        if (mNumColumns != numColumns) {
            mNumColumns = numColumns;
            notifyDataChanged();
        }
    }

    public void setOnItemClickListener(ListContainer.ItemClickedListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(ListContainer.ItemLongClickedListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public int getItemComponentType(int position) {
        int numHeaders = getHeadersCount();
        // Header
        if (position < numHeaders) {
            return HEADER_TYPE;
        }
        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount;
        if (provider != null) {
            adapterCount = provider.getCount();
            if (adjPosition < adapterCount) {
                return NORMAL_TYPE;
            }
        }
        // Footer
        return FOOTER_TYPE;
    }

    @Override
    public int getCount() {
        return getHeadersCount() + getFootersCount() + getAdapterAndPlaceHolderCount();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i - getHeadersCount();
    }

    @Override
    public Component getComponent(int position, Component convertComponent, ComponentContainer componentContainer) {
        if (position < getHeadersCount()) {
            return mHeaderViewInfos.get(position);
        } else if (position >= getHeadersCount() + getAdapterAndPlaceHolderCount()) {
            convertComponent = mFooterViewInfos.get(position - mHeaderViewInfos.size() - getAdapterAndPlaceHolderCount());
            return convertComponent;
        }

        convertComponent = new DirectionalLayout(mContext);
        ((DirectionalLayout) convertComponent).setOrientation(Component.HORIZONTAL);
        ComponentContainer.LayoutConfig layoutConfig = convertComponent.getLayoutConfig();
        layoutConfig.width = DependentLayout.LayoutConfig.MATCH_PARENT;
        convertComponent.setLayoutConfig(layoutConfig);

        // 调整下标
        position = position - getHeadersCount();
        for (int i = 0; i < mNumColumns; i++) {
            if (position * mNumColumns + i < provider.getCount()) {
                DirectionalLayout dlItemParent = new DirectionalLayout(mContext);
                dlItemParent.setLayoutConfig(new DirectionalLayout.LayoutConfig(0, DirectionalLayout.LayoutConfig.MATCH_CONTENT, TOP, 1));
                height = dlItemParent.getHeight();
                Component childConvertComponent = provider.getComponent(position * mNumColumns + i, null, componentContainer);
                int finalPosition = position * mNumColumns + i;
                childConvertComponent.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClicked(mListContainer, component, finalPosition, component.getId());
                        }
                    }
                });
                childConvertComponent.setLongClickedListener(new Component.LongClickedListener() {
                    @Override
                    public void onLongClicked(Component component) {
                        if (mOnItemLongClickListener != null) {
                            mOnItemLongClickListener.onItemLongClicked(mListContainer, component, finalPosition, component.getId());
                        }
                    }
                });
                dlItemParent.addComponent(childConvertComponent);
                ((ComponentContainer) convertComponent).addComponent(dlItemParent);
            } else {
                Component childConvertComponent = new Component(mContext);
                DirectionalLayout.LayoutConfig layoutConfig1 = new DirectionalLayout.LayoutConfig(0, height);
                layoutConfig1.weight = 1;
                childConvertComponent.setLayoutConfig(layoutConfig1);
                ((ComponentContainer) convertComponent).addComponent(childConvertComponent);
            }
        }
        return convertComponent;
    }

    private int getAdapterAndPlaceHolderCount() {
        if (provider != null) {
            return (provider.getCount() % mNumColumns == 0 ? provider.getCount() / mNumColumns : provider.getCount() / mNumColumns + 1);
        }
        return 0;
    }

    public int getHeadersCount() {
        return mHeaderViewInfos.size();
    }

    public int getFootersCount() {
        return mFooterViewInfos.size();
    }

}
