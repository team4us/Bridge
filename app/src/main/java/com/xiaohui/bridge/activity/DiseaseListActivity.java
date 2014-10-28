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
import com.xiaohui.bridge.model.DiseaseModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.view.IDiseaseView;
import com.xiaohui.bridge.viewmodel.DiseasesViewModel;
import com.xiaohui.bridge.viewmodel.ProjectsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 病害列表界面
 * Created by jztang on 2014/9/26.
 */
public class DiseaseListActivity extends AbstractOrmLiteActivity<DatabaseHelper> implements IDiseaseView {
    private int longClickPosition;
    private ComponentModel componentModel;
    private DiseasesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        componentModel = (ComponentModel) getCookie().get(Keys.COMPONENT);
        viewModel = new DiseasesViewModel(this, getCookie(), componentModel);
        setContentView(R.layout.activity_disease_list, viewModel);
        setTitle(componentModel.getComponent().getName() + "病害列表");
        ListView diseaseListView = (ListView) findViewById(R.id.lv_disease);
        registerForContextMenu(diseaseListView);
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
            startActivity(intent);
        } else if (id == R.id.action_disease_statistics) {
            Intent intent = new Intent(this, DiseaseStatisticsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelect(int position, DiseaseModel project) {
        Intent intent = new Intent(this, DiseaseDetailActivity.class);
        intent.putExtra(Keys.FLAG, false); //是否为新增
        startActivity(intent);
    }
}
