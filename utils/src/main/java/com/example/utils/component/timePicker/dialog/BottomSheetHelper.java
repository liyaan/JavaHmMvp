package com.example.utils.component.timePicker.dialog;

import com.example.utils.ResourceTable;
import com.example.utils.component.timePicker.Utils;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ComponentTreeObserver;
import ohos.agp.components.LayoutScatter;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.BaseDialog;
import ohos.agp.window.dialog.PopupDialog;
import ohos.agp.window.service.Window;
import ohos.agp.window.service.WindowManager;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;



public class BottomSheetHelper {
    private static HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, 0x000110, "BottomSheetHelper-- ");
    private Context context;
    private int layoutId;

    private Component mComponent;
    private Listener listener;

    private EventHandler handler;
    private WindowManager windowManager;

    private boolean focusable;
    private Window mAddWindow;
    private PopupDialog popupDialog;
    private int mLayoutAlignment;
    private int mDialogWidthSize;
//    private AnimatorProperty popIntproperty;
    public BottomSheetHelper(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        this.handler = new EventHandler(EventRunner.getMainEventRunner());
    }

    private void init() {
        handler.postTask(new Runnable() {
            @Override
            public void run() {
//        if (context instanceof Activity) {
                windowManager = WindowManager.getInstance();
                mComponent = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_bottom_sheet_picker_bottom_sheet, null, true);
                // Don't let it grab the input focus if focusable is false
                int flags = focusable ? 0 : WindowManager.LayoutConfig.MARK_FOCUSABLE_IMPOSSIBLE;
                WindowManager.LayoutConfig layoutParams = new WindowManager.LayoutConfig();
                layoutParams.width = WindowManager.LayoutConfig.MARK_FULL_SCREEN;
                layoutParams.height = WindowManager.LayoutConfig.MARK_FULL_SCREEN;
                layoutParams.type = WindowManager.LayoutConfig.MOD_APPLICATION_PANEL;
                layoutParams.flags = flags;

                mAddWindow = windowManager.addComponent((ComponentContainer) mComponent, context, WindowManager.LayoutConfig.MOD_APPLICATION_PANEL);
                mAddWindow.setTransparent(true);

                mComponent.findComponentById(ResourceTable.Id_bottom_sheet_background)
                    .setClickedListener(new Component.ClickedListener() {
                        @Override
                        public void onClick(Component component) {
                            hide();
                        }
                    });

                mComponent.getComponentTreeObserver().addWindowBoundListener(new ComponentTreeObserver.WindowBoundListener() {
                    @Override
                    public void onWindowBound() {
                        mComponent.getComponentTreeObserver().removeWindowBoundListener(this);
                        if (listener != null) {
                            listener.onLoaded(mComponent);
                        }
                        animateBottomSheet();
                    }

                    @Override
                    public void onWindowUnbound() {

                    }
                });
            }
        }, 100);
    }

    public BottomSheetHelper setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    public void display() {
//        init();
        initPopupDialog();
    }


    public void setLayoutAlignment(int alignment) {
        this.mLayoutAlignment = alignment;
    }

    public void setDialogWidthSize(int dialogWidthSize) {
        this.mDialogWidthSize = dialogWidthSize;
    }

    private void initPopupDialog() {

        mComponent = LayoutScatter.getInstance(context)
            .parse(layoutId, null, true);
//        popIntproperty = createAnimInPop();
        popupDialog = new PopupDialog(context, null);
        popupDialog.setCustomComponent(mComponent);
        //开始 动画
//        popIntproperty.setTarget(mComponent);
        if (mDialogWidthSize != 0) {
            popupDialog.setSize(mDialogWidthSize, ComponentContainer.LayoutConfig.MATCH_CONTENT);
        } else {
            popupDialog.setSize(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);

        }
        popupDialog.setOffset(0, 55);
        popupDialog.setAutoClosable(true);
//        popupDialog.setAlignment(LayoutAlignment.BOTTOM);
        if (listener != null) {
            listener.onLoaded(mComponent);
        }
        popupDialog.setDialogListener(new BaseDialog.DialogListener() {
            @Override
            public boolean isTouchOutside() {
                popupDialog.destroy();
                return false;
            }
        });

        if (mLayoutAlignment != 0) {
            popupDialog.showOnCertainPosition(mLayoutAlignment, 0, 0);
        } else {
            int height = Utils.getDisplayHeightInPx(context);
            int width = Utils.getDisplayWidthInPx(context);
            if (height > width) {
                popupDialog.showOnCertainPosition(LayoutAlignment.BOTTOM, -100, 100);
            } else {
                popupDialog.showOnCertainPosition(LayoutAlignment.BOTTOM, -100, 0);
            }

        }

    }

    private void showDialog() {
        if (popupDialog != null) {
            popupDialog.show();
//            popIntproperty.start();
        }
    }

    private AnimatorProperty createAnimInPop() {
        AnimatorProperty animator = new AnimatorProperty();
        animator.moveFromY(450).moveToY(0).setDuration(2000);
        return animator;
    }
    public void hide() {

        if (popupDialog != null) {
            popupDialog.hide();
        }

        if (listener != null) {
            listener.onClose();
        }
//        handler.postTask(new Runnable() {
//            @Override
//            public void run() {
//                AnimatorProperty animator = mComponent.createAnimatorProperty();
//                animator.moveFromY(0).moveToY(mComponent.getHeight()).setDuration(500);
//                animator.start();
//                animator.setStateChangedListener(new Animator.StateChangedListener() {
//                    @Override
//                    public void onStart(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onStop(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onCancel(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onEnd(Animator animator) {
//                        mComponent.setVisibility(Component.HIDE);
//                        if (listener != null) {
//                            listener.onClose();
//                        }
//                        remove();
//                    }
//
//                    @Override
//                    public void onPause(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onResume(Animator animator) {
//
//                    }
//                });
//            }
//        }, 200);
    }

    public void dismiss() {
        remove();
    }

    private void remove() {
        if (mAddWindow != null) {
            windowManager.destroyWindow(mAddWindow);
            mAddWindow = null;
        }

    }

    private void animateBottomSheet() {
        AnimatorProperty animator = mComponent.createAnimatorProperty();
        animator.moveFromY(mComponent.getHeight()).moveToY(0).setDuration(500);
        animator.start();
        animator.setStateChangedListener(new Animator.StateChangedListener() {
            @Override
            public void onStart(Animator animator) {

            }

            @Override
            public void onStop(Animator animator) {

            }

            @Override
            public void onCancel(Animator animator) {

            }

            @Override
            public void onEnd(Animator animator) {
                if (listener != null) {
                    listener.onOpen();
                }
            }

            @Override
            public void onPause(Animator animator) {

            }

            @Override
            public void onResume(Animator animator) {

            }
        });
    }

    public interface Listener {
        void onOpen();

        void onLoaded(Component component);

        void onClose();
    }
}
