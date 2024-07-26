package com.example.utils.component.timePicker.widget;


import com.example.utils.component.timePicker.DateHelper;

import java.util.Date;

public class DateWithLabel {
    public final String mLabel;
    public final Date mDate;

    public DateWithLabel(String label, Date date) {
        if(date != null){
            mDate = (Date) date.clone();
        }else {
            mDate = null;
        }
        this.mLabel = label;

    }

    @Override
    public String toString() {
        return mLabel;
    }

    @Override
    public int hashCode() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DateWithLabel) {
            DateWithLabel newDate = (DateWithLabel) o;
            return mLabel.equals(newDate.mLabel) && DateHelper.compareDateIgnoreTime(mDate, newDate.mDate) == 0;
        }
        return false;
    }
}
