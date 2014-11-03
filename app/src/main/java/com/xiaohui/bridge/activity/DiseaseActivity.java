package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.BusinessManager;
import com.xiaohui.bridge.component.PickPicture.Bimp;
import com.xiaohui.bridge.component.PickPicture.TestPicActivity;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.view.IDiseaseView;
import com.xiaohui.bridge.viewmodel.DiseaseViewModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xhChen on 14/10/29.
 */
public class DiseaseActivity extends AbstractOrmLiteActivity<DatabaseHelper> implements IDiseaseView {

    public static final String PICTURE_PATH = BusinessManager.USER_MEDIA_FILE_PATH + "Picture/";

    private DiseaseViewModel viewModel;
    private boolean isNewDisease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isNewDisease = getIntent().getBooleanExtra(Keys.FLAG, true);
        viewModel = new DiseaseViewModel(this, getCookie());
        setContentView(R.layout.activity_disease, viewModel);
        setTitle(isNewDisease ? "病害新增" : "病害编辑");
    }

    @Override
    public void takePhoto() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String currentTakePictureName = "Picture-" + df.format(new Date()) + ".jpg";

        File pictureSaveDir = new File(PICTURE_PATH);

        if (!pictureSaveDir.exists()) {
            if (!pictureSaveDir.mkdirs()) {
                Toast.makeText(this, "创建图片文档目录失败", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        File file = new File(PICTURE_PATH + currentTakePictureName);
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, Keys.RequestCodeTakePicture);
    }

    @Override
    public void selectPictures() {
        Intent intent = new Intent(this, TestPicActivity.class);
        startActivityForResult(intent, Keys.RequestCodePickPicture);
    }

    @Override
    public void takeVoice() {
        Intent intent = new Intent(this, VoiceRecordActivity.class);
        startActivityForResult(intent, Keys.RequestCodeTakeRecord);
    }

    @Override
    public void takeMovie() {
        Intent intent = new Intent(this, MovieRecordActivity.class);
        startActivityForResult(intent, Keys.RequestCodeTakeMovie);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        switch (requestCode) {
//            case Keys.RequestCodeTakePicture:
//                if (Bimp.drr.size() < 9 && resultCode == RESULT_OK) {
//                    picturesList.clear();
//                    Bimp.drr.add(path);
//                    for (int i = 0; i < Bimp.drr.size(); i++) {
//                        picturesList.add(Bimp.drr.get(i));
//                    }
//                }
//                break;
//            case Keys.RequestCodeTakeRecord:
//                if (resultCode == Keys.ResultCodeSuccess && null != data && null != data.getExtras()) {
//                    String recordPath = data.getExtras().getString(Keys.KeyContent);
//                    if (!TextUtils.isEmpty(recordPath)) {
//                        addMediaFile(recordPath, true);
//                    }
//                }
//                break;
//
//            case Keys.RequestCodeTakeMovie:
//                if (resultCode != Keys.ResultCodeSuccess || null == data.getExtras()) {
//                    return;
//                }
//                String videoPath = data.getExtras().getString(Keys.KeyContent);
//                if (!TextUtils.isEmpty(videoPath)) {
//                    addMediaFile(videoPath, false);
//                }
//                break;
//            case Keys.RequestCodeCoordinate:
//                View view = inputTemplateView.findViewById(R.id.et_startpoint);
//                if (view != null) {
//
//                    String startPoint = random() + "," + random();
//                    ((EditText) view).setText(startPoint);
//
//                    EditText editText = (EditText) inputTemplateView.findViewById(R.id.et_endpoint);
//                    String endPoint = random() + "," + random();
//                    editText.setText(endPoint);
//                } else {
//                    view = inputTemplateView.findViewById(R.id.et_position);
//                    if (view != null) {
//                        EditText editText = (EditText) view;
//                        String endPoint = random() + "," + random();
//                        editText.setText(endPoint);
//                    }
//                }
//                break;
//        }
    }

    private int random() {
        return 1 + (int)(Math.random()*15);
    }
}
