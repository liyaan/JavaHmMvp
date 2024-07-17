package com.liyaan.study.slice;

import com.liyaan.study.ResourceTable;
import com.liyaan.study.WelComeAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.dispatcher.task.TaskPriority;

import java.util.Timer;
import java.util.TimerTask;

public class WelComeAbilitySlice extends AbilitySlice {
    private static final long DELAY = 3000;
    private Timer mTimer;
    private TimerTask mTimerTask;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_welcome_slice);
    }

    @Override
    public void onActive() {
        super.onActive();

        // 初始化Intent
        final Intent intent = new Intent();
//        final ToastDialog toastDialog = new ToastDialog(this);
//        toastDialog.setText("倒计时完成");

        // 通过Intent中的OperationBuilder类构造operation对象，指定设备标识（空串表示当前设备）、应用包名、Ability名称
        // withBundleName为读者自己项目的包名
        // withAbilityName为读者需要跳转到主界面的Ability的名字
//        Operation operation = new Intent.OperationBuilder()
//                .withDeviceId("")
//                .withBundleName("com.liyaan.study.slice")
//                .withAbilityName("com.liyaan.study.slice.LoginAbilitySlice")
//                .build();
// 把operation设置到intent中
//        intent.setOperation(operation);
//        startAbility(intent);//执行
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                getUITaskDispatcher().asyncDispatchBarrier(()->{
//                    toastDialog.show();
     //               intent.setParam("productId",product.getProductId());
                    present(new LoginAbilitySlice(),intent);
                    terminate();
                });

            }
        };
        mTimer.schedule(mTimerTask,DELAY);
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
