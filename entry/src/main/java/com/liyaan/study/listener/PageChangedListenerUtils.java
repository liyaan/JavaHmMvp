package com.liyaan.study.listener;

import ohos.agp.components.PageSlider;
import ohos.agp.components.PageSliderIndicator;

public class PageChangedListenerUtils implements PageSlider.PageChangedListener{
    PageSliderIndicator mIndicator;
    PageSlider mPageSlider;

    public PageChangedListenerUtils(PageSliderIndicator indicator, PageSlider pageSlider) {
        this.mIndicator = indicator;
        this.mPageSlider = pageSlider;
    }

    @Override
    public void onPageSliding(int i, float v, int i1) {

    }

    @Override
    public void onPageSlideStateChanged(int i) {

    }

    @Override
    public void onPageChosen(int i) {

    }
}