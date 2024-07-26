/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.utils.component.decoration.model;


import com.example.utils.ResourceTable;
import com.example.utils.component.decoration.holder.ItemHolder;

import java.lang.reflect.Array;



/**
 * 文字类型Item
 *
 * @since 2021-04-14
 */
public class ItemModel implements Model {
    /**
     * ItemModel类型
     */
    public static final int TYPE = 0;
    private String title;
    private String message;
    private int[] paddings = new int[]{0, 0, 0, 0};
    private boolean isInline = false;

    /**
     * 空参构造函数
     */
    public ItemModel() {
    }

    /**
     * 构建item数据实体类
     *
     * @param title 标题
     * @param message 内容
     * @param hasInline 内嵌文字间隔
     */
    public ItemModel(String title, String message, boolean hasInline) {
        this.title = title;
        this.message = message;
        this.isInline = hasInline;
    }

    /**
     * 设置边距
     *
     * @param paddings 边距间隔
     */
    public void setPaddings(int[] paddings) {
        for (int i = 0; i < paddings.length; i++) {
            this.paddings[i] = paddings[i];
        }
    }

    @Override
    public int getResource(int position) {
        if (isInline) {
            return ResourceTable.Layout_item_inline_text;
        }
        return ResourceTable.Layout_item_test;
    }

    @Override
    public Class getHolderClass(int position) {
        return ItemHolder.class;
    }

    @Override
    public boolean isHeader() {
        return false;
    }

    @Override
    public String getTitle() {
        return title != null ? title : "";
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int[] getPadding() {
        return (int[]) Array.get(paddings, paddings.length);
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
