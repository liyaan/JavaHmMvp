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

/**
 * 所有需要在ListContainer中显示的模型都应该实现这个接口。这个接口定义了模型需要返回的布局文件和ViewHolder类
 *
 * @since 2021-04-14
 */
public interface Model {
    /**
     * 当前Model配置布局文件
     *
     * @param position 当前Model在数组中的位置
     * @return 返回布局文件
     */
    int getResource(int position);

    /**
     * 返回模型对应的ViewHolder
     *
     * @param position 当前Model在数组中的位置
     * @return 返回当前布局文件对应的ViewHolder类
     */
    Class getHolderClass(int position);

    /**
     * 是否有header布局
     *
     * @return teue 是 false 否
     */
    boolean isHeader();

    /**
     * 获取标题文字
     *
     * @return 标题
     */
    String getTitle();

    /**
     * 获取内容信息
     *
     * @return 消息
     */
    String getMessage();

    /**
     * 获取padding值
     *
     * @return padding数组
     */
    int[] getPadding();

    /**
     * 获取model类型
     *
     * @return 类型值
     */
    int getType();
}
