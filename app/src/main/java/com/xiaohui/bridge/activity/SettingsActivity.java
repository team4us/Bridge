package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.BusinessManager;
import com.xiaohui.bridge.model.UserModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.view.ISettingsView;
import com.xiaohui.bridge.viewmodel.SettingsViewModel;

/**
 * Created by xiaohui on 14-10-27.
 */
public class SettingsActivity extends AbstractOrmLiteActivity<DatabaseHelper> implements ISettingsView {

    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new SettingsViewModel(this, getCookie(), getHelper());
        setContentView(R.layout.activity_settings, viewModel);
        setTitle(R.string.action_settings);
    }

    @Override
    public void onLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onUpload() {

    }

    @Override
    public void onDownload() {
        new BusinessTask().execute();
    }

    @Override
    public void onClear() {

    }

    private class BusinessTask extends AsyncTask<String, Integer, String> {

        private final BusinessManager businessManager = new BusinessManager();

        @Override
        protected void onPreExecute() {
            viewModel.downloadStart();
        }

        @Override
        protected String doInBackground(String... params) {
            businessManager.download(getHelper(), (UserModel) getCookie().get(Keys.USER));
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progresses) {

        }

        @Override
        protected void onPostExecute(String result) {
            viewModel.downloadSuccess();
        }

        @Override
        protected void onCancelled() {

        }
    }
}
