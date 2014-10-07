package com.xiaohui.bridge.business.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 */
public enum ETakePhotoType implements Serializable {
    Take(0, "拍照"),
    pick(1, "从相册选择");

    private int id;
    private String title;

    public final static ArrayList<ETakePhotoType> takePhotoTypeList
            = new ArrayList<ETakePhotoType>(Arrays.asList(Take, pick));

    private ETakePhotoType(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public static ETakePhotoType get(int pos) {
        return takePhotoTypeList.get(pos);
    }

    public String getTitle() {
        return title;
    }
}
