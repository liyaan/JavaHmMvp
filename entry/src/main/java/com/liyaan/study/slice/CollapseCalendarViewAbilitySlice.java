package com.liyaan.study.slice;

import com.liyaan.study.ResourceTable;
import com.wefika.calendar.CollapseCalendarView;
import com.wefika.calendar.manager.CalendarManager;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import org.joda.time.LocalDate;


public class CollapseCalendarViewAbilitySlice extends AbilitySlice {
    private CollapseCalendarView mCalendarView;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_collapse_calendar_view);
        mCalendarView = (CollapseCalendarView) findComponentById(ResourceTable.Id_calendar);
        LocalDate selected = LocalDate.now();
        LocalDate minDate = LocalDate.now();
        LocalDate maxDate = LocalDate.now().plusYears(1);
        CalendarManager manager = new CalendarManager(selected, CalendarManager.State.MONTH, minDate, maxDate);
        mCalendarView.init(manager);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

}
