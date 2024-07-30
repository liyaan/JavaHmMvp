package com.liyaan.study.component;

import ohos.agp.components.*;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;


public class CustomScrollView extends NestedScrollView {


    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttrSet attrSet) {
        super(context, attrSet);
    }

    public CustomScrollView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
    }


}