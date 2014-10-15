package com.xiaohui.bridge.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
import com.xiaohui.bridge.util.DeviceParamterUtil;
import com.xiaohui.bridge.view.DiseaseInputTemplateView.DiseaseInputTemplate1;
import com.xiaohui.bridge.view.DiseaseInputTemplateView.DiseaseInputTemplate2;
import com.xiaohui.bridge.view.DiseaseInputTemplateView.DiseaseInputTemplate3;
import com.xiaohui.bridge.view.DiseaseInputTemplateView.DiseaseInputTemplate4;
import com.xiaohui.bridge.view.DiseaseInputTemplateView.DiseaseInputTemplate5;
import com.xiaohui.bridge.view.MyGridView;
import com.xiaohui.bridge.view.PickPicture.Bimp;
import com.xiaohui.bridge.view.PickPicture.FileUtils;
import com.xiaohui.bridge.view.PickPicture.PhotoActivity;
import com.xiaohui.bridge.view.PickPicture.TestPicActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * 多种病害输入模板基类
 * Created by Administrator on 2014/10/9.
 */
public class DiseaseDetailActivity extends AbstractActivity implements View.OnClickListener {

    private static String PicturePath = "/mnt/sdcard/IBridge/Picture/";
    private static String AddPhotoTag = "AddPhoto";
    private static String AddPictureTag = "AddPicture";
    private static String AddVoiceTag = "AddVoice";
    private static String AddVideoTag = "AddVideo";

    protected boolean isHaveTag = false;

    private LinearLayout llMediaTypes;
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

    private MyGridView mgvPicturesGridView;
    private GridAdapter mgvPicturesAdapter;

