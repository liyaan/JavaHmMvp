package com.example.utils.component.sticky;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ScrollView;
import ohos.app.Context;

/**
 * 自定义ScrollView
 */
public class StickyScrollView extends ScrollView implements Component.BindStateChangedListener,
        Component.ScrolledListener, Component.LayoutRefreshedListener {
    private static int offset = 50;
    private StickChangeLisener stickChangeLisener;
    private ComponentContainer headView;
    private ComponentContainer footView;
    private Context mContext;
    private int screenHeight;

    public StickyScrollView(Context context) {
        this(context, null);
    }

    public StickyScrollView(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    public StickyScrollView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        mContext = context;
        setBindStateChangedListener(this);
        setScrolledListener(this);
        setLayoutRefreshedListener(this);
    }

    @Override
    public void onComponentBoundToWindow(Component component) {
        initView();
    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {
    }

    private void initView() {
        screenHeight = ScreenUtils.getAttributesHeight(mContext);
        footView = getFootView();
        headView = getHeadView();
        footView.setBindStateChangedListener(new BindStateChangedListener() {
            @Override
            public void onComponentBoundToWindow(Component component) {
                if (screenHeight - offset > (double) component.getContentPositionY() + component.getHeight()) {
                    footView.setVisibility(HIDE);
                    stickChangeLisener.hideFoot();
                } else {
                    footView.setVisibility(VISIBLE);
                    stickChangeLisener.stickFoot();
                }
            }

            @Override
            public void onComponentUnboundFromWindow(Component component) {
            }
        });
    }

    /**
     * 获取头部控件
     *
     * @return ComponentContainer 头部控件
     */
    public ComponentContainer getHeadView() {
        return headView;
    }

    /**
     * 设置头部控件
     *
     * @param headView 头部控件
     */
    public void setHeadView(ComponentContainer headView) {
        this.headView = headView;
    }

    /**
     * 获取尾部控件
     *
     * @return ComponentContainer 尾部控件
     */
    public ComponentContainer getFootView() {
        return footView;
    }

    /**
     * 设置尾部控件
     *
     * @param footView 尾部控件
     */
    public void setFootView(ComponentContainer footView) {
        this.footView = footView;
    }

    @Override
    public void onContentScrolled(Component component, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > headView.getContentPositionY()) {
            stickChangeLisener.stcikHead();
        } else {
            stickChangeLisener.hideHead();
        }
        if (scrollY - offset > ((double) (footView.getContentPositionY()) - screenHeight + footView.getHeight() * 2)) {
            footView.setVisibility(Component.VISIBLE);
            stickChangeLisener.hideFoot();
        } else {
            footView.setVisibility(Component.INVISIBLE);
            stickChangeLisener.stickFoot();
        }
    }

    /**
     * 设置粘连监听状态
     *
     * @param stickChangeLisener 回调接口
     */
    public void setStickChangeLisener(StickChangeLisener stickChangeLisener) {
        this.stickChangeLisener = stickChangeLisener;
    }

    @Override
    public void onRefreshed(Component component) {
        double realPostion = footView.getContentPositionY();
        int postionY = getScrollValue(1);
        if (postionY - offset > (realPostion - screenHeight + footView.getHeight() * 2)) {
            footView.setVisibility(Component.VISIBLE);
            stickChangeLisener.hideFoot();
        } else {
            stickChangeLisener.stickFoot();
        }
        if (postionY > headView.getContentPositionY()) {
            stickChangeLisener.stcikHead();
        } else {
            stickChangeLisener.hideHead();
        }
    }
}
