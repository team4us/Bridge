package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.business.store.StoreManager;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.storage.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 病害列表界面
 * Created by jztang on 2014/9/26.
 */
public class DiseaseListActivity extends AbstractOrmLiteActivity implements AdapterView.OnItemClickListener {
    private ListView diseaseListView;
    private String componentName;
    private String positionName;
    private int longClickPosition;
    private ComponentModel componentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_list);

        componentModel = (ComponentModel) getCookie().get(Keys.COMPONENT);
        
        StoreManager.Instance.initDiseasesModelList(componentName, positionName);

        componentName = getIntent().getExtras().getString(Keys.KeySelectedComponentName);
        positionName = getIntent().getExtras().getString(Keys.KeySelectedPositionName);

        setTitle(getIntent().getStringExtra("title") + "病害列表");

        diseaseListView = (ListView) findViewById(R.id.lv_disease);
        diseaseListView.setAdapter(new ArrayAdapter<String>(this, R.layout.view_disease_item, getData()));
        diseaseListView.setOnItemClickListener(this);
        registerForContextMenu(diseaseListView);
    }

    private List<String> getData() {

        List<String> data = new ArrayList<String>();
        for (int i = 0; i < StoreManager.Instance.getDiseasesList().size(); i++) {
            data.add((i + 1) + " " + StoreManager.Instance.getDiseasesList().get(i).getDiseaseType());
        }
        return data;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_copy:
                StoreManager.Instance.addDiseaseModel(StoreManager.Instance.getDiseasesList().get(longClickPosition));
                break;
            case R.id.action_delete:
                StoreManager.Instance.getDiseasesList().remove(longClickPosition);
                break;
        }
        diseaseListView.setAdapter(new ArrayAdapter<String>(this, R.layout.view_disease_item, getData()));
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        longClickPosition = (int) info.id;
        menu.setHeaderTitle(getString(R.string.action_operate_tips));
        getMenuInflater().inflate(R.menu.diseaselist_menu, menu);
    }

    @Override
    public void finish() {
        StoreManager.Instance.clearDiseasesList();
        super.finish();
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
            Intent intent = new Intent(this, DiseaseDetailActivity.class);
            intent.putExtra(Keys.FLAG, true); //是否为新增
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
        } else if (id == R.id.action_disease_statistics) {
            Intent intent = new Intent(this, DiseaseStatisticsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        diseaseListView.setAdapter(new ArrayAdapter<String>(this, R.layout.view_disease_item, getData()));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, DiseaseDetailActivity.class);
        intent.putExtra(Keys.FLAG, false); //是否为新增
        intent.putExtra(Keys.KeySelectedIndex, position);
        intent.putExtras(getIntent().getExtras());
        startActivity(intent);
    }

}
