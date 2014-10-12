package com.xiaohui.bridge.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.store.KeyStore;
import com.xiaohui.bridge.business.store.StoreManager;
import com.xiaohui.bridge.util.BitmapUtil;
import com.xiaohui.bridge.util.DeviceParamterUtil;
import com.xiaohui.bridge.view.DiseaseInputTemplateView.DiseaseInputTemplate1;
import com.xiaohui.bridge.view.DiseaseInputTemplateView.DiseaseInputTemplate2;
import com.xiaohui.bridge.view.DiseaseInputTemplateView.DiseaseInputTemplate3;
import com.xiaohui.bridge.view.DiseaseInputTemplateView.DiseaseInputTemplate4;
import com.xiaohui.bridge.view.DiseaseInputTemplateView.DiseaseInputTemplate5;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * 多种病害输入模板基类
 * Created by Administrator on 2014/10/9.
 */
public class BaseDiseaseDetailActivity extends AbstractActivity implements View.OnClickListener {

    private static String PicturePath = "/mnt/sdcard/IBridge/Picture/";
    private static String AddPhotoTag = "AddPhoto";
    private static String AddPictureTag = "AddPicture";
    private static String AddVoiceTag = "AddVoice";
    private static String AddVideoTag = "AddVideo";

    protected boolean isHaveTag = false;

    private LinearLayout llPictures;
    private LinearLayout llVoices;
    private LinearLayout llVideos;
    private Spinner spChoosePosition;
    private Spinner spChooseDiseaseType;
    private EditText etDiseaseType;
    private RadioGroup rgRadioGroup;
    private LinearLayout llInputTemplate;

    private String currentTakePictureName = "";

