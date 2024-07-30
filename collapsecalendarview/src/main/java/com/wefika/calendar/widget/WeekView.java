package com.wefika.calendar.widget;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.app.Context;

/**
 * Created by Blaz Solar on 24/02/14.
 */
public class WeekView extends DirectionalLayout implements Component.EstimateSizeListener, ComponentContainer.ArrangeListener {

    private static final String TAG = "WeekView";

    public WeekView(Context context, AttrSet attrs) {
        super(context, attrs);
//        setEstimateSizeListener(this);
//        setArrangeListener(this);
    }


    @Override
    public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = EstimateSpec.getSize(widthMeasureSpec);

        int maxSize = widthSize / 7;
        int baseSize = 0;

        int cnt = getChildCount();
        for(int i = 0; i < cnt; i++) {

            Component child = getComponentAt(i);

            if(child.getVisibility() == Component.HIDE) {
                continue;
            }
            child.estimateSize(
                    EstimateSpec.getSizeWithMode(maxSize, EstimateSpec.UNCONSTRAINT),
                    EstimateSpec.getSizeWithMode(maxSize, EstimateSpec.UNCONSTRAINT)
            );

            baseSize = Math.max(baseSize, child.getEstimatedHeight());

        }

        for (int i = 0; i < cnt; i++) {

            Component child = getComponentAt(i);

            if (child.getVisibility() == HIDE) {
                continue;
            }

            child.estimateSize(
                    EstimateSpec.getSizeWithMode(baseSize, EstimateSpec.PRECISE),
                    EstimateSpec.getSizeWithMode(baseSize, EstimateSpec.PRECISE)
            );

        }

        setEstimatedSize(widthSize, getLayoutConfig().height >= 0 ? getLayoutConfig().height : baseSize + getPaddingBottom() + getPaddingTop());
        return false;
    }

    @Override
    public boolean onArrange(int i, int i1, int i2, int i3) {
        int cnt = getChildCount();

        int width = getEstimatedWidth();
        int part = width / cnt;

        for(int j = 0; j < cnt; j++) {

            Component child = getComponentAt(i);
            if(child.getVisibility() == Component.HIDE) {
                continue;
            }

            int childWidth = child.getEstimatedWidth();

            int x = j * part + ((part - childWidth) / 2);
            child.arrange(x, 0, x + childWidth, child.getEstimatedHeight());
        }
        return false;
    }
}
