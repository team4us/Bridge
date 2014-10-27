package com.xiaohui.bridge.model;

import com.xiaohui.bridge.business.enums.EDiseaseInputMethod;

import java.util.ArrayList;
import java.util.Map;

/**
 * 病害类
 * Created by Administrator on 2014/10/12.
 */
public class DiseasesModel {
    private String componentName;
    private String position;
    private String diseaseType;
    private EDiseaseInputMethod inputMethod;
    private Map<String, Object> inputMethodValues;
//    private BaseInputModel diseaseInputMethod;
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

//    public void setDiseaseInputMethod(BaseInputModel diseaseInputMethod) {
//        this.diseaseInputMethod = diseaseInputMethod;
//    }

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

//    public BaseInputModel getDiseaseInputMethod() {
//        return diseaseInputMethod;
//    }

    public Map<String, Object> getInputMethodValues() {
        return inputMethodValues;
    }

    public void setInputMethodValues(Map<String, Object> inputMethodValues) {
        this.inputMethodValues = inputMethodValues;
    }

    public EDiseaseInputMethod getInputMethod() {
        return inputMethod;
    }

    public void setInputMethod(EDiseaseInputMethod inputMethod) {
        this.inputMethod = inputMethod;
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

    public boolean isHaveEmptyData(){
        for(int i = 0; i < getInputMethod().getInputTitles().length; i ++){
            if(!getInputMethod().getInputTitles()[i].equals("moreinfo") &&
                    ((String)getInputMethodValues().get(getInputMethod().getInputTitles()[i])).isEmpty()){
                return true;
            }
        }
        return false;
    }
}
