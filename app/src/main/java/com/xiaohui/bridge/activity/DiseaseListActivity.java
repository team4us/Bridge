package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xiaohui.bridge.Keys;
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
        setContentView(R.layout.activity_disease_list);

        setTitle("病害列表");

        diseaseListView = (ListView) findViewById(R.id.lv_disease);
        diseaseListView.setAdapter(new ArrayAdapter<String>(this, R.layout.view_disease_item, getData()));
        diseaseListView.setOnItemClickListener(this);
    }

    private List<String> getData() {

        List<String> data = new ArrayList<String>();
        data.add("1 裂缝");
        data.add("2 破损");
        data.add("3 裂缝");
        data.add("4 钢筋锈蚀");
        data.add("5 破损");
        data.add("6 钢筋锈蚀");

        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.disease_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_disease_add) {
            Intent intent = new Intent(this, BaseDiseaseDetailActivity.class);
            intent.putExtra(Keys.FLAG, true); //是否为新增
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, BaseDiseaseDetailActivity.class);
        intent.putExtra(Keys.FLAG, false); //是否为新增
        startActivity(intent);
    }
}
