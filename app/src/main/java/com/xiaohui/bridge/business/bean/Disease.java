package com.xiaohui.bridge.business.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 病害类
 * Created by Administrator on 2014/10/12.
 */
public class Disease implements Serializable {
    private String location;
    private String type;
    private String method;
    private String comment;
    private Map<String, String> values;
    private List<String> pictureList;
    private List<String> voiceList;
    private List<String> videoList;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }

    public List<String> getVoiceList() {
        return voiceList;
    }

    public void setVoiceList(List<String> voiceList) {
        this.voiceList = voiceList;
    }

    public List<String> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<String> videoList) {
        this.videoList = videoList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
