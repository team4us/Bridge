package com.xiaohui.bridge.business.bean;

/**
 * Created by xiaohui on 14-9-26.
 */
public class ChildBridge {

    private String name; //桥梁名称
    private String category; //按跨径分类

    public ChildBridge(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
