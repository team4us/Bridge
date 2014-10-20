package com.xiaohui.bridge.model;

import java.util.ArrayList;

/**
 * 病害类
 * Created by Administrator on 2014/10/12.
 */
public class DiseasesModel {
    private String componentName;
    private String position;
    private String diseaseType;
    private BaseInputModel diseaseInputMethod;
    private ArrayList<String> pictureList = new ArrayList<String>();
    private ArrayList<String> recordList = new ArrayList<String>();
    private ArrayList<String> videoList = new ArrayList<String>();

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public void setDiseaseInputMethod(BaseInputModel diseaseInputMethod) {
        this.diseaseInputMethod = diseaseInputMethod;
    }

    public void setPictureList(ArrayList<String> pictureList) {
        this.pictureList = pictureList;
    }

    public void setRecordList(ArrayList<String> recordList) {
        this.recordList = recordList;
    }

    public void setVideoList(ArrayList<String> videoList) {
        this.videoList = videoList;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getPosition() {
        return position;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public BaseInputModel getDiseaseInputMethod() {
        return diseaseInputMethod;
    }

    public ArrayList<String> getPictureList() {
        return pictureList;
    }

    public ArrayList<String> getRecordList() {
        return recordList;
    }

    public ArrayList<String> getVideoList() {
        return videoList;
    }
}
