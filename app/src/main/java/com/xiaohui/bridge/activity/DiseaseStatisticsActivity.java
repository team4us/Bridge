package com.xiaohui.bridge.activity;

import android.os.Bundle;

import com.xiaohui.bridge.R;

/**
 * 病害列表界面
 * Created by jztang on 2014/9/26.
 */
public class DiseaseStatisticsActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_statistics);
        setTitle("病害统计");
    }

}

