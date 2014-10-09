package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.store.KeyStore;
import com.xiaohui.bridge.util.BitmapUtil;
import com.xiaohui.bridge.util.DeviceParamterUtil;

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
public class BaseDiseaseDetailActivity extends AbstractActivity implements View.OnClickListener{

    private static String picturePath = Environment.getExternalStorageDirectory() + "/IBridge/Picture/";

    private Button btnAddMedia;
    private Button btnAddPictureFromScreen;
    private LinearLayout llPictures;
    private String currentTakePictureName = "";

    private int mediaLayoutWidth = 0;
    private int mediaLayoutHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnAddMedia = (Button) findViewById(R.id.btn_media);
        btnAddPictureFromScreen = (Button) findViewById(R.id.btn_addposition_from_screen);
        llPictures = (LinearLayout) findViewById(R.id.ll_pictures);

        btnAddMedia.setOnClickListener(this);
        btnAddPictureFromScreen.setOnClickListener(this);

        initPictureLayout();
    }

    private void initPictureLayout() {
        mediaLayoutWidth = (int) (DeviceParamterUtil.getScreenPixelsWidth() - DeviceParamterUtil.getScreenDensity() * 40);
        mediaLayoutHeight = mediaLayoutWidth / 5;

        LinearLayout.LayoutParams layoutLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mediaLayoutHeight);
        llPictures.setLayoutParams(layoutLP);

        ImageView addPictureIcon = new ImageView(this);
        LinearLayout.LayoutParams addPicLP = new LinearLayout.LayoutParams(mediaLayoutHeight, mediaLayoutHeight);
        addPictureIcon.setLayoutParams(addPicLP);
        addPictureIcon.setTag("AddPicture");
        addPictureIcon.setOnClickListener(this);
        addPictureIcon.setBackgroundResource(R.drawable.bg_add_picture);

        llPictures.addView(addPictureIcon);
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
                addPictureBack(data);
                break;
            case KeyStore.RequestCodePickPicture:
                if (resultCode != RESULT_OK) {
                    return;
                }
                addPictureBack(data);
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

    private void llPicturesLayoutChange(String picturePath){
        for (int i = 0; i < llPictures.getChildCount(); i ++){
            View view = llPictures.getChildAt(i);
            if(view.getTag().equals(picturePath)){
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
            File cameraPhotoSaveDir = new File(picturePath);

            if (!cameraPhotoSaveDir.exists()) {
                if (!cameraPhotoSaveDir.mkdirs()) {
                    Toast.makeText(this, "创建目录失败", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // 给拍摄的照片文件命名
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            currentTakePictureName = "Picture-" + df.format(new Date()) + ".jpg";
            File cameraPhotoSavePath = new File(picturePath + currentTakePictureName);

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

    private void addPictureBack(Intent data) {
        ImageView picView = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mediaLayoutHeight, mediaLayoutHeight);
        lp.setMargins(0, 0, (int) DeviceParamterUtil.getScreenDensity() * 5, 0);

        BitmapDrawable drableBit;
        if (null != data) {
            //获取图片的BitmapDrawable对象
            drableBit = getPhotoDrawable(data);
        } else {
            drableBit = new BitmapDrawable(BitmapUtil.getBitmapFromFilePath(picturePath + currentTakePictureName, 400 * 400));

        }

        picView.setBackgroundDrawable(drableBit);
        picView.setLayoutParams(lp);
        picView.setTag(picturePath + currentTakePictureName);
        picView.setOnClickListener(this);
        llPictures.addView(picView, 0);
    }

    @SuppressWarnings("deprecation")
    public static BitmapDrawable getPhotoDrawable(Intent data) {
        ByteArrayOutputStream stream = null;
        Bitmap photo = getPhotoBitmap(data);
        if (photo != null) {
            stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0,100)压缩文件
            //将Bitmap对象转换成BitmapDrawable对象
            BitmapDrawable drableBit = new BitmapDrawable(photo);
            return drableBit;
        }
        return null;
    }

    /**
     * 返回裁剪后的照片的Bitmap对象路径
     *
     * @param data
     * @return
     */
    public static Bitmap getPhotoBitmap(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap photo = null;
        if (null != extras) {
            photo = extras.getParcelable("data");

            // 此else改法是根据bug:1184的报错信息和solomon给的提示信息修改，暂时没有方法验证，但能确保正常情况下不会有问题。
            // eric.huang 2013-8-21 如果裁剪后设置不需要返回return-data也会通过uri方式获取图片
        } else {
            Uri uri = data.getData();
            if (uri != null) {
                // eric.huang从资源中获取的图片可能会比较大，需要经过自定义缩放比的压缩
                photo = BitmapUtil.getBitmapFromFilePath(uri.getPath());
            }
        }
        return photo;
    }

    @Override
    public void onClick(View v) {
        if (null != v.getTag()) {
            if (v.getTag().equals("AddPicture")) {
                Intent intent = new Intent();
                intent.setClass(this, MenuActivity.class);
                ArrayList<MenuActivity.MenuItem> menuItems = new ArrayList<MenuActivity.MenuItem>();
                menuItems.add(new MenuActivity.MenuItem("拍照"));
                menuItems.add(new MenuActivity.MenuItem("从相册选择"));
                intent.putExtra(KeyStore.KeyContent, menuItems);
                startActivityForResult(intent, KeyStore.RequestCodePicture);
                return;
            } else {
                String picPath = (String) v.getTag();
                Intent intent = new Intent();
                intent.setClass(this, ShowImageActivity.class);
                intent.putExtra(KeyStore.KeyContent, picPath);
                startActivityForResult(intent, KeyStore.RequestCodeShowImage);
            }
        }

        if (v == btnAddMedia) {
            Intent intent = new Intent();
            intent.setClass(this, MenuActivity.class);
            ArrayList<MenuActivity.MenuItem> menuItems = new ArrayList<MenuActivity.MenuItem>();
            menuItems.add(new MenuActivity.MenuItem("拍照"));
            menuItems.add(new MenuActivity.MenuItem("从相册选择"));
            menuItems.add(new MenuActivity.MenuItem("录音"));
            menuItems.add(new MenuActivity.MenuItem("摄像"));
            intent.putExtra(KeyStore.KeyContent, menuItems);
            startActivityForResult(intent, KeyStore.RequestCodePicture);
        }
    }
}
