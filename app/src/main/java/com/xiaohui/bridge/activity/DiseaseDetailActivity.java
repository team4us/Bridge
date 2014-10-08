package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.store.KeyStore;
import com.xiaohui.bridge.util.BitmapUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 病害详情界面
 * Created by Administrator on 2014/9/27.
 */
public class DiseaseDetailActivity extends AbstractActivity implements View.OnClickListener{

    private static String picturePath = Environment.getExternalStorageDirectory() + "/IBridge/Picture/";

    private Button btnAddPicture;
    private Button btnAddMedia;
    private LinearLayout llPictures;
    private String currentTakePictureName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);
        setTitle("病害录入");

        btnAddPicture = (Button) findViewById(R.id.btn_add_picture);
        btnAddMedia = (Button) findViewById(R.id.btn_media);
        llPictures = (LinearLayout) findViewById(R.id.ll_pictures);

        btnAddPicture.setOnClickListener(this);
        btnAddMedia.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnAddPicture) {
            Intent intent = new Intent();
            intent.setClass(this, MenuActivity.class);
            ArrayList<MenuActivity.MenuItem> menuItems = new ArrayList<MenuActivity.MenuItem>();
            menuItems.add(new MenuActivity.MenuItem("拍照"));
            menuItems.add(new MenuActivity.MenuItem("从相册选择"));
            intent.putExtra(KeyStore.KeyContent, menuItems);
            startActivityForResult(intent, KeyStore.RequestCodePicture);
        } else if(v == btnAddMedia){
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

    private void onMenuResult(Intent data) {
        if(null == data){
            return;
        }
        int selectedIndex = data.getIntExtra(KeyStore.KeySelectedIndex, -1);
        if (selectedIndex == -1) {
            return;
        }
        if (selectedIndex == 0) {
            takePhotoFromCamera();
        } else if(selectedIndex == 1){
            pickPhotoFromGallery();
        } else if(selectedIndex == 2){
            addVoiceRecord();
        } else if(selectedIndex == 3){
            addMovie();
        }
    }

    private void addVoiceRecord(){
        Intent intent = new Intent();
        intent.setClass(this, VoiceRecordActivity.class);
        startActivityForResult(intent, KeyStore.RequestCodeTakeRecord);
    }

    private void addMovie(){
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
            startActivityForResult(intent, KeyStore.RequestCodePickPicture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case KeyStore.RequestCodePicture:
                onMenuResult(data);
                break;
            case KeyStore.RequestCodeTakePicture:
                if (null != data) {
                    //获取图片的BitmapDrawable对象
                    BitmapDrawable drableBit = getPhotoDrawable(data);
                    ImageView picView = new ImageView(this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
                    picView.setLayoutParams(lp);

                    picView.setBackgroundDrawable(drableBit);
                    llPictures.addView(picView);
                } else {
                    BitmapDrawable bp = new BitmapDrawable(BitmapUtil.getBitmapFromFilePath(picturePath + currentTakePictureName));
                    ImageView picView = new ImageView(this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
                    picView.setLayoutParams(lp);

                    picView.setBackgroundDrawable(bp);
                    llPictures.addView(picView);
                }
                break;
            case KeyStore.RequestCodePickPicture:
                break;
            case KeyStore.RequestCodeTakeRecord:
                if(null != data && null != data.getExtras()) {
                    ArrayList<String> recordList = data.getExtras().getStringArrayList(KeyStore.KeyContent);
                    if(!recordList.isEmpty()){
                        // TODO 这里显示录音文件的名字
                    }
                }
                break;
        }
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
     * @param data
     * @return
     */
    public static Bitmap getPhotoBitmap(Intent data){
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

}
