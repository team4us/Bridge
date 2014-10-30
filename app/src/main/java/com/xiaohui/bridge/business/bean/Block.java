package com.xiaohui.bridge.business.bean;

import java.io.Serializable;

/**
 * Created by xiaohui on 14-10-28.
 */
public class Block implements Serializable {
    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
