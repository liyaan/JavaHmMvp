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
import com.example.utils.component.decoration.holder.HeaderHolder;

/**
 * 双头部布局
 *
 * @since 2021-04-14
 */
public class DoubleHeaderModel extends ItemModel {
    /**
     * DoubleHeaderModel类型
     */
    public static final int TYPE = 2;
    private String title;
    private String message;

    /**
     * 构建item数据实体类
     *
     * @param title 标题
     * @param message 内容
     */
    public DoubleHeaderModel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public boolean isHeader() {
        return true;
    }

    @Override
    public int getResource(int position) {
        return ResourceTable.Layout_item_double_header;
    }

    @Override
    public Class getHolderClass(int position) {
        return HeaderHolder.class;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
