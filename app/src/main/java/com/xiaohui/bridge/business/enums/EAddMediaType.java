package com.xiaohui.bridge.business.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 */
public enum EAddMediaType implements Serializable {
    Take(0, "拍照"),
    pick(1, "从相册选择"),
    Record(2, "录音"),
    Movie(3, "摄像"), ;

    private int id;
    private String title;

    public final static ArrayList<EAddMediaType> takeMediaTypeList
            = new ArrayList<EAddMediaType>(Arrays.asList(Take, pick, Record, Movie));

    public static ArrayList<EAddMediaType> getMediaTypeList() {
        return takeMediaTypeList;
    }

    private EAddMediaType(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public static EAddMediaType get(int pos) {
        return takeMediaTypeList.get(pos);
    }

    public String getTitle() {
        return title;
    }
}
