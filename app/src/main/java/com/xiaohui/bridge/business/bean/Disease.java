package com.xiaohui.bridge.business.bean;

import com.xiaohui.bridge.business.enums.EDiseaseInputMethod;

/**
 * Created by xiaohui on 14-10-17.
 */
public class Disease {
    private String location; //部位
    private String start; //起点位置，当只记录一个点时，记录在起点位置
    private String end; //终点位置
    private float length; //长度
    private float width; //宽度
    private int mediaCount; //多媒体个数
    private EDiseaseInputMethod inputMethod; //记录方式

    public String getLocation() {
        return location;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public float getLength() {
        return length;
    }

    public float getWidth() {
        return width;
    }

    public int getMediaCount() {
        return mediaCount;
    }

    public EDiseaseInputMethod getInputMethod() {
        return inputMethod;
    }
}
