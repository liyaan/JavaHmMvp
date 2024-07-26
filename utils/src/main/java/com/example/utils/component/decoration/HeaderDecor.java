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

package com.example.utils.component.decoration;

import com.example.utils.component.decoration.model.DoubleHeaderModel;
import com.example.utils.component.decoration.model.HeaderModel;
import com.example.utils.component.decoration.model.InlineHeaderModel;
import com.example.utils.component.decoration.model.ItemModel;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

import java.util.List;

/**
 * 滑动逻辑处理
 *
 * @since 2021-04-21
 */
public class HeaderDecor {
    private static final int UPSCROLL = 1;
    private static final int DOWNSCROLL = 2;
    private static final int FIRST_ITEM = 1;
    private static final int SECOND_ITEM = 2;
    private static final int UNIT_Y = 20;
    private static final int MARGIN_TOP = 48;
    /**
     * tab显示状态，true正常状态
     */
    private static boolean isTabChanged = true;
    private ListContainer listContainer;
    private Text headerText;
    private Text headerDoubleText;
    private List<ItemModel> dataList;
    private float headerTextY = 0;
    private float headerDoubleTextY = 0;
    private int scrollType = 0;
    /**
     * 下滑时是否要更改上方悬停标题
     */
    private boolean isChangeTitle = false;
    /**
     * onTouchEvent事件中记录down时的Y轴值
     */
    private float mDownY = 0;
    /**
     * 单头部显示的字符串
     */
    private String headerString = "Header 0";
    /**
     * 双头部显示的字符串
     */
    private String headerDoubleString = "";
    /**
     * 是否单header
     */
    private boolean isDoubleHeader = false;

    private int mMoveY;

    /**
     * 单header构造函数
     *
     * @param listContainer 滑动控件
     * @param oneText 头部Header
     */
    public HeaderDecor(ListContainer listContainer, Text oneText) {
        isDoubleHeader = false;
        this.listContainer = listContainer;
        this.headerText = oneText;
        this.headerTextY = headerText.getHeight();
        setTouchEventLisener();
        setScrolledListener();
    }
    /**
     * 单header构造函数
     *
     * @param listContainer 滑动控件
     * @param oneText 头部Header
     */
    public HeaderDecor(ListContainer listContainer, Text oneText,int moveY) {
        isDoubleHeader = false;
        this.listContainer = listContainer;
        this.headerText = oneText;
        this.headerTextY = headerText.getHeight();
        this.mMoveY = moveY;
        setTouchEventLisener();
        setScrolledListener();
    }
    /**
     * 双header构造函数
     *
     * @param listContainer 滑动控件
     * @param oneText 头部Header
     * @param twoText 头部DoubleHeader
     */
    public HeaderDecor(ListContainer listContainer, Text oneText, Text twoText) {
        isDoubleHeader = true;
        this.listContainer = listContainer;
        this.headerText = oneText;
        this.headerDoubleText = twoText;
        this.headerTextY = headerText.getHeight();
        this.headerDoubleTextY = headerDoubleText.getHeight();
        setTouchEventLisener();
        setScrolledListener();
        if (headerText.getWidth() == headerDoubleText.getWidth()) {
            headerDoubleString = "Sub-header 0";
        } else {
            headerDoubleString = "0";
        }
    }

    public void setDataList(List<ItemModel> dataList) {
        this.dataList = dataList;
    }

