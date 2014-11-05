package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaohui.bridge.BuildConfig;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.bean.Component;
import com.xiaohui.bridge.business.bean.Disease;
import com.xiaohui.bridge.business.enums.EDiseaseMethod;
import com.xiaohui.bridge.component.PickPicture.FileUtils;
import com.xiaohui.bridge.component.PickPicture.TestPicActivity;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.model.DiseaseModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.view.IDiseaseView;
import com.xiaohui.bridge.viewmodel.DiseaseViewModel;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by xhChen on 14/10/29.
 */
public class DiseaseActivity extends AbstractOrmLiteActivity<DatabaseHelper> implements IDiseaseView {

    public static final int MODE_NEW = 0;
    public static final int MODE_EDIT = 1;
    public static final int MODE_CHECK = 2;

    private RadioGroup rgMethods;
    private LinearLayout llMethodView;
    private Spinner spLocations;
    private Spinner spDiseaseType;
    private View methodView;
    private EditText etComment;
    private EditText etOther;
    private DiseaseViewModel viewModel;
    private EDiseaseMethod currentMethod;
    private DiseaseModel diseaseModel;
    private Map<String, String> methodValues;
    private List<String> pictureList;
    private List<String> voiceList;
    private List<String> videoList;
    private int mode;
    private boolean isOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = getIntent().getIntExtra(Keys.MODE, MODE_NEW);
        viewModel = new DiseaseViewModel(this, getCookie());
        setContentView(R.layout.activity_disease, viewModel);
        initViews();
        if (mode == MODE_NEW) {
            setTitle("病害新增");
            diseaseModel = new DiseaseModel();
            diseaseModel.setComponent((ComponentModel) getCookie().get(Keys.COMPONENT));
            viewModel.onItemClickDiseaseType(0);
            methodValues = new HashMap<String, String>();
            pictureList = new ArrayList<String>();
            voiceList = new ArrayList<String>();
            videoList = new ArrayList<String>();
        } else {
            setTitle("病害编辑");
            fillData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCookie().remove(Keys.DISEASE);
    }

    @Override
    public void updateMethodView(boolean isOther, int method) {
        this.isOther = isOther;
        etOther.setVisibility(isOther ? View.VISIBLE : View.GONE);
        int radioButtonId = -1;
        for (int i = 0; i < 6; i++) {
            EDiseaseMethod diseaseMethod = EDiseaseMethod.values()[i];
            RadioButton rb = (RadioButton) findViewById(diseaseMethod.getRadioButtonResId());
            if ((method & (1 << i)) == 0) {
                rb.setVisibility(View.GONE);
            } else {
                rb.setVisibility(View.VISIBLE);
                if (isOther) {
                    rb.setText("方法" + (i + 1));
                    radioButtonId = R.id.rb_method_5;
                } else {
                    rb.setText(diseaseMethod.getNameResId());
                    if (radioButtonId == -1) {
                        radioButtonId = diseaseMethod.getRadioButtonResId();
                    }
                }
            }
        }

        setDefaultMethod(radioButtonId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.disease_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_disease_save) {
            save();
        } else if (id == R.id.action_disease_cancel) {
            cancel();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void takePhoto() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String currentTakePictureName = "Picture-" + df.format(new Date()) + ".jpg";

        File pictureSaveDir = new File(FileUtils.PICTURE_PATH);

        if (!pictureSaveDir.exists()) {
            if (!pictureSaveDir.mkdirs()) {
                Toast.makeText(this, "创建图片文档目录失败", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        File file = new File(FileUtils.PICTURE_PATH + currentTakePictureName);
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

    private void fillData() {
        diseaseModel = (DiseaseModel) getCookie().get(Keys.DISEASE);
        Disease disease = diseaseModel.getDisease();

        int index = viewModel.indexWithLocation(disease.getLocation());
        spLocations.setSelection(index);
        viewModel.onItemClickLocation(index);

        index = viewModel.indexWithType(disease.getType());
        spDiseaseType.setSelection(index);
        viewModel.onItemClickDiseaseType(index);
        if (isOther) {
            etOther.setText(disease.getType());
        }

        methodView = switchMethodView(EDiseaseMethod.valueOf(disease.getMethod()));
        int count = currentMethod.getCount();
        methodValues = disease.getValues();
        for (int i = 0; i < count; i++) {
            TextView textView = (TextView) methodView.findViewById(getResources().getIdentifier("tv_" + i, "id", BuildConfig.PACKAGE_NAME));
            String key = textView.getText().toString();
            String value = methodValues.get(key);

            EditText editText = (EditText) methodView.findViewById(getResources().getIdentifier("et_" + i, "id", BuildConfig.PACKAGE_NAME));
            editText.setText(value);
        }

        etComment.setText(disease.getComment());
    }

    private void initViews() {
        for (int i = 0; i < 6; i++) {
            EDiseaseMethod diseaseMethod = EDiseaseMethod.values()[i];
            RadioButton rb = (RadioButton) findViewById(diseaseMethod.getRadioButtonResId());
            rb.setTag(diseaseMethod);
        }

        rgMethods = (RadioGroup) findViewById(R.id.rg_methods);
        llMethodView = (LinearLayout) findViewById(R.id.ll_method_view);
        etComment = (EditText) findViewById(R.id.et_comment);
        etOther = (EditText) findViewById(R.id.et_other);
        initListener();

    }

    private void initListener() {
        spLocations = (Spinner) findViewById(R.id.sp_locations);
        spLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.onItemClickLocation(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDiseaseType = (Spinner) findViewById(R.id.sp_disease_type);
        spDiseaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.onItemClickDiseaseType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rgMethods.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switchMethodView(checkedId);
            }
        });
    }

    private void setDefaultMethod(int radioButtonId) {
        rgMethods.check(radioButtonId);
        switchMethodView(radioButtonId);
    }

    private View switchMethodView(int radioButtonId) {
        Object tag = findViewById(radioButtonId).getTag();
        return switchMethodView((EDiseaseMethod) tag);
    }

    private View switchMethodView(EDiseaseMethod method) {
        currentMethod = method;
        llMethodView.removeAllViews();
        View view = View.inflate(this, currentMethod.getViewResId(), null);
        llMethodView.addView(view);
        return view;
    }

    private void save() {
        Disease disease = new Disease();
        disease.setLocation(viewModel.getLocation());
        if (isOther && !TextUtils.isEmpty(etOther.getText())) {
            disease.setType(etOther.getText().toString());
        } else {
            disease.setType(viewModel.getType());
        }
        disease.setMethod(currentMethod.toString());
        disease.setComment(etComment.getText().toString());
        methodView = switchMethodView(EDiseaseMethod.valueOf(disease.getMethod()));
        int count = currentMethod.getCount();
        for (int i = 0; i < count; i++) {
            TextView textView = (TextView) methodView.findViewById(getResources().getIdentifier("tv_" + i, "id", BuildConfig.PACKAGE_NAME));
            EditText editText = (EditText) methodView.findViewById(getResources().getIdentifier("et_" + i, "id", BuildConfig.PACKAGE_NAME));
            methodValues.put(textView.getText().toString(), editText.getText().toString());
        }
        disease.setValues(methodValues);
        disease.setPictureList(pictureList);
        disease.setVideoList(videoList);
        disease.setVoiceList(voiceList);
        diseaseModel.setDisease(disease);

        try {
            getHelper().getDiseaseDao().createOrUpdate(diseaseModel);
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            this.finish();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancel() {
        finish();
    }

    private int random() {
        return 1 + (int) (Math.random() * 15);
    }
}
