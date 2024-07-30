package com.liyaan.selectpicker;


import java.util.LinkedList;

import com.example.utils.ohospickers.util.LogUtils;
import ohos.aafwk.ability.Ability;

/**
 * Activity及Service管理，以便实现退出功能
 */
public class AppManager {
  private static final String TAG = "AppManager";
  //本类的实例
  private static AppManager instance;
  //保存所有Activity
  private LinkedList<Ability> activities = new LinkedList<>();
  //保存所有Service
  private LinkedList<Ability> services = new LinkedList<>();

  public static AppManager getInstance() {
    if (instance == null) {
      instance = new AppManager();
    }
    return instance;
  }

  /**
   * 注册Activity以便集中“finish()”
   *
   * @param activity the activity
   */
  public void addActivity(Ability activity) {
    activities.add(activity);
  }

  /**
   * 移除Activity.
   *
   * @param activity the activity
   */
  public void removeActivity(Ability activity) {
    activities.remove(activity);
  }

  /**
   * 所有的Activity
   *
   * @return the activities
   */
  public LinkedList<Ability> getActivities() {
    return activities;
  }

  /**
   * 最后加入的Activity
   *
   * @return the activity
   */
  public Ability getLastActivity() {
    Ability activity = activities.getLast();
    LogUtils.debug(TAG, "last activity is " + activity.getClass().getName());
    return activity;
  }

  /**
   * 注册Service以便集中“stopSelf()”
   *
   * @param service the service
   */
  public void addService(Ability service) {
    services.add(service);
  }

  /**
   * Remove service.
   *
   * @param service the service
   */
  public void removeService(Ability service) {
    services.remove(service);
  }

  /**
   * 所有的Service
   *
   * @return the services
   */
  public LinkedList<Ability> getServices() {
    return services;
  }

  /**
   * 退出软件
   */
  public void exitApp() {
    clearActivitiesAndServices();
    ohos.os.ProcessManager.kill(ohos.os.ProcessManager.getPid());
    System.exit(0);//normal exit application
  }

  /**
   * 当内存不足时，需要清除已打开的Activity及Service
   */
  public void clearActivitiesAndServices() {
    for (Ability activity : activities) {
      if (!activity.isTerminating()) {
        activity.terminateAbility();
      }
    }
    for (Ability service : services) {
      service.terminateAbility();
    }
  }

}
