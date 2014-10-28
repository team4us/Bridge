package com.xiaohui.bridge.business.enums;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.XhApplication;

import java.io.Serializable;

/**
 */
public enum EDiseaseInputMethod implements Serializable {
    One(1, R.layout.view_disease_input_1, getKeys(R.array.disease_input_type1)),
    Two(2, R.layout.view_disease_input_2, getKeys(R.array.disease_input_type2)),
    Three(3, R.layout.view_disease_input_3, getKeys(R.array.disease_input_type3)),
    Four(4, R.layout.view_disease_input_4, getKeys(R.array.disease_input_type4)),
    Five(5, R.layout.view_disease_input_5, getKeys(R.array.disease_input_type5));

    private int id;
    private int resid;
    private String[] inputTitles;

    private EDiseaseInputMethod(int id, int resid, String[] inputTitles) {
        this.id = id;
        this.resid = resid;
        this.inputTitles = inputTitles;
    }

    public String[] getInputTitles() {
        return inputTitles;
    }

    public int getId() {
        return id;
    }

    public int getResID(){
        return resid;
    }

    private static String[] getKeys(int resid){
        return XhApplication.getApplication().getResources().getStringArray(resid);
    }

}
