package com.xiaohui.bridge.view;

/**
 * Created by xiaohui on 14-10-28.
 */
public interface OnItemClick<T> {
    public void onItemSelect(int position, T object);
}
