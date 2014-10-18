package com.xiaohui.bridge.model;

/**
 * Created by Administrator on 2014/10/18.
 */
public class InputType3 extends BaseInputModel{
    public InputType3(String position, String length, String typicalWidth, String imageNumber){
        this.position = position;
        this.length = length;
        this.typicalWidth = typicalWidth;
        this.imageNumber = imageNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return typicalWidth;
    }

    public void setWidth(String width) {
        this.typicalWidth = width;
    }

    private String position;
    private String length;
    private String typicalWidth;

}
