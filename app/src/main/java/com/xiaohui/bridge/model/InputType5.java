package com.xiaohui.bridge.model;

/**
 * Created by Administrator on 2014/10/18.
 */
public class InputType5 extends BaseInputModel{
    public InputType5(String position, String description, String imageNumber){
        this.position = position;
        this.description = description;
        this.imageNumber = imageNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String position;
    private String description;
}
