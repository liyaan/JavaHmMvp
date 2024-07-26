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

package com.example.utils.component.decoration.holder;


import com.example.utils.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.app.Context;



/**
 * 公共的Item布局实体类
 *
 * @since 2021-04-14
 */
public class ItemHolder extends Holder {
    /**
     * 每一行中的文字
     */
    public Text itemText;

    /**
     * 实体类构造函数
     *
     * @param context 上下文
     * @param itemView item根布局
     */
    public ItemHolder(Context context, Component itemView) {
        itemText = (Text) itemView.findComponentById(ResourceTable.Id_item_text);
    }
}
