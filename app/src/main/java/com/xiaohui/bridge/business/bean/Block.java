package com.xiaohui.bridge.business.bean;

import java.io.Serializable;

/**
 * Created by xiaohui on 14-10-28.
 */
public class Block implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
