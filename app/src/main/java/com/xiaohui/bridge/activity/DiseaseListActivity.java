package com.xiaohui.bridge.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xiaohui.bridge.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 病害列表界面
 * Created by jztang on 2014/9/26.
 */
public class DiseaseListActivity extends AbstractActivity implements AdapterView.OnItemClickListener{
    private ListView diseaseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseaselist);

        setTitle("病害列表");

        diseaseListView = (ListView) findViewById(R.id.lv_disease);
        diseaseListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData()));
        diseaseListView.setOnItemClickListener(this);
    }

    private List<String> getData() {

        List<String> data = new ArrayList<String>();
        data.add("裂缝");
        data.add("破损");
        data.add("裂缝");
        data.add("钢筋锈蚀");
        data.add("破损");
        data.add("钢筋锈蚀");

        return data;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

    }
}
