package com.xiaohui.bridge.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.model.ProjectModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.view.IProjectView;
import com.xiaohui.bridge.viewmodel.ProjectsViewModel;

/**
 * Created by xhChen on 14/9/27.
 */
public class ProjectsActivity extends AbstractOrmLiteActivity<DatabaseHelper> implements IProjectView {

    private ProjectsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ProjectsViewModel(this, getHelper(), getGlobalApplication().getCurrentUserName());
        setContentView(R.layout.activity_projects, viewModel);
        setTitle("项目列表");
        ListView lv = (ListView) findViewById(R.id.lv_projects);
        registerForContextMenu(lv);
    }

    @Override
    public void onItemSelect(int position, ProjectModel project) {
        getCookie().put(Keys.PROJECT, project.getProject());
        startActivity(new Intent(this, BridgeActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.project_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_download) {
            viewModel.download();
            return true;
        } else if (id == R.id.action_upload) {
            viewModel.upload();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ProjectModel projectModel = viewModel.getProject((int) info.id);
        menu.setHeaderTitle(projectModel.getProject().getName());
        getMenuInflater().inflate(R.menu.projects_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ProjectModel projectModel = viewModel.getProject((int) info.id);
        switch (item.getItemId()) {
            case R.id.action_upload:
                break;
            case R.id.action_download:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认退出吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ProjectsActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
