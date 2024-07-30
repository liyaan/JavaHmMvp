package com.example.utils.ohospickers.common;


import com.example.utils.ohospickers.listeners.OnItemPickListener;
import com.example.utils.ohospickers.widget.WheelView;

final public class OnItemPickedRunnable implements Runnable {
  final private WheelView wheelView;
  private OnItemPickListener onItemPickListener;

  public OnItemPickedRunnable(WheelView wheelView, OnItemPickListener onItemPickListener) {
    this.wheelView = wheelView;
    this.onItemPickListener = onItemPickListener;
  }

  @Override
  public final void run() {
    wheelView.getContext().getUITaskDispatcher().asyncDispatch(() ->
        onItemPickListener.onItemPicked(wheelView.getCurrentPosition(), wheelView.getCurrentItem())
    );
  }
}
