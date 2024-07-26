

package com.example.utils.log;

import com.example.utils.component.decoration.model.DoubleHeaderModel;
import com.example.utils.component.decoration.model.HeaderModel;
import com.example.utils.component.decoration.model.InlineHeaderModel;
import com.example.utils.component.decoration.model.ItemModel;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 *
 * @since 2021-04-21
 */
public class UtilLog {
    private static final String TAG = "log_content";
    private static HiLogLabel sLogLabel;
    private static final int DOMAIN = 0x001;

    private UtilLog() {
    }

    private static HiLogLabel createLogLabel() {
        if (sLogLabel == null) {
            sLogLabel = new HiLogLabel(HiLog.LOG_APP, DOMAIN, TAG);
        }
        return sLogLabel;
    }

    /**
     * 输出info日志
     *
     * @param format 日志信息
     * @param objects 数据内容
     */
    public static void info(String format, Object... objects) {
        HiLog.info(createLogLabel(), format, objects);
    }

    /**
     * 输出debug日志
     *
     * @param format 日志信息
     * @param objects 数据内容
     */
    public static void debug(String format, Object... objects) {
        HiLog.debug(createLogLabel(), format, objects);
    }

    /**
     * 输出error日志
     *
     * @param format 日志信息
     * @param objects 数据内容
     */
    public static void error(String format, Object... objects) {
        HiLog.error(createLogLabel(), format, objects);
    }

    /**
     * 输出warn日志
     *
     * @param format 日志信息
     * @param objects 数据内容
     */
    public static void warn(String format, Object... objects) {
        HiLog.warn(createLogLabel(), format, objects);
    }

    /**
     * 输出fatal日志
     *
     * @param format 日志信息
     * @param objects 数据内容
     */
    public static void fatal(String format, Object... objects) {
        HiLog.fatal(createLogLabel(), format, objects);
    }

    /**
     * 获取Sticky数据源
     *
     * @return 数据集合
     */
    public static List<ItemModel> getStickyList() {
        List<ItemModel> items = new ArrayList<>();
        ItemModel itemModel = null;
        int headerNum = 0;
        int itemNum = 0;
        for (int i = 0; i < 58; i++) {
            if (i == 1 || i == 8 || i == 16 || i == 24 || i == 32 || i == 40 || i == 48 || i == 56) {
                itemModel = new HeaderModel("Header " + headerNum, "");
                headerNum++;
            } else {
                itemModel = new ItemModel("Item " + itemNum, "Item description at " + i, false);
                itemNum++;
            }
            items.add(itemModel);
        }
        return items;
    }

    /**
     * 获取StickyInline数据源
     *
     * @return 数据集合
     */
    public static List<ItemModel> getStickyInlineList() {
        List<ItemModel> items = new ArrayList<>();
        ItemModel itemModel = null;
        int headerNum = 0;
        for (int i = 0; i < 50; i++) {
            if (i % 7 == 0) {
                itemModel = new InlineHeaderModel(String.valueOf(headerNum), "Item " + i);
                headerNum++;
            } else {
                itemModel = new ItemModel("Item " + i, "Item description at " + i, true);
                itemModel.setPaddings(new int[]{300, 0, 0, 0});
            }
            items.add(itemModel);
        }
        return items;
    }

    /**
     * 获取Double数据源
     *
     * @return 数据集合
     */
    public static List<ItemModel> getDoubleList() {
        List<ItemModel> items = new ArrayList<>();
        ItemModel itemModel = null;
        int headerNum = 0;
        int doubleNum = 0;
        int itemNum = 0;
        for (int i = itemNum; i < 62; i++) {
            if (i == 17 || i == 34 || i == 51 || i == 0) {
                itemModel = new DoubleHeaderModel("Header " + headerNum, "");
                headerNum++;
            } else if (i == 9 || i == 18 || i == 26 || i == 35 || i == 43 || i == 52 || i == 60 || i == 1) {
                itemModel = new HeaderModel("Sub-header " + doubleNum, "");
                doubleNum++;
            } else {
                itemModel = new ItemModel("Item " + itemNum, "Item description at " + i, false);
                itemNum++;
            }
            items.add(itemModel);
        }
        return items;
    }

    /**
     * 获取DoubleInline数据源
     *
     * @return 数据集合
     */
    public static List<ItemModel> getDoubleInlineList() {
        List<ItemModel> items = new ArrayList<>();
        ItemModel itemModel = null;
        int headerNum = 0;
        int doubleNum = 0;
        for (int i = 0; i <= 53; i++) {
            if (i == 15 || i == 30 || i == 45 || i == 0) {
                itemModel = new DoubleHeaderModel("Header " + headerNum, "");
                headerNum++;
            } else if (i == 8 || i == 16 || i == 23 || i == 31 || i == 38 || i == 46 || i == 53 || i == 1) {
                itemModel = new InlineHeaderModel(String.valueOf(doubleNum), "Item " + (i - headerNum));
                doubleNum++;
            } else {
                itemModel = new ItemModel("Item " + (i - headerNum), "Item description at " + i, true);
                itemModel.setPaddings(new int[]{300, 0, 0, 0});
            }
            items.add(itemModel);
        }
        return items;
    }
}
