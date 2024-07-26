package com.example.utils.component.timePicker;


import ohos.app.Context;
import ohos.global.configuration.Configuration;
import ohos.global.configuration.LocaleProfile;
import ohos.global.resource.ResourceManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.Locale;

/**
 * Helper class, provides {@link Locale} specific methods.
 */
public class LocaleHelper {
    private static HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, 0x000110, "LocaleHelper----");
    /**
     * Retrieves the string from resources by specific {@link Locale}.
     *
     * @param context The context.
     * @param locale The requested locale.
     * @param resourceId The string resource id.
     * @return The string.
     */
    public static String getString(Context context, Locale locale, int resourceId) {

//		String text = context.getString(resourceId);
//        return text;
        try {
            ResourceManager resourceManager = context.getResourceManager();
            Configuration conf = resourceManager.getConfiguration();
            Locale savedLocale = conf.getFirstLocale();
            conf.setLocaleProfile(new LocaleProfile(new Locale[]{locale}));
            resourceManager.updateConfiguration(conf, null);

            // retrieve resources from desired locale
            String result = resourceManager.getElement(resourceId).getString();
            // restore original locale
            conf.setLocaleProfile(new LocaleProfile(new Locale[]{savedLocale}));
            resourceManager.updateConfiguration(conf, null);

            return result;
        } catch (Exception exception) {

        }
//
        return "";
    }
}
