package com.xiaohui.bridge.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.xiaohui.bridge.R;

/**
 * 病害列表界面
 * Created by jztang on 2014/9/26.
 */
public class DiseaseListActivity extends AbstractActivity{
    private ListView diseaseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseaselist);

        diseaseListView = (ListView) findViewById(R.id.lv_disease);
    }
}