    private String currentTakePictureName = "";
    private String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);
        boolean isAdded = getIntent().getBooleanExtra(Keys.FLAG, true);
        setTitle(isAdded ? "病害新增" : "病害编辑");

        llMediaTypes = (LinearLayout) findViewById(R.id.ll_media_types);
        spChoosePosition = (Spinner) findViewById(R.id.sp_choose_position);
        spChooseDiseaseType = (Spinner) findViewById(R.id.sp_disease_type);
        etDiseaseType = (EditText) findViewById(R.id.et_disease_type);
        rgRadioGroup = (RadioGroup) findViewById(R.id.rg_radio_group);
        rbRadioButton1 = (RadioButton) findViewById(R.id.rb_input_1);
        rbRadioButton2 = (RadioButton) findViewById(R.id.rb_input_2);
        rbRadioButton3 = (RadioButton) findViewById(R.id.rb_input_3);
        rbRadioButton4 = (RadioButton) findViewById(R.id.rb_input_4);
        rbRadioButton5 = (RadioButton) findViewById(R.id.rb_input_5);
        llInputTemplate = (LinearLayout) findViewById(R.id.ll_input_container);

        initDiseaseDetailView();
        initMediaLayout();
        initGridView();
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
            Toast.makeText(this, "保存", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_disease_cancel) {
            // TODO 这里需要弹出个提示框问是否确定要退出，因为可能是误触
            Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initDiseaseDetailView() {
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
                if (position == 0) {
                    rbRadioButton1.setChecked(true);
                    rbRadioButton1.setVisibility(View.VISIBLE);
                    rbRadioButton1.setText("详细记录");
                    rbRadioButton3.setVisibility(View.VISIBLE);
                    rbRadioButton3.setText("统计记录");
                    rbRadioButton5.setVisibility(View.VISIBLE);
                    rbRadioButton5.setText("描述记录");
                    rbRadioButton2.setVisibility(View.GONE);
                    rbRadioButton4.setVisibility(View.GONE);
                } else {
                    rbRadioButton2.setChecked(true);
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

        llInputTemplate.addView(new DiseaseInputTemplate1(DiseaseDetailActivity.this));
        rgRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) DiseaseDetailActivity.this.findViewById(radioButtonId);
                llInputTemplate.removeAllViews();
                switch (Integer.valueOf((String) rb.getTag())) {
                    case 1:
                        llInputTemplate.addView(new DiseaseInputTemplate1(DiseaseDetailActivity.this));
                        break;
                    case 2:
                        llInputTemplate.addView(new DiseaseInputTemplate2(DiseaseDetailActivity.this));
                        break;
                    case 3:
                        llInputTemplate.addView(new DiseaseInputTemplate3(DiseaseDetailActivity.this));
                        break;
                    case 4:
                        llInputTemplate.addView(new DiseaseInputTemplate4(DiseaseDetailActivity.this));
                        break;
                    case 5:
                        llInputTemplate.addView(new DiseaseInputTemplate5(DiseaseDetailActivity.this));
                        break;
                }
            }
        });
    }

    private void initGridView() {
        mgvPicturesGridView = (MyGridView) findViewById(R.id.mgv_pictures);
        mgvPicturesGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mgvPicturesAdapter = new GridAdapter(this);
        mgvPicturesAdapter.update();
        mgvPicturesGridView.setAdapter(mgvPicturesAdapter);
        mgvPicturesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(DiseaseDetailActivity.this,
                        PhotoActivity.class);
                intent.putExtra("ID", arg2);
                startActivity(intent);
            }
        });
    }

    private void initMediaLayout() {
        int width = DeviceParamterUtil.dip2px(60);
        int height = width;
        LinearLayout.LayoutParams layoutLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        llMediaTypes.setLayoutParams(layoutLP);

        LinearLayout.LayoutParams addIconLP = new LinearLayout.LayoutParams(width, height);
        ImageView addPhotoIcon = new ImageView(this);
        addIconLP.setMargins(5, 0, 5, 0);
        addPhotoIcon.setLayoutParams(addIconLP);
        addPhotoIcon.setOnClickListener(this);
        addPhotoIcon.setTag(AddPhotoTag);
        addPhotoIcon.setBackgroundResource(R.drawable.bg_photo);
        llMediaTypes.addView(addPhotoIcon, addIconLP);

        ImageView addPicIcon = new ImageView(this);
        addPicIcon.setLayoutParams(addIconLP);
        addPicIcon.setOnClickListener(this);
        addPicIcon.setTag(AddPictureTag);
        addPicIcon.setBackgroundResource(R.drawable.bg_add_picture);
        llMediaTypes.addView(addPicIcon);

        ImageView addVoiceIcon = new ImageView(this);
        addVoiceIcon.setLayoutParams(addIconLP);
        addVoiceIcon.setOnClickListener(this);
        addVoiceIcon.setTag(AddVoiceTag);
        addVoiceIcon.setBackgroundResource(R.drawable.bg_add_voice);
        llMediaTypes.addView(addVoiceIcon);

        ImageView addVideoIcon = new ImageView(this);
        addVideoIcon.setLayoutParams(addIconLP);
        addVideoIcon.setOnClickListener(this);
        addVideoIcon.setTag(AddVideoTag);
        addVideoIcon.setBackgroundResource(R.drawable.bg_add_movie);
        llMediaTypes.addView(addVideoIcon);
    }

    protected void onRestart() {
        mgvPicturesAdapter.update();
        super.onRestart();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case KeyStore.RequestCodeTakePicture:
                if (Bimp.drr.size() < 9 && resultCode == -1) {
                    Bimp.drr.add(path);
                }
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
        for (int i = 0; i < llMediaTypes.getChildCount(); i++) {
            View view = llMediaTypes.getChildAt(i);
            if (view.getTag().equals(picturePath)) {
                llMediaTypes.removeView(view);
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        currentTakePictureName = "Picture-" + df.format(new Date()) + ".jpg";
        File file = new File(PicturePath + currentTakePictureName);
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, KeyStore.RequestCodeTakePicture);
    }

    protected void pickPhotoFromGallery() {
        Intent intent = new Intent(DiseaseDetailActivity.this,
                TestPicActivity.class);
        startActivityForResult(intent, KeyStore.RequestCodePickPicture);
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

    @Override
    public void finish() {
        super.finish();
        Bimp.clearData();
    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private int selectedPosition = -1;// 选中的位置
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            return (Bimp.bmp.size() + 1);
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        /**
         * ListView Item设置
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position < Bimp.bmp.size() && null != Bimp.bmp && Bimp.bmp.size() > 0) {
                holder.image.setVisibility(View.VISIBLE);
                holder.image.setImageBitmap(Bimp.bmp.get(position));
            } else {
                holder.image.setVisibility(View.GONE);
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mgvPicturesAdapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.drr.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            try {
                                String path = Bimp.drr.get(Bimp.max);
                                System.out.println(path);
                                Bitmap bm = Bimp.revitionImageSize(path);
                                Bimp.bmp.add(bm);
                                String newStr = path.substring(
                                        path.lastIndexOf("/") + 1,
                                        path.lastIndexOf("."));
                                FileUtils.saveBitmap(bm, "" + newStr);
                                Bimp.max += 1;
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }
}
