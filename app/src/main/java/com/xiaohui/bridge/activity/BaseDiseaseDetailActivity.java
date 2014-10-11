package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.ByteArrayOutputStream;
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

    private static String PicturePath = Environment.getExternalStorageDirectory() + "/IBridge/Picture/";
    private static String AddPictureTag = "AddPicture";
    private static String AddVoiceTag = "AddVoice";
    private static String AddVideoTag = "AddVedio";

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

        ArrayAdapter<String> positions = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, StoreManager.Instance.generalsTypes);
        positions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChoosePosition.setAdapter(positions);
        spChoosePosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> diseases = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, StoreManager.Instance.diseaseTypes);
        diseases.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChooseDiseaseType.setAdapter(diseases);
        spChooseDiseaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
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
                RadioButton rb = (RadioButton)BaseDiseaseDetailActivity.this.findViewById(radioButtonId);
                llInputTemplate.removeAllViews();
                switch (Integer.valueOf((String)rb.getTag())){
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

        ImageView addPicIcon = new ImageView(this);
        LinearLayout.LayoutParams addIconLP = new LinearLayout.LayoutParams(mediaLayoutHeight, mediaLayoutHeight);
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

    private void onMenuResult(Intent data) {
        if (null == data) {
            return;
        }
        int selectedIndex = data.getIntExtra(KeyStore.KeySelectedIndex, -1);
        if (selectedIndex == -1) {
            return;
        }
        if (selectedIndex == 0) {
            takePhotoFromCamera();
        } else if (selectedIndex == 1) {
            pickPhotoFromGallery();
        } else if (selectedIndex == 2) {
            addVoiceRecord();
        } else if (selectedIndex == 3) {
            addMovie();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case KeyStore.RequestCodePicture:
                if (resultCode != KeyStore.ResultCodeSuccess) {
                    return;
                }
                onMenuResult(data);
                break;
            case KeyStore.RequestCodeTakePicture:
                if (resultCode != RESULT_OK) {
                    return;
                }
                addPictureFromCamera(data);
                break;
            case KeyStore.RequestCodePickPicture:
                if (resultCode != RESULT_OK) {
                    return;
                }
                addPictureFromAlbum(data);
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

    private void addPictureFromCamera(Intent data) {
        ImageView picView = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mediaLayoutHeight, mediaLayoutHeight);
        lp.setMargins(0, 0, (int) DeviceParamterUtil.getScreenDensity() * 5, 0);

        BitmapDrawable drableBit;
        if (null != data) {
            //获取图片的BitmapDrawable对象
            drableBit = getPhotoDrawable(data);
        } else {
            drableBit = new BitmapDrawable(BitmapUtil.getBitmapFromFilePath(PicturePath + currentTakePictureName, 400 * 400));

        }

        picView.setBackgroundDrawable(drableBit);
        picView.setLayoutParams(lp);
        picView.setTag(PicturePath + currentTakePictureName);
        picView.setOnClickListener(this);
        llPictures.addView(picView, 0);
    }

    private void addPictureFromAlbum(Intent data) {
        ImageView picView = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mediaLayoutHeight, mediaLayoutHeight);
        lp.setMargins(0, 0, (int) DeviceParamterUtil.getScreenDensity() * 5, 0);

        Uri originalUri = data.getData();
        String picPath = "";
        if (originalUri != null) {
            try {
                String[] proj = {MediaStore.MediaColumns.DATA};
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                // 按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();

                // 最后根据索引值获取图片路径
                picPath = cursor.getString(column_index);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        BitmapDrawable drawableBit = new BitmapDrawable(BitmapUtil.getBitmapFromFilePath(picPath, 400 * 400));

        picView.setBackgroundDrawable(drawableBit);
        picView.setLayoutParams(lp);
        picView.setTag(picPath);
        picView.setOnClickListener(this);
        llPictures.addView(picView, 0);
    }

    @SuppressWarnings("deprecation")
    public static BitmapDrawable getPhotoDrawable(Intent data) {
        Bitmap photo = getPhotoBitmap(data);
        if (photo != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0,100)压缩文件
            //将Bitmap对象转换成BitmapDrawable对象
            BitmapDrawable drableBit = new BitmapDrawable(photo);
            return drableBit;
        }
        return null;
    }

    /**
     * 返回裁剪后的照片的Bitmap对象路径
     */
    public static Bitmap getPhotoBitmap(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap photo = null;
        if (null != extras) {
            photo = extras.getParcelable("data");
        } else {
            Uri uri = data.getData();
            if (uri != null) {
                photo = BitmapUtil.getBitmapFromFilePath(uri.getPath());
            }
        }
        return photo;
    }

    @Override
    public void onClick(View v) {
        if (null != v.getTag()) {
            isHaveTag = true;
            if (v.getTag().equals(AddPictureTag)) {
                Intent intent = new Intent();
                intent.setClass(this, MenuActivity.class);
                ArrayList<MenuActivity.MenuItem> menuItems = new ArrayList<MenuActivity.MenuItem>();
                menuItems.add(new MenuActivity.MenuItem("拍照"));
                menuItems.add(new MenuActivity.MenuItem("从相册选择"));
                intent.putExtra(KeyStore.KeyContent, menuItems);
                startActivityForResult(intent, KeyStore.RequestCodePicture);
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
