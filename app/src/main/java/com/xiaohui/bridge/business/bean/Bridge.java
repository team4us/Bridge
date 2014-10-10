package com.xiaohui.bridge.business.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohui on 14-9-26.
 */
public class Bridge {

    private String code; //桥梁编号
    private String name; //桥梁名称
    private String category; //按跨径分类
    private String maintainType; //养护类别
    private String maintainLevel; //养护等级
    private String createTime; //建造年月
    private String designer; //设计单位
    private String builder; //施工单位
    private String load; //设计荷载
    private int count; //分幅数
    private String decription; //分幅信息
    private List<ChildBridge> childBridges; //分幅桥

    public Bridge(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getCategory() {
        return category;
    }

    public String getMaintainType() {
        return maintainType;
    }

    public String getMaintainLevel() {
        return maintainLevel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getDesigner() {
        return designer;
    }

    public String getBuilder() {
        return builder;
    }

    public String getLoad() {
        return load;
    }

    public int getCount() {
        return count;
    }

    public String getDecription() {
        return decription;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setMaintainType(String maintainType) {
        this.maintainType = maintainType;
    }

    public void setMaintainLevel(String maintainLevel) {
        this.maintainLevel = maintainLevel;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public void setBuilder(String builder) {
        this.builder = builder;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public void addChildBridge(ChildBridge childBridge) {
        if (childBridges == null) {
            childBridges = new ArrayList<ChildBridge>();
        }

        childBridges.add(childBridge);
    }

    public List<ChildBridge> getChildBridges() {
        return childBridges;
    }

    @Override
    public String toString() {
        return "Bridge{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", maintainType='" + maintainType + '\'' +
                ", maintainLevel='" + maintainLevel + '\'' +
                ", createTime='" + createTime + '\'' +
                ", designer='" + designer + '\'' +
                ", builder='" + builder + '\'' +
                ", load='" + load + '\'' +
                ", count=" + count +
                ", decription='" + decription + '\'' +
                ", childBridges=" + childBridges +
                '}';
    }
}