    private int mediaLayoutWidth = 0;
    private int mediaLayoutHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);
        boolean isAdded = getIntent().getBooleanExtra(Keys.FLAG, true);
        setTitle(isAdded ? "病害新增" : "病害编辑");

        llPictures = (LinearLayout) findViewById(R.id.ll_pictures);
        llVoices = (LinearLayout) findViewById(R.id.ll_voice_records);
        llVideos = (LinearLayout) findViewById(R.id.ll_video_records);
        spChoosePosition = (Spinner) findViewById(R.id.sp_choose_position);
        spChooseDiseaseType = (Spinner) findViewById(R.id.sp_disease_type);
        etDiseaseType = (EditText) findViewById(R.id.et_disease_type);
        rgRadioGroup = (RadioGroup) findViewById(R.id.rg_radio_group);
        llInputTemplate = (LinearLayout) findViewById(R.id.ll_input_container);

        ArrayAdapter<String> positions = new ArrayAdapter<String>(this, R.layout.view_spinner_item, StoreManager.Instance.generalsTypes);
        positions.setDropDownViewResource(R.layout.view_spinner_dropdown_item);
        spChoosePosition.setAdapter(positions);
        spChoosePosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> diseases = new ArrayAdapter<String>(this, R.layout.view_spinner_item, StoreManager.Instance.diseaseTypes);
        diseases.setDropDownViewResource(R.layout.view_spinner_dropdown_item);
        spChooseDiseaseType.setAdapter(diseases);
        spChooseDiseaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
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

        llInputTemplate.addView(new DiseaseInputTemplate1(BaseDiseaseDetailActivity.this));
        rgRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) BaseDiseaseDetailActivity.this.findViewById(radioButtonId);
                llInputTemplate.removeAllViews();
                switch (Integer.valueOf((String) rb.getTag())) {
                    case 1:
                        llInputTemplate.addView(new DiseaseInputTemplate1(BaseDiseaseDetailActivity.this));
                        break;
                    case 2:
                        llInputTemplate.addView(new DiseaseInputTemplate2(BaseDiseaseDetailActivity.this));
                        break;
                    case 3:
                        llInputTemplate.addView(new DiseaseInputTemplate3(BaseDiseaseDetailActivity.this));
                        break;
                    case 4:
                        llInputTemplate.addView(new DiseaseInputTemplate4(BaseDiseaseDetailActivity.this));
                        break;
                    case 5:
                        llInputTemplate.addView(new DiseaseInputTemplate5(BaseDiseaseDetailActivity.this));
                        break;
                }
            }
        });

        initMediaLayout();
    }

    private void initMediaLayout() {
        mediaLayoutWidth = (int) (DeviceParamterUtil.getScreenPixelsWidth() - DeviceParamterUtil.getScreenDensity() * 40);
        mediaLayoutHeight = mediaLayoutWidth / 5;

        LinearLayout.LayoutParams layoutLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mediaLayoutHeight);
        llPictures.setLayoutParams(layoutLP);
        llVoices.setLayoutParams(layoutLP);
        llVideos.setLayoutParams(layoutLP);

        LinearLayout.LayoutParams addIconLP = new LinearLayout.LayoutParams(mediaLayoutHeight, mediaLayoutHeight);
        ImageView addPhotoIcon = new ImageView(this);
        addPhotoIcon.setLayoutParams(addIconLP);
        addPhotoIcon.setOnClickListener(this);
        addPhotoIcon.setTag(AddPhotoTag);
        addPhotoIcon.setBackgroundResource(R.drawable.bg_photo);
        llPictures.addView(addPhotoIcon);

        ImageView addPicIcon = new ImageView(this);
        addPicIcon.setLayoutParams(addIconLP);
        addPicIcon.setOnClickListener(this);
        addPicIcon.setTag(AddPictureTag);
        addPicIcon.setBackgroundResource(R.drawable.bg_add_picture);
        llPictures.addView(addPicIcon);

        ImageView addVoiceIcon = new ImageView(this);
        addVoiceIcon.setLayoutParams(addIconLP);
        addVoiceIcon.setOnClickListener(this);
        addVoiceIcon.setTag(AddVoiceTag);
        addVoiceIcon.setBackgroundResource(R.drawable.bg_add_voice);
        llVoices.addView(addVoiceIcon);

        ImageView addVideoIcon = new ImageView(this);
        addVideoIcon.setLayoutParams(addIconLP);
        addVideoIcon.setOnClickListener(this);
        addVideoIcon.setTag(AddVideoTag);
        addVideoIcon.setBackgroundResource(R.drawable.bg_add_movie);
        llVideos.addView(addVideoIcon);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case KeyStore.RequestCodeTakePicture:
                if (resultCode != RESULT_OK) {
                    return;
                }
                addPicture(data);
                break;
            case KeyStore.RequestCodePickPicture:
                if (resultCode != RESULT_OK) {
                    return;
                }
                addPicture(data);
                break;
            case KeyStore.RequestCodeTakeRecord:
                if (null != data && null != data.getExtras()) {
                    ArrayList<String> recordList = data.getExtras().getStringArrayList(KeyStore.KeyContent);
                    if (!recordList.isEmpty()) {
                        // TODO 这里显示录音文件的名字
                    }
                }
                break;

            case KeyStore.RequestCodeShowImage:
                if (resultCode != KeyStore.ResultCodeDelete) {
                    return;
                }
                if (null != data && null != data.getExtras()) {
                    String picPath = data.getExtras().getString(KeyStore.KeyContent);
                    if (picPath.isEmpty()) {
                        return;
                    } else {
                        llPicturesLayoutChange(picPath);
                    }
                }
                break;
        }
    }

    private void llPicturesLayoutChange(String picturePath) {
        for (int i = 0; i < llPictures.getChildCount(); i++) {
            View view = llPictures.getChildAt(i);
            if (view.getTag().equals(picturePath)) {
                llPictures.removeView(view);
                return;
            }
        }
    }

    private void addVoiceRecord() {
        Intent intent = new Intent();
        intent.setClass(this, VoiceRecordActivity.class);
        startActivityForResult(intent, KeyStore.RequestCodeTakeRecord);
    }

    private void addMovie() {
        Intent intent = new Intent();
        intent.setClass(this, MovieRecordActivity.class);
        startActivityForResult(intent, KeyStore.RequestCodeTakeMovie);
    }

    protected void takePhotoFromCamera() {
        try {
            File cameraPhotoSaveDir = new File(PicturePath);

            if (!cameraPhotoSaveDir.exists()) {
                if (!cameraPhotoSaveDir.mkdirs()) {
                    Toast.makeText(this, "创建目录失败", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // 给拍摄的照片文件命名
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            currentTakePictureName = "Picture-" + df.format(new Date()) + ".jpg";
            File cameraPhotoSavePath = new File(PicturePath + currentTakePictureName);

            // 打开相机拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraPhotoSavePath));
            startActivityForResult(intent, KeyStore.RequestCodeTakePicture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void pickPhotoFromGallery() {
        // 打开相册
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            intent.setType("image/*");
            intent.putExtra("return-data", true);
            startActivityForResult(intent, KeyStore.RequestCodePickPicture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDataColumn(Context context, Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        // 按我个人理解 这个是获得用户选择的图片的索引值
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        // 将光标移至开头 ，这个很重要，不小心很容易引起越界
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        // 最后根据索引值获取图片路径
        return path;

    }

    private void addPicture(Intent data) {
        if (null != data) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                addBitmapToView(bitmap, getDataColumn(this, data.getData()));
                bitmap.recycle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String path = PicturePath + currentTakePictureName;
            Bitmap bitmap = BitmapUtil.getBitmapFromFilePath(path, 400 * 400);
            addBitmapToView(bitmap, path);
            bitmap.recycle();
        }
    }

    private void addBitmapToView(Bitmap bitmap, Object tag) {
        try {
            ImageView picView = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mediaLayoutHeight, mediaLayoutHeight);
            lp.setMargins(0, 0, (int) DeviceParamterUtil.getScreenDensity() * 5, 0);
            picView.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap, 400, 400));
            picView.setLayoutParams(lp);
            picView.setClickable(true);
            picView.setOnClickListener(this);
            picView.setTag(tag);
            llPictures.addView(picView, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                String picPath = (String) v.getTag();
                Intent intent = new Intent();
                intent.setClass(this, ShowImageActivity.class);
                intent.putExtra(KeyStore.KeyContent, picPath);
                startActivityForResult(intent, KeyStore.RequestCodeShowImage);
            }
        } else {
            isHaveTag = false;
        }
    }
}
