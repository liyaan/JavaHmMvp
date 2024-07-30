package com.example.utils.ohospickers.listeners;

import com.example.utils.ohospickers.widget.WheelView;
import ohos.agp.components.Component;
import ohos.agp.components.DragInfo;


final public class WheelViewGestureListener implements Component.DraggedListener {

  private final WheelView wheelView;

  public WheelViewGestureListener(WheelView wheelView) {
    this.wheelView = wheelView;
  }

  @Override
  public void onDragDown(Component component, DragInfo dragInfo) {
  }

  @Override
  public void onDragStart(Component component, DragInfo dragInfo) {
  }

  @Override
  public void onDragUpdate(Component component, DragInfo dragInfo) {
    wheelView.scrollBy((float) dragInfo.yOffset * 60);
  }

  @Override
  public void onDragEnd(Component component, DragInfo dragInfo) {
  }

  @Override
  public void onDragCancel(Component component, DragInfo dragInfo) {
  }

  @Override
  public boolean onDragPreAccept(Component component, int dragDirection) {
    return true;
  }
}
