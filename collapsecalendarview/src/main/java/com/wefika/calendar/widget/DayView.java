package com.wefika.calendar.widget;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Text;
import ohos.app.Context;

/**
 * Created by Blaz Solar on 24/05/14.
 */
public class DayView extends Text {

//    private static final int[] STATE_CURRENT = { R.attr.state_current };

    private boolean mCurrent;

    public DayView(Context context) {
        super(context);
    }

    public DayView(Context context, AttrSet attrs) {
        super(context, attrs);
    }

    public DayView(Context context, AttrSet attrs, String defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCurrent(boolean current) {
        if (mCurrent != current) {
            mCurrent = current;
//            refreshDrawableState();
        }
    }

    public boolean isCurrent() {
        return mCurrent;
    }

//    @Override
//    protected int[] onCreateDrawableState(int extraSpace) {
//        final int[] state = super.onCreateDrawableState(extraSpace + 1);
//
//        if (mCurrent) {
//            mergeDrawableStates(state, STATE_CURRENT);
//        }
//
//        return state;
//    }
}
