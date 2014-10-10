package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.bean.Project;
import com.xiaohui.bridge.view.IProjectView;
import com.xiaohui.bridge.viewmodel.ProjectsViewModel;

/**
 * Created by xhChen on 14/9/27.
 */
public class ProjectsActivity extends AbstractActivity implements IProjectView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeContentView(R.layout.activity_projects, new ProjectsViewModel(this));
        setTitle("项目列表");
    }

    @Override
    public void onItemSelect(int position, Project project) {
        getCookie().put(Keys.PROJECT, project);
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
            Toast.makeText(this, R.string.action_download, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_upload) {
            Toast.makeText(this, R.string.action_upload, Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
