package com.xiaohui.bridge.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.BusinessManager;
import com.xiaohui.bridge.model.UserModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.util.FileOperateUtil;
import com.xiaohui.bridge.view.ISettingsView;
import com.xiaohui.bridge.viewmodel.SettingsViewModel;

import java.io.File;
import java.text.DecimalFormat;

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
        new GetDataSizeTask().execute("");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("将会删除所有此用户下的记录数据和多媒体文件，确定继续吗？").setTitle("警告")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        viewModel.startClear();
                        new ClearDataTask().execute("");
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
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
        protected void onPostExecute(String result) {
            viewModel.downloadSuccess();
        }
    }

    private class GetDataSizeTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            long mediaSize = 0;
            try {
                mediaSize = FileOperateUtil.getFolderSize(getGlobalApplication().getCachePath());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mediaSize < 1) {
                return "";
            }

            double mbSize = mediaSize / 1024.0f;
            // 如果缓存中是有数据的，并且大小小于0.1mb，那么直接显示0.1mb
            if (mediaSize > 0 && mbSize < 1) {
                return "0.1MB";
            }

            // 需要使用MB来显示
            if (mbSize / 1024.0f > 1) {
                DecimalFormat df = new DecimalFormat("0.0");
                return df.format(mbSize / 1024.0f) + "MB";
            } else {
                DecimalFormat df = new DecimalFormat("0.0");
                return df.format(mbSize) + "KB";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            viewModel.setAllFileSize(result);
        }
    }

    private class ClearDataTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO 还要删除数据库中的文件
            FileOperateUtil.deleteAllFiles(BusinessManager.USER_MEDIA_FILE_PATH);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            viewModel.clearSuccess();
        }
    }
}
