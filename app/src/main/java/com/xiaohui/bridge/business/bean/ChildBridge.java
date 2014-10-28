package com.xiaohui.bridge.business.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaohui on 14-9-26.
 */
public class ChildBridge implements Serializable {

    private String name; //桥梁名称
    private String category; //按跨径分类
    private String count; //桥梁跨数
    private String combination; //跨径组合
    private String length; //桥梁总长
    private String width; //桥梁总宽
    private List<List<Component>> components; //构建列表

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCombination() {
        return combination;
    }

    public void setCombination(String combination) {
        this.combination = combination;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public List<List<Component>> getComponents() {
        return components;
    }

    public void setComponents(List<List<Component>> components) {
        this.components = components;
    }
}
