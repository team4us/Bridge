package com.xiaohui.bridge.business.enums;

import com.xiaohui.bridge.R;

import java.io.Serializable;

/**
 */
public enum EDiseaseMethod implements Serializable {
    MethodOne(0, R.string.method_name_0, 4),
    MethodTwo(1, R.string.method_name_1, 3),
    MethodThree(2, R.string.method_name_2, 4),
    MethodFour(3, R.string.method_name_3, 4),
    MethodFive(4, R.string.method_name_4, 3),
    MethodSix(5, R.string.method_name_5, 2);

    private final int id;
    private final int nameResId;
    private final int count; //标识该种记录方法包含几个键值对

    private EDiseaseMethod(int id, int nameResId, int count) {
        this.id = id;
        this.nameResId = nameResId;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public int getNameResId() {
        return nameResId;
    }

    public int getCount() {
        return count;
    }

}
