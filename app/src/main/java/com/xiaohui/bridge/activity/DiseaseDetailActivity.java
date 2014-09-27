package com.xiaohui.bridge.activity;

import android.os.Bundle;

import com.xiaohui.bridge.R;

/**
 * 病害详情界面
 * Created by Administrator on 2014/9/27.
 */
public class DiseaseDetailActivity extends AbstractActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);
        setTitle("病害录入");
    }
}
