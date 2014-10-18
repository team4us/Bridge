package com.xiaohui.bridge.business.enums;

import java.io.Serializable;

/**
 */
public enum EDiseaseInputMethod implements Serializable {
    One(1, "方法1"),
    Two(2, "方法2"),
    Three(3, "方法3"),
    Four(4, "方法4"),
    Five(5, "方法5");

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
