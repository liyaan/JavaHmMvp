package com.example.utils.data;

import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;

public class PreferencesUtils {
    private volatile static PreferencesUtils mPreferencesUtils;
    private Context mContext;

    private static DatabaseHelper mDatabaseHelper;

    private Preferences mPreferences;
    private PreferencesUtils(Context context){
        this.mContext = context;
    }

    public static PreferencesUtils getPreferencesUtils(Context context){
        if (mPreferencesUtils==null){
            synchronized (PreferencesUtils.class){
                if (mPreferencesUtils==null){
                    mPreferencesUtils = new PreferencesUtils(context);
                    mDatabaseHelper = new DatabaseHelper(context);
                }
            }
        }
        return mPreferencesUtils;
    }
    public Preferences init(String fileName){
        mPreferences = mDatabaseHelper.getPreferences(fileName);
        return mPreferences;
    }


}