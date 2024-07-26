package com.liyaan.study.utils;

import com.liyaan.study.ResourceTable;
import com.liyaan.study.component.CustomPageSlider;
import ohos.agp.components.PageSlider;
import ohos.agp.components.PageSliderIndicator;
import ohos.agp.components.PageSliderProvider;
import ohos.agp.components.element.ShapeElement;

public class PageSliderUtils {

    public static void settingPageSlider(CustomPageSlider pageSlider,
                                         PageSliderIndicator pageSliderIndicator,
                                         PageSliderProvider pageProvider,int dataSize){
        if (pageSlider==null){
            throw new RuntimeException("PageSlider 不能为空");
        }
        pageSlider.setProvider(pageProvider);
        pageSlider.setDataSize(dataSize);
        pageSlider.startPlaying();
        pageSlider.addTouchEventListener();
        if (pageSliderIndicator!=null)
            setPageSliderIndicator(pageSliderIndicator,pageSlider);
    }
    private static void setPageSliderIndicator(PageSliderIndicator indicator,PageSlider pageSlider){

        ShapeElement normalElement = new ShapeElement(pageSlider.getContext(), ResourceTable.Graphic_unselected_page_bg_element);
        ShapeElement selectedElement = new ShapeElement(pageSlider.getContext(), ResourceTable.Graphic_selected_page_bg_element);
        if (pageSlider instanceof CustomPageSlider){
            ((CustomPageSlider) pageSlider).setPageSliderIndicator(indicator,normalElement,selectedElement);
        }



    }
}