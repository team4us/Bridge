package com.xiaohui.bridge.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaohui.bridge.BuildConfig;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.BusinessManager;
import com.xiaohui.bridge.business.bean.Disease;
import com.xiaohui.bridge.business.enums.EDiseaseInputMethod;
import com.xiaohui.bridge.business.store.StoreManager;
import com.xiaohui.bridge.component.MyGridView;
import com.xiaohui.bridge.component.PickPicture.Bimp;
import com.xiaohui.bridge.component.PickPicture.GridAdapter;
import com.xiaohui.bridge.component.PickPicture.PhotoActivity;
import com.xiaohui.bridge.component.PickPicture.TestPicActivity;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.model.DiseaseModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.util.DeviceParamterUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * 多种病害输入模板基类
 * Created by Administrator on 2014/10/9.
 */
public class DiseaseDetailActivity extends AbstractOrmLiteActivity<DatabaseHelper> implements View.OnClickListener {

    public static String PicturePath = BusinessManager.USER_MEDIA_FILE_PATH + "Picture/";
    private static String AddPhotoTag = "AddPhoto";
    private static String AddPictureTag = "AddPicture";
    private static String AddVoiceTag = "AddVoice";
    private static String AddVideoTag = "AddVideo";

    protected boolean isHaveTag = false;

    private LinearLayout llMediaTypes;
    private LinearLayout llVoiceRecords;
    private LinearLayout llVideoRecords;
    private Spinner spChoosePosition;
    private Spinner spChooseDiseaseType;
    private EditText etDiseaseType;
    private RadioGroup rgRadioGroup;
    private RadioButton rbRadioButton1;
    private RadioButton rbRadioButton2;
    private RadioButton rbRadioButton3;
    private RadioButton rbRadioButton4;
    private RadioButton rbRadioButton5;
    private LinearLayout llInputTemplate;
    private TextView tvComponentName;
    private View viewPictureDivider;
    private View viewVoiceDivider;
    private View viewVideoDivider;

    private GridAdapter mgvPicturesAdapter;
    private int iconWidth = DeviceParamterUtil.dip2px(60);

    private String path = "";

    private ArrayList<String> picturesList = new ArrayList<String>();
    private ArrayList<String> recordsList = new ArrayList<String>();
    private ArrayList<String> videosList = new ArrayList<String>();

    private boolean isNewDisease = false;
    private String componentName;
    private String positionName;

