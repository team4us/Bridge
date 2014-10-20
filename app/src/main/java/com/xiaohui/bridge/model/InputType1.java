package com.xiaohui.bridge.model;

/**
 * Created by Administrator on 2014/10/18.
 */
public class InputType1 extends BaseInputModel{
    public InputType1(String startPosition, String endPosition, String length, String typicalWidth, String imageNumber){
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.length = length;
        this.typicalWidth = typicalWidth;
        this.imageNumber = imageNumber;
        this.more = "";
    }

    public InputType1(String startPosition, String endPosition, String length, String typicalWidth, String imageNumber, String more){
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.length = length;
        this.typicalWidth = typicalWidth;
        this.imageNumber = imageNumber;
        this.more = more;
    }


    public String getStartPosition() {
        return startPosition;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public String getLength() {
        return length;
    }

    public String getWidth() {
        return typicalWidth;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setWidth(String width) {
        this.typicalWidth = width;
    }

    private String startPosition;
    private String endPosition;
    private String length;
    private String typicalWidth;
}
