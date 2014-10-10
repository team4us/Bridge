package com.xiaohui.bridge.business.bean;

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
    private String count; //分幅数
    private String decription; //分幅信息

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

    public String getCount() {
        return count;
    }

    public String getDecription() {
        return decription;
    }

}
