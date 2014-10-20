package com.xiaohui.bridge.model;

/**
 * Created by Administrator on 2014/10/18.
 */
public class InputType4 extends BaseInputModel{
    public InputType4(String position, String length, String width, String imageNumber){
        this.position = position;
        this.length = length;
        this.width = width;
        this.imageNumber = imageNumber;
        this.more = "";
    }

    public InputType4(String position, String length, String width, String imageNumber, String more){
        this.position = position;
        this.length = length;
        this.width = width;
        this.imageNumber = imageNumber;
        this.more = more;
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
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    private String position;
    private String length;
    private String width;

}
