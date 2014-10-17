package com.xiaohui.bridge.business.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 */
public enum EDiseaseInputMethod implements Serializable {
    One(0, "方法一"),
    Two(1, "方法二"),
    Three(2, "方法三"),
    Four(3, "方法四"),
    Five(4, "方法五");

    private int id;
    private String title;

    private EDiseaseInputMethod(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }
}
