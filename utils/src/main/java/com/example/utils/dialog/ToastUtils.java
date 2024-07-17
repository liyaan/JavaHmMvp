package com.example.utils.dialog;

import com.example.utils.ResourceTable;
import ohos.agp.components.*;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

public class ToastUtils {
    private static ToastDialog mToastDialog;

    public static void show(Context context, String content){
        if (mToastDialog==null){
            mToastDialog = new ToastDialog(context);
        }
        mToastDialog.setText(content);
        mToastDialog.setAlignment(TextAlignment.CENTER);
        mToastDialog.show();
    }

    public static void oneShow(Context context, String content,int gravity){
        if (mToastDialog==null){
            mToastDialog = new ToastDialog(context);

        }
        DirectionalLayout toastLayout = (DirectionalLayout) LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_toast, null, false);
        mToastDialog.setContentCustomComponent(toastLayout);
        Text tv = (Text) toastLayout.findComponentById(ResourceTable.Id_toast_tv);
        tv.setText(content);
        mToastDialog.setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
//        mToastDialog.setText(content);
        mToastDialog.setAlignment(gravity);
        mToastDialog.show();
    }
}