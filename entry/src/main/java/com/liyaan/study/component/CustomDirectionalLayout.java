package com.liyaan.study.component;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.NestedScrollView;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;


public class CustomDirectionalLayout extends DirectionalLayout {


    public CustomDirectionalLayout(Context context) {
        super(context);
    }

    public CustomDirectionalLayout(Context context, AttrSet attrSet) {
        super(context, attrSet);
    }

    public CustomDirectionalLayout(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
    }

    @Override
    public void postLayout(){
        System.out.println("CustomScrollView CustomDirectionalLayout "+getHeight());
    }


}