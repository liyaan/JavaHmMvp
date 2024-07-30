package com.liyaan.study.slice;

import com.example.utils.component.sticky.StickChangeLisener;
import com.example.utils.component.sticky.StickyScrollView;
import com.liyaan.study.ResourceTable;
import com.wefika.calendar.CollapseCalendarView;
import com.wefika.calendar.manager.CalendarManager;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import org.joda.time.LocalDate;


public class StickyScrollViewAbilitySlice extends AbilitySlice implements Component.ClickedListener{
    private StickyScrollView scrollView;
    private Image mainShoeView;
    private boolean redShoeVisible = true;
    private DirectionalLayout dl_title;
    private DirectionalLayout dl_title2;
    private DirectionalLayout dl_button;
    private DirectionalLayout dl_button2;
    private boolean isHeaderSticky;
    private boolean isFooterSticky;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_stick_slice);
        getWindow().setStatusBarColor(Color.getIntColor("#00796B"));
        scrollView = (StickyScrollView) findComponentById(ResourceTable.Id_scrollView);
        dl_title = (DirectionalLayout) findComponentById(ResourceTable.Id_title);
        dl_title2 = (DirectionalLayout) findComponentById(ResourceTable.Id_title2);
        dl_button = (DirectionalLayout) findComponentById(ResourceTable.Id_buttons);
        dl_button2 = (DirectionalLayout) findComponentById(ResourceTable.Id_buttons2);
        mainShoeView = (Image) findComponentById(ResourceTable.Id_main_shoe_picture);
        dl_title.setClickedListener(this);
        dl_title2.setClickedListener(this);
        findComponentById(ResourceTable.Id_buy).setClickedListener(this);
        findComponentById(ResourceTable.Id_buy2).setClickedListener(this);
        findComponentById(ResourceTable.Id_save).setClickedListener(this);
        findComponentById(ResourceTable.Id_save2).setClickedListener(this);
        findComponentById(ResourceTable.Id_other_product).setClickedListener(this);
        scrollView.setHeadView(dl_title);
        scrollView.setFootView(dl_button);
        scrollView.setStickChangeLisener(new StickChangeLisener() {
            @Override
            public void stcikHead() {
                dl_title2.setVisibility(Component.VISIBLE);
                isHeaderSticky = true;
            }

            @Override
            public void hideHead() {
                dl_title2.setVisibility(Component.INVISIBLE);
                isHeaderSticky = false;
            }

            @Override
            public void stickFoot() {
                dl_button2.setVisibility(Component.VISIBLE);
                isFooterSticky = true;
            }

            @Override
            public void hideFoot() {
                dl_button2.setVisibility(Component.INVISIBLE);
                isFooterSticky = false;
            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_buttons:
            case ResourceTable.Id_buttons2:
                new ToastDialog(this).setText(isFooterSticky ? "Footer is Sticky" : "Footer is not sticky").show();
                break;
            case ResourceTable.Id_title2:
            case ResourceTable.Id_title:
                new ToastDialog(this).setText(isHeaderSticky ? "Header is Sticky" : "Header is not sticky").show();
                break;
            case ResourceTable.Id_other_product:
                switchShoeViews();
                break;
        }
    }
    private void switchShoeViews() {
        if (redShoeVisible) {
            redShoeVisible = false;
            mainShoeView.setPixelMap(ResourceTable.Media_similar_1);
            DirectionalLayout.LayoutConfig params = (DirectionalLayout.LayoutConfig) mainShoeView.getLayoutConfig();
            params.height = vp2px(320);
            mainShoeView.setLayoutConfig(params);
        } else {
            redShoeVisible = true;
            mainShoeView.setPixelMap(ResourceTable.Media_nike);
            DirectionalLayout.LayoutConfig params = (DirectionalLayout.LayoutConfig) mainShoeView.getLayoutConfig();
            params.height = vp2px(420);
            mainShoeView.setLayoutConfig(params);
        }
    }

    /**
     * vp转换px工具
     *
     * @param vp 传参vp
     * @return px 返回像素值
     */
    public int vp2px(double vp) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(this).get();
        double dpi = display.getAttributes().densityPixels;
        return (int) (vp * dpi);
    }
}