    private Disease diseaseDetail;
    private View inputTemplateView;
    private DiseaseModel diseaseModel;
    private ComponentModel componentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);
        isNewDisease = getIntent().getBooleanExtra(Keys.FLAG, true);
        setTitle(isNewDisease ? "病害新增" : "病害编辑");

        componentModel = (ComponentModel) getCookie().get(Keys.COMPONENT);
        componentName = componentModel.getComponent().getName();
        positionName = componentModel.getBlock().getBlock().getName();

        if (!isNewDisease) {
            diseaseModel = (DiseaseModel) getCookie().get(Keys.DISEASE);
            diseaseDetail = diseaseModel.getDisease();
            initInputTemplateView(diseaseDetail.getInputMethod().getResID());
        } else {
            diseaseModel = new DiseaseModel();
            diseaseDetail = new Disease();
            diseaseModel.setComponent(componentModel);
            initInputTemplateView(0);
        }

        initBaseView();
        initMediaResource();
        initDiseaseDetailView();
        initMediaLayout();
        initGridView();
    }

    private void initInputTemplateView(int resid) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inputTemplateView = inflater.inflate(resid == 0 ? R.layout.view_disease_input_1 : resid, null);
    }

    private void initDiseaseDetailView() {
        tvComponentName.setText(componentName);

        final ArrayAdapter<String> positions = new ArrayAdapter<String>(this, R.layout.view_spinner_item, StoreManager.Instance.generalsTypes);
        positions.setDropDownViewResource(R.layout.view_spinner_dropdown_item);
        spChoosePosition.setAdapter(positions);
        spChoosePosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                positionName = positions.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        for (int i = 0; i < StoreManager.Instance.generalsTypes.length; i++) {
            if (positionName.equals(StoreManager.Instance.generalsTypes[i])) {
                spChoosePosition.setSelection(i);
                break;
            }
        }

        String key = "disease_type_" + String.valueOf(componentModel.getBlock().getBlock().getId() + 1);
        int id = getResources().getIdentifier(key, "array", BuildConfig.PACKAGE_NAME);
        String[] diseaseTypes = getResources().getStringArray(id);

        ArrayAdapter<String> diseases = new ArrayAdapter<String>(this, R.layout.view_spinner_item, diseaseTypes);
        diseases.setDropDownViewResource(R.layout.view_spinner_dropdown_item);
        spChooseDiseaseType.setAdapter(diseases);
        spChooseDiseaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    if (isNewDisease) {
                        rbRadioButton1.setChecked(true);
                    }
                    rbRadioButton1.setVisibility(View.VISIBLE);
                    rbRadioButton1.setText("详细记录");
                    rbRadioButton3.setVisibility(View.VISIBLE);
                    rbRadioButton3.setText("统计记录");
                    rbRadioButton5.setVisibility(View.VISIBLE);
                    rbRadioButton5.setText("描述记录");
                    rbRadioButton2.setVisibility(View.GONE);
                    rbRadioButton4.setVisibility(View.GONE);
                } else {
                    if (isNewDisease) {
                        rbRadioButton2.setChecked(true);
                    }
                    rbRadioButton2.setVisibility(View.VISIBLE);
                    rbRadioButton2.setText("详细记录");
                    rbRadioButton4.setVisibility(View.VISIBLE);
                    rbRadioButton4.setText("统计记录");
                    rbRadioButton5.setVisibility(View.VISIBLE);
                    rbRadioButton5.setText("描述记录");
                    rbRadioButton1.setVisibility(View.GONE);
                    rbRadioButton3.setVisibility(View.GONE);
                }

                if (position == 3) {
                    etDiseaseType.setVisibility(View.VISIBLE);
                    etDiseaseType.setText("");

                } else {
                    etDiseaseType.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if (!isNewDisease) {
            for (int i = 0; i < StoreManager.Instance.diseaseTypes.length; i++) {
                if (diseaseDetail.getDiseaseType().equals(StoreManager.Instance.diseaseTypes[i])) {
                    spChooseDiseaseType.setSelection(i);
                    break;
                }
            }
        }

        llInputTemplate.addView(inputTemplateView);
        rgRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) DiseaseDetailActivity.this.findViewById(radioButtonId);
                llInputTemplate.removeAllViews();
                // 初始化成相应的录入模板
                EDiseaseInputMethod nowInputMethod = (EDiseaseInputMethod) rb.getTag();
                initInputTemplateView(nowInputMethod.getResID());
                llInputTemplate.addView(inputTemplateView);

                // 匹配当前的录入模板是否和原本的数据模板相同，如果相同，那么初始化视图中的数据，如果不同，什么都不做
                if (!isNewDisease && null != diseaseDetail && nowInputMethod.getId() == diseaseDetail.getInputMethod().getId()) {
                    String[] keys = nowInputMethod.getInputTitles();
                    for (int j = 0; j < keys.length; j++) {
                        StringBuilder builder = new StringBuilder("et_");
                        builder.append(keys[j]);
                        EditText et = (EditText) inputTemplateView.findViewById(getResources().getIdentifier(builder.toString(), "id", BuildConfig.PACKAGE_NAME));
                        et.setText((String) diseaseDetail.getInputMethodValues().get(keys[j]));
                    }
                }
                // 初始化方法一和方法二的点击图片选择坐标点的图标点击事件
                int imageid = getResources().getIdentifier("btn_add_position_from_screen", "id", BuildConfig.PACKAGE_NAME);
                if (imageid > 0) {
                    ImageView iv = (ImageView) inputTemplateView.findViewById(imageid);
                    if (null != iv) {
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(DiseaseDetailActivity.this, CoordinateActivity.class);
                                startActivityForResult(intent, Keys.RequestCodeCoordinate);
                            }
                        });
                    }
                }
            }
        });

        if (!isNewDisease) {
            StringBuilder builder = new StringBuilder("rb_input_");
            builder.append(diseaseDetail.getInputMethod().getId());
            rgRadioGroup.check(getResources().getIdentifier(builder.toString(), "id", BuildConfig.PACKAGE_NAME));
        }
    }

    private Map<String, Object> getCurrentValues() {
        String[] keys = ((EDiseaseInputMethod) findViewById(rgRadioGroup.getCheckedRadioButtonId()).getTag()).getInputTitles();
        HashMap<String, Object> values = new HashMap<String, Object>();
        for (int j = 0; j < keys.length; j++) {
            StringBuilder builder = new StringBuilder("et_");
            builder.append(keys[j]);
            EditText et = (EditText) inputTemplateView.findViewById(getResources().getIdentifier(builder.toString(), "id", BuildConfig.PACKAGE_NAME));
            values.put(keys[j], et.getText().toString());
        }
        return values;
    }

    private void saveDiseaseDetail() {
        EDiseaseInputMethod type = (EDiseaseInputMethod) findViewById(rgRadioGroup.getCheckedRadioButtonId()).getTag();
        Map<String, Object> values = getCurrentValues();

        if (isHaveEmptyData(values, type)) {
            Toast.makeText(this, "请输入全部数据！", Toast.LENGTH_SHORT).show();
            return;
        }

        diseaseDetail.setComponentName(componentName);
        diseaseDetail.setPosition(positionName);
        diseaseDetail.setDiseaseType(StoreManager.Instance.diseaseTypes[spChooseDiseaseType.getSelectedItemPosition()]);
        diseaseDetail.setInputMethod(type);
        diseaseDetail.setInputMethodValues(values);
        diseaseDetail.setPictureList(picturesList);
        diseaseDetail.setRecordList(recordsList);
        diseaseDetail.setVideoList(videosList);

        diseaseModel.setDisease(diseaseDetail);

        if (isNewDisease) {
            try {
                getHelper().getDiseaseDao().create(diseaseModel);
                Toast.makeText(this, "新增成功", Toast.LENGTH_SHORT).show();
                this.finish();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, "新增失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                getHelper().getDiseaseDao().update(diseaseModel);
                Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                this.finish();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initMediaResource() {
        if (!isNewDisease) {
            picturesList = diseaseDetail.getPictureList();
            Bimp.drr = picturesList;
            for (int i = 0; i < diseaseDetail.getRecordList().size(); i++) {
                addMediaFile(diseaseDetail.getRecordList().get(i), true);
            }
            for (int i = 0; i < diseaseDetail.getVideoList().size(); i++) {
                addMediaFile(diseaseDetail.getVideoList().get(i), false);
            }
        }
    }

    public boolean isHaveEmptyData(Map<String, Object> values, EDiseaseInputMethod method) {
//        if(null == values || values.size() == 0){
//            return true;
//        }
//        for(int i = 0; i < method.getInputTitles().length; i ++){
//            if(!method.getInputTitles()[i].equals("moreinfo") &&
//                    ((String)values.get(method.getInputTitles()[i])).isEmpty()){
//                return true;
//            }
//        }
        return false;
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
            saveDiseaseDetail();
        } else if (id == R.id.action_disease_cancel) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initBaseView() {
        llMediaTypes = (LinearLayout) findViewById(R.id.ll_media_types);
        llVoiceRecords = (LinearLayout) findViewById(R.id.ll_voice_record);
        llVideoRecords = (LinearLayout) findViewById(R.id.ll_video_record);
        spChoosePosition = (Spinner) findViewById(R.id.sp_choose_position);
        spChooseDiseaseType = (Spinner) findViewById(R.id.sp_disease_type);
        etDiseaseType = (EditText) findViewById(R.id.et_disease_type);
        rgRadioGroup = (RadioGroup) findViewById(R.id.rg_radio_group);
        rbRadioButton1 = (RadioButton) findViewById(R.id.rb_input_1);
        rbRadioButton1.setTag(EDiseaseInputMethod.One);
        rbRadioButton2 = (RadioButton) findViewById(R.id.rb_input_2);
        rbRadioButton2.setTag(EDiseaseInputMethod.Two);
        rbRadioButton3 = (RadioButton) findViewById(R.id.rb_input_3);
        rbRadioButton3.setTag(EDiseaseInputMethod.Three);
        rbRadioButton4 = (RadioButton) findViewById(R.id.rb_input_4);
        rbRadioButton4.setTag(EDiseaseInputMethod.Four);
        rbRadioButton5 = (RadioButton) findViewById(R.id.rb_input_5);
        rbRadioButton5.setTag(EDiseaseInputMethod.Five);
        llInputTemplate = (LinearLayout) findViewById(R.id.ll_input_container);
        viewPictureDivider = findViewById(R.id.view_picture_divider);
        viewVoiceDivider = findViewById(R.id.view_voice_divider);
        viewVideoDivider = findViewById(R.id.view_video_divider);
        tvComponentName = (TextView) findViewById(R.id.tv_component_name);
    }

    private void initGridView() {
        MyGridView mgvPicturesGridView = (MyGridView) findViewById(R.id.mgv_pictures);
        mgvPicturesGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mgvPicturesAdapter = new GridAdapter(this);
        mgvPicturesAdapter.update();
        mgvPicturesGridView.setAdapter(mgvPicturesAdapter);
        mgvPicturesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(DiseaseDetailActivity.this, PhotoActivity.class);
                intent.putExtra("ID", arg2);
                startActivity(intent);
            }
        });
    }

    private void initMediaLayout() {
        LinearLayout.LayoutParams layoutLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, iconWidth);
        llMediaTypes.setLayoutParams(layoutLP);
        llMediaTypes.addView(addMediaIcon(AddPhotoTag, R.drawable.bg_photo));
        llMediaTypes.addView(addMediaIcon(AddPictureTag, R.drawable.bg_add_picture));
        llMediaTypes.addView(addMediaIcon(AddVoiceTag, R.drawable.bg_add_voice));
        llMediaTypes.addView(addMediaIcon(AddVideoTag, R.drawable.bg_add_movie));
    }

    private View addMediaIcon(String tag, int imageid) {
        LinearLayout.LayoutParams addIconLP = new LinearLayout.LayoutParams(iconWidth, iconWidth);
        addIconLP.setMargins(5, 0, 5, 0);

        ImageView addVideoIcon = new ImageView(this);
        addVideoIcon.setLayoutParams(addIconLP);
        addVideoIcon.setOnClickListener(this);
        addVideoIcon.setTag(tag);
        addVideoIcon.setBackgroundResource(imageid);

        return addVideoIcon;
    }

    protected void onRestart() {
        if (Bimp.drr.size() > 0) {
            viewPictureDivider.setVisibility(View.VISIBLE);
        } else {
            viewPictureDivider.setVisibility(View.GONE);
        }
        mgvPicturesAdapter.update();
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Keys.RequestCodeTakePicture:
                if (Bimp.drr.size() < 9 && resultCode == RESULT_OK) {
                    picturesList.clear();
                    Bimp.drr.add(path);
                    for (int i = 0; i < Bimp.drr.size(); i++) {
                        picturesList.add(Bimp.drr.get(i));
                    }
                }
                break;
            case Keys.RequestCodeTakeRecord:
                if (resultCode == Keys.ResultCodeSuccess && null != data && null != data.getExtras()) {
                    String recordPath = data.getExtras().getString(Keys.KeyContent);
                    if (!TextUtils.isEmpty(recordPath)) {
                        addMediaFile(recordPath, true);
                    }
                }
                break;

            case Keys.RequestCodeTakeMovie:
                if (resultCode != Keys.ResultCodeSuccess || null == data.getExtras()) {
                    return;
                }
                String videoPath = data.getExtras().getString(Keys.KeyContent);
                if (!TextUtils.isEmpty(videoPath)) {
                    addMediaFile(videoPath, false);
                }
                break;
            case Keys.RequestCodeCoordinate:
                View view = inputTemplateView.findViewById(R.id.et_startpoint);
                if (view != null) {

                    String startPoint = random() + "," + random();
                    ((EditText) view).setText(startPoint);

                    EditText editText = (EditText) inputTemplateView.findViewById(R.id.et_endpoint);
                    String endPoint = random() + "," + random();
                    editText.setText(endPoint);
                } else {
                    view = inputTemplateView.findViewById(R.id.et_position);
                    if (view != null) {
                        EditText editText = (EditText) view;
                        String endPoint = random() + "," + random();
                        editText.setText(endPoint);
                    }
                }
                break;
        }
    }

    private int random() {
        return 1 + (int)(Math.random()*15);
    }

    private void addVoiceRecord() {
        Intent intent = new Intent();
        intent.setClass(this, VoiceRecordActivity.class);
        startActivityForResult(intent, Keys.RequestCodeTakeRecord);
    }

    /**
     * isVoice = true 表示是增加语音文件
     * isVoice = false 表示是增加视频文件
     */
    private void addMediaFile(final String filePath, boolean isVoice) {
        LinearLayout.LayoutParams layoutLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, iconWidth);
        LinearLayout.LayoutParams addIconLP = new LinearLayout.LayoutParams(iconWidth, iconWidth);
        ImageView addPhotoIcon = new ImageView(this);
        addIconLP.setMargins(5, 0, 10, 0);
        addPhotoIcon.setLayoutParams(addIconLP);
        addPhotoIcon.setOnClickListener(this);
        addPhotoIcon.setTag(filePath);
        addPhotoIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DiseaseDetailActivity.this);
                builder.setMessage("确认删除该多媒体文件吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (v.getTag().toString().contains("Voice")) {
                            llVoiceRecords.removeView(v);
                            recordsList.remove(filePath);
                        } else {
                            llVideoRecords.removeView(v);
                            videosList.remove(filePath);
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return false;
            }
        });
        if (isVoice) {
            llVoiceRecords.setLayoutParams(layoutLP);
            addPhotoIcon.setBackgroundResource(R.drawable.icon_voice);
            llVoiceRecords.addView(addPhotoIcon, addIconLP);
            if (llVoiceRecords.getChildCount() > 0) {
                viewVoiceDivider.setVisibility(View.VISIBLE);
            } else {
                viewVoiceDivider.setVisibility(View.GONE);
            }
            recordsList.add(filePath);
        } else {
            llVideoRecords.setLayoutParams(layoutLP);
            addPhotoIcon.setBackgroundResource(R.drawable.icon_vedio);
            llVideoRecords.addView(addPhotoIcon, addIconLP);
            if (llVideoRecords.getChildCount() > 0) {
                viewVideoDivider.setVisibility(View.VISIBLE);
            } else {
                viewVideoDivider.setVisibility(View.GONE);
            }
            videosList.add(filePath);
        }
    }

    private void addMovie() {
        Intent intent = new Intent();
        intent.setClass(this, MovieRecordActivity.class);
        startActivityForResult(intent, Keys.RequestCodeTakeMovie);
    }

    protected void takePhotoFromCamera() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String currentTakePictureName = "Picture-" + df.format(new Date()) + ".jpg";

        File pictureSaveDir = new File(PicturePath);

        if (!pictureSaveDir.exists()) {
            if (!pictureSaveDir.mkdirs()) {
                Toast.makeText(this, "创建图片文档目录失败", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        File file = new File(PicturePath + currentTakePictureName);
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, Keys.RequestCodeTakePicture);
    }

    protected void pickPhotoFromGallery() {
        Intent intent = new Intent(DiseaseDetailActivity.this,
                TestPicActivity.class);
        startActivityForResult(intent, Keys.RequestCodePickPicture);
    }

    @Override
    public void onClick(View v) {
        if (null != v.getTag()) {
            isHaveTag = true;
            if (v.getTag().equals(AddPictureTag)) {
                pickPhotoFromGallery();
            } else if (v.getTag().equals(AddPhotoTag)) {
                takePhotoFromCamera();
            } else if (v.getTag().equals(AddVoiceTag)) {
                addVoiceRecord();
            } else if (v.getTag().equals(AddVideoTag)) {
                addMovie();
            } else {
                String mediaPath = (String) v.getTag();
                // 这里不是视频就是音频文件了
                if (mediaPath.contains("Video")) {
                    Uri uri = Uri.parse("file://" + mediaPath);
                    //调用系统自带的播放器
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "video/3gpp");
                    startActivity(intent);
                } else {
                    MediaPlayer mPlayer = new MediaPlayer();
                    try {
                        mPlayer.setDataSource(mediaPath);
                        mPlayer.prepare();
                        mPlayer.start();
                    } catch (IOException e) {
                        Toast.makeText(this, "播放录音失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            isHaveTag = false;
        }
    }

    @Override
    public void finish() {
        super.finish();
        Bimp.clearData();
    }
}
