package com.xiaohui.bridge.model;

/**
 * Created by Administrator on 2014/10/18.
 */
public class InputType2 extends BaseInputModel{
    public InputType2(String positionCoordinate, String length, String width, String imageNumber){
        this.positionCoordinate = positionCoordinate;
        this.length = length;
        this.width = width;
        this.imageNumber = imageNumber;
        this.more = "";
    }

    public InputType2(String positionCoordinate, String length, String width, String imageNumber, String more){
        this.positionCoordinate = positionCoordinate;
        this.length = length;
        this.width = width;
        this.imageNumber = imageNumber;
        this.more = more;
    }

    public String getPositionCoordinate() {
        return positionCoordinate;
    }

    public void setPositionCoordinate(String positionCoordinate) {
        this.positionCoordinate = positionCoordinate;
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

    private String positionCoordinate;
    private String length;
    private String width;

}
