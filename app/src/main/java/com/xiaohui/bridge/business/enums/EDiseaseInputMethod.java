package com.xiaohui.bridge.business.enums;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.XhApplication;

import java.io.Serializable;

/**
 */
public enum EDiseaseInputMethod implements Serializable {
    One(1, getKeys(R.array.disease_input_type1)),
    Two(2, getKeys(R.array.disease_input_type2)),
    Three(3, getKeys(R.array.disease_input_type3)),
    Four(4, getKeys(R.array.disease_input_type4)),
    Five(5, getKeys(R.array.disease_input_type5));

    private int id;
    private String[] inputTitles;

    private EDiseaseInputMethod(int id, String[] inputTitles) {
        this.id = id;
        this.inputTitles = inputTitles;
    }

    public String[] getInputTitles() {
        return inputTitles;
    }

    public int getId() {
        return id;
    }

    private static String[] getKeys(int resid){
        return XhApplication.getApplication().getResources().getStringArray(resid);
    }

}
