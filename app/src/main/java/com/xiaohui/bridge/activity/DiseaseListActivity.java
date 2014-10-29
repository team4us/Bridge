package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.model.DiseaseModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.view.IDiseaseView;
import com.xiaohui.bridge.viewmodel.DiseasesViewModel;

import java.sql.SQLException;

/**
 * 病害列表界面
 * Created by jztang on 2014/9/26.
 */
public class DiseaseListActivity extends AbstractOrmLiteActivity<DatabaseHelper> implements IDiseaseView {
    private DiseasesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ComponentModel componentModel = (ComponentModel) getCookie().get(Keys.COMPONENT);
        viewModel = new DiseasesViewModel(this, getCookie(), getHelper());
        setContentView(R.layout.activity_disease_list, viewModel);
        setTitle(componentModel.getComponent().getName() + "病害列表");
        registerForContextMenu(findViewById(R.id.lv_disease));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = (int) info.id;
        switch (item.getItemId()) {
            case R.id.action_copy:
                viewModel.copyOneDiseaseWithPos(pos);
                break;
            case R.id.action_delete:
                viewModel.deleteOneDiseaseWithPos(pos);
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(getString(R.string.action_operate_tips));
        getMenuInflater().inflate(R.menu.diseaselist_menu, menu);
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
    protected void onResume() {
        viewModel.updateData();
        super.onResume();
    }

    @Override
    public void onItemSelect(int position, DiseaseModel project) {
        Intent intent = new Intent(this, DiseaseDetailActivity.class);
        intent.putExtra(Keys.FLAG, false); //是否为新增
        startActivity(intent);
    }
}
