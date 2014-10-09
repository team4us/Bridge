package com.xiaohui.bridge.activity;

import android.os.Bundle;

import com.xiaohui.bridge.R;

/**
 * 病害录入模板一界面
 * Created by Administrator on 2014/9/27.
 */
public class DiseaseDetailOneActivity extends BaseDiseaseDetailActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_disease_detail_one);
        super.onCreate(savedInstanceState);
        setTitle("病害录入模板一");
    }
}
