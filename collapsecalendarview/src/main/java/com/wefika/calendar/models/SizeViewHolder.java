package com.wefika.calendar.models;


import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;

/**
 * Created by Blaz Solar on 17/04/14.
 */
public class SizeViewHolder extends AbstractViewHolder {

    private int mMinHeight;
    private int mMaxHeight;

    public SizeViewHolder(int minHeight, int maxHeight) {
        mMinHeight = minHeight;
        mMaxHeight = maxHeight;
    }

    public int getHeight() {
        return mMaxHeight - mMinHeight;
    }

    public void setMinHeight(int minHeight) {
        mMinHeight = minHeight;
    }

    public int getMaxHeight() {
        return mMaxHeight;
    }

    public int getMinHeight() {
        return mMinHeight;
    }

    public void setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
    }

    @Override
    public void onFinish(boolean done) {
        if (done) {
            onShown();
        } else {
            onHidden();
        }
    }

    public void onShown() {
        getView().getLayoutConfig().height = ComponentContainer.LayoutConfig.MATCH_CONTENT;
        getView().setVisibility(Component.VISIBLE);
    }

    public void onHidden() {
        getView().getLayoutConfig().height = ComponentContainer.LayoutConfig.MATCH_CONTENT;
        getView().setVisibility(Component.HIDE);
    }

    @Override
    protected void onAnimate(float time) {
        Component view = getView();
        view.setVisibility(Component.VISIBLE);
        view.getLayoutConfig().height = (int) (getMinHeight() + getHeight() * time);
        view.postLayout();
    }
}
