package com.example.utils.ohospickers.listeners;

/**
 * 点击确认按钮选中item的回调
 */

public interface OnMoreItemPickListener<T> {
  void onItemPicked(T t1, T t2, T t3);
}
