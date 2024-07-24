package com.liyaan.study.component;

import com.liyaan.study.utils.SparseArray;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
public class CustomViewHolder {
    private Component mComponent;
    private final SparseArray<Component> views;
    public CustomViewHolder(Component component) {
        this.mComponent = component;
        this.views =new SparseArray<>();
    }

    public void setTextContent(int id,String content){
        Text text = getView(id);
        text.setText(content);

    }
    public void setBgColor(int id, RgbColor rgbColor){
        Text text = getView(id);
        ShapeElement element = new ShapeElement();
        element.setRgbColor(rgbColor);
        text.setBackground(element);

    }
    public <T extends Component> T getView( int viewId) {
        Component view = views.get(viewId);
        if (view == null) {
            view = mComponent.findComponentById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}