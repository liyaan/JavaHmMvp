package com.example.utils.ohospickers.common;


import com.example.utils.ohospickers.widget.WheelView;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;


final public class MessageHandler extends EventHandler {
  public static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
  public static final int WHAT_SMOOTH_SCROLL = 2000;
  public static final int WHAT_ITEM_SELECTED = 3000;

  private WheelView wheelView;

  public MessageHandler(EventRunner runner, WheelView wheelView) throws IllegalArgumentException {
    super(runner);
    this.wheelView = wheelView;
  }

  @Override
  protected void processEvent(InnerEvent msg) {
    switch (msg.eventId) {
      case WHAT_INVALIDATE_LOOP_VIEW:
        wheelView.getContext().getUITaskDispatcher().asyncDispatch(() ->
            wheelView.invalidate()
        );
        break;
      case WHAT_SMOOTH_SCROLL:
        wheelView.getContext().getUITaskDispatcher().asyncDispatch(() ->
            wheelView.smoothScroll(WheelView.ACTION.FLING)
        );
        break;
      case WHAT_ITEM_SELECTED:
        wheelView.onItemPicked();
        break;
    }
  }

}