    private void setTouchEventLisener() {
        listContainer.setTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                int action = touchEvent.getAction();
                MmiPoint point = touchEvent.getPointerPosition(touchEvent.getIndex());
                switch (action) {
                    case TouchEvent.PRIMARY_POINT_DOWN:
                        return true;
                    case TouchEvent.POINT_MOVE:
                        if (point.getY() > mDownY) {
                            scrollType = DOWNSCROLL;
                        } else if (point.getY() < mDownY) {
                            scrollType = UPSCROLL;
                        }
                        mDownY = point.getY();
                        break;
                    case TouchEvent.PRIMARY_POINT_UP:
                        mDownY = 0;
                        break;
                    case TouchEvent.NONE:
                    case TouchEvent.CANCEL:
                        mDownY = point.getY();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
    }
    private void setScrolledListener() {
        listContainer.setScrolledListener(new Component.ScrolledListener() {
            @Override
            public void onContentScrolled(Component component, int scrollX, int scrollY,
                                          int oldScrollX, int oldScrollY) {
                Utils.info("scrollY = "+scrollY+",oldScrollY="+oldScrollY+",scrollType="+scrollType);
                if (!isTabChanged) {
                    return;
                }
                if (scrollType == DOWNSCROLL && scrollY<oldScrollY) {
                    if (isDoubleHeader) {
                        downDoubleMove();
                    } else {
                        downMove();
                    }
                } else if (scrollType == UPSCROLL && scrollY>oldScrollY) {
                    if (isDoubleHeader) {
                        upMoveHeader(scrollY);
                        upDoubleMove();
                    } else {
                        upMove();
                    }
                }
                if (isDoubleHeader && scrollY <= headerTextY * 2) {
                    if (scrollY >= MARGIN_TOP) {
                        headerText.setVisibility(Component.VISIBLE);
                        headerDoubleText.setVisibility(Component.VISIBLE);
                    } else {
                        headerText.setVisibility(Component.HIDE);
                        headerDoubleText.setVisibility(Component.HIDE);
                    }
                }
            }
        });
    }

    /**
     * 向下滑动
     */
    private void downMove() {

        int firstIndex = listContainer.getItemPosByVisibleIndex(0);
        if (firstIndex < 0 || firstIndex >= dataList.size()) {
            return;
        }
        int index = listContainer.getItemPosByVisibleIndex(1);
        boolean isHeader = dataList.get(index).isHeader();
        Component firstView = listContainer.getComponentAt(index);
        if (firstView == null) {
            return;
        }
        int firstViewTop = firstView.getTop();
        Utils.info("downMove    firstViewTop  " + firstViewTop+" ,headerTextY = "+headerTextY);
        if (firstIndex == 0) {
            if (!dataList.get(0).isHeader()) {
                headerText.setVisibility(Component.HIDE);
            } else {
                if (firstViewTop >= headerTextY) {
                    headerText.setVisibility(Component.HIDE);
                }
            }
            return;
        }
        if (isHeader) {
            isChangeTitle = true;
            float moveY = firstViewTop - headerTextY+mMoveY;
            if (moveY <= 0) {
                headerText.setContentPositionY(moveY);
            }
            for (int i = index - 1; i >= 0; i--) {
                if (dataList.get(i).isHeader()) {
                    headerString = dataList.get(i).getTitle();
                    break;
                }
            }
            headerText.setText(headerString);
        } else {
            if (isChangeTitle && index != 0) {
                headerText.setText(headerString);
            }
            headerText.setContentPositionY(mMoveY);
            isChangeTitle = false;
        }
    }

    /**
     * 向上滑动
     */
    private void upMove() {
        // 获取当前屏幕完整显示的第1个item的下标
        int index = listContainer.getItemPosByVisibleIndex(1);
        if (index < 0 || index >= dataList.size()) {
            return;
        }
        boolean isHeader = dataList.get(index).isHeader();
        Component firstView = listContainer.getComponentAt(index);
        if (firstView == null) {
            return;
        }
        int firstViewTop = firstView.getTop();
        Utils.info("upMove    firstViewTop  " + firstViewTop+" ,headerTextY = "+headerTextY);
        if (index == 1 && firstViewTop <= headerTextY) {
            if (dataList.get(0).isHeader()) {
                headerText.setText(dataList.get(0).getTitle());
                headerText.setVisibility(Component.VISIBLE);
            }
        }

        if (isHeader) {
            isChangeTitle = true;
            float moveY = firstViewTop - headerTextY+mMoveY;
            if (moveY <= 0) {
                headerText.setContentPositionY(moveY);
            }
            ItemModel itemModel = null;
            itemModel = dataList.get(index);
            if (itemModel != null) {
                headerString = itemModel.getTitle();
            }
        } else {
            if (isChangeTitle && index != 0) {
                Utils.warn("##### upMove  headerText.setText ######" + headerString);
                headerText.setText(headerString);
                headerText.setVisibility(Component.VISIBLE);
            }
            headerText.setContentPositionY(mMoveY);
            isChangeTitle = false;
        }
    }

    private void downDoubleMove() {
        // headerText滑动
        int headerIndex = listContainer.getItemPosByVisibleIndex(1);
        if (headerIndex < 0 || headerIndex >= dataList.size()) {
            return;
        }
        boolean isHeader = dataList.get(headerIndex).isHeader();
        int type = dataList.get(headerIndex).getType();
        if (isHeader && type == DoubleHeaderModel.TYPE) {
            isChangeTitle = true;
            headerDoubleText.setVisibility(Component.HIDE);
            Component firstView = listContainer.getComponentAt(headerIndex);
            if (firstView == null) {
                return;
            }
            int firstViewTop = firstView.getTop();
            float moveY = firstViewTop - headerTextY;
            headerText.setContentPositionY(moveY);
            for (int i = headerIndex - 1; i >= 0; i--) {
                if (dataList.get(i).isHeader() && dataList.get(i).getType() == DoubleHeaderModel.TYPE) {
                    headerString = dataList.get(i).getTitle();
                    break;
                }
                headerString = "Header 0";
            }
            headerText.setText(headerString);
        } else {
            if (isChangeTitle && headerIndex != 0) {
                headerText.setText(headerString);
            }
            headerText.setContentPositionY(0);
            if (headerDoubleText.getVisibility() != Component.VISIBLE) {
                headerDoubleText.setVisibility(Component.VISIBLE);
                headerDoubleText.setContentPositionY(headerTextY);
            }
            isChangeTitle = false;
        }
        moveDownByDoubleText(headerIndex);
    }

    private void moveDownByDoubleText(int headerIndex) {
        // doubleText滑动
        int doubleIndex = listContainer.getItemPosByVisibleIndex(SECOND_ITEM);
        if (doubleIndex < 0 || doubleIndex >= dataList.size()) {
            return;
        }
        boolean isHeader = dataList.get(doubleIndex).isHeader();
        int doubleType = dataList.get(doubleIndex).getType();
        if (isHeader) {
            isChangeTitle = true;
            Component firstView = listContainer.getComponentAt(doubleIndex);
            if (firstView == null) {
                return;
            }
            int firstViewTop = firstView.getTop();
            /**
             * moveY取值应为210-0
             */
            float moveY = firstViewTop - headerDoubleTextY;
            if (moveY <= headerTextY) {
                headerDoubleText.setContentPositionY(moveY);
            }
            for (int i = headerIndex - 1; i >= 0; i--) {
                if (dataList.get(i).isHeader() && (dataList.get(i).getType() == HeaderModel.TYPE
                    || dataList.get(i).getType() == InlineHeaderModel.TYPE)) {
                    headerDoubleString = dataList.get(i).getTitle();
                    break;
                }
                if (doubleType == HeaderModel.TYPE) {
                    headerDoubleString = "Sub-header 0";
                } else if (doubleType == InlineHeaderModel.TYPE) {
                    headerDoubleString = "0";
                }
            }
            headerDoubleText.setText(headerDoubleString);
        } else {
            headerDoubleText.setText(headerDoubleString);
            headerDoubleText.setContentPositionY(headerTextY);
            isChangeTitle = false;
        }
    }

    private void upDoubleMove() {
        // headerText滑动
        int headerIndex = listContainer.getItemPosByVisibleIndex(1);
        if (headerIndex < 0 || headerIndex >= dataList.size()) {
            return;
        }
        boolean isHeader = dataList.get(headerIndex).isHeader();
        int type = dataList.get(headerIndex).getType();
        if (isHeader && type == DoubleHeaderModel.TYPE) {
            isChangeTitle = true;
            headerDoubleText.setVisibility(Component.HIDE);
            Component firstView = listContainer.getComponentAt(headerIndex);
            if (firstView == null) {
                return;
            }
            int firstViewTop = firstView.getTop();
            float moveY = firstViewTop - headerTextY;
            headerText.setContentPositionY(moveY);
            headerString = dataList.get(headerIndex).getTitle();
        } else {
            if (isChangeTitle && headerIndex != 0) {
                headerText.setText(headerString);
            }
            headerText.setContentPositionY(0);
            if (headerDoubleText.getVisibility() != Component.VISIBLE) {
                headerDoubleText.setVisibility(Component.VISIBLE);
                headerDoubleText.setContentPositionY(headerTextY);
            }
            isChangeTitle = false;
        }

        moveUpByDoubleText();
    }

    private void moveUpByDoubleText() {
        int doubleIndex = listContainer.getItemPosByVisibleIndex(SECOND_ITEM);
        if (doubleIndex < 0 || doubleIndex >= dataList.size()) {
            return;
        }
        boolean isSecondHeader = dataList.get(doubleIndex).isHeader();
        int doubleType = dataList.get(doubleIndex).getType();
        if (isSecondHeader) {
            isChangeTitle = true;
            Component firstView = listContainer.getComponentAt(doubleIndex);
            if (firstView == null) {
                return;
            }
            int firstViewTop = firstView.getTop();
            /**
             * moveY取值应为210-0`
             */
            float moveY = firstViewTop - headerDoubleTextY;
            if (moveY <= headerTextY) {
                headerDoubleText.setContentPositionY(moveY);
            }
            if (DoubleHeaderModel.TYPE == doubleType && moveY <= UNIT_Y) {
                headerDoubleText.setVisibility(Component.HIDE);
            }
            if (doubleType == HeaderModel.TYPE || doubleType == InlineHeaderModel.TYPE) {
                headerDoubleString = dataList.get(doubleIndex).getTitle();
            }
        } else {
            headerDoubleText.setText(headerDoubleString);
            headerDoubleText.setContentPositionY(headerTextY);
            isChangeTitle = false;
        }
    }

    private void upMoveHeader(int scrollY) {
        if (scrollY <= MARGIN_TOP) {
            headerText.setContentPositionY(-scrollY);
            headerDoubleText.setContentPositionY(headerTextY - scrollY);
        }
    }

    /**
     * 监听tab页状态类
     *
     * @since 2021-04-25
     */
    public static class MyTabListener implements TabItemChangeListener {
        @Override
        public void onTabChangeStatus(boolean status) {
            isTabChanged = status;
        }
    }
}
