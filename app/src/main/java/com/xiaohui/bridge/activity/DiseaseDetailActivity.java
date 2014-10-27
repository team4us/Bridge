package com.xiaohui.bridge.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.xiaohui.bridge.BuildConfig;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.enums.EDiseaseInputMethod;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.business.store.StoreManager;
import com.xiaohui.bridge.model.DiseasesModel;
import com.xiaohui.bridge.util.DeviceParamterUtil;
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

    public static String PicturePath = Environment.getExternalStorageDirectory() + "/IBridge/Picture/";
    private static String AddPhotoTag = "AddPhoto";
    private static String AddPictureTag = "AddPicture";
    private static String AddVoiceTag = "AddVoice";
    private static String AddVideoTag = "AddVideo";

    protected boolean isHaveTag = false;

    private LinearLayout llMediaTypes;
    private LinearLayout llVoiceRecrds;
    private LinearLayout llVideoRecrds;
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
    private MediaPlayer mPlayer = null;

    private int selectIndex = -1;
    private ArrayList<String> picturesList = new ArrayList<String>();
    private ArrayList<String> recordsList = new ArrayList<String>();
    private ArrayList<String> videosList = new ArrayList<String>();

    private boolean isNewDisease = false;
    private String componentName;
    private String positionName;

    private DiseasesModel diseaseDetail;
    private View inputTemplateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);
        isNewDisease = getIntent().getBooleanExtra(Keys.FLAG, true);
        selectIndex = getIntent().getIntExtra(Keys.KeySelectedIndex, -1);
        setTitle(isNewDisease ? "病害新增" : "病害编辑");

        componentName = getIntent().getExtras().getString(Keys.KeySelectedComponentName);
        positionName = getIntent().getExtras().getString(Keys.KeySelectedPositionName);
        if(!isNewDisease) {
            diseaseDetail = StoreManager.Instance.getDiseasesList().get(selectIndex);
        }

        if(isNewDisease){
            initInputTemplateView(0);
        } else {
            initInputTemplateView(diseaseDetail.getInputMethod().getResID());
        }

        llMediaTypes = (LinearLayout) findViewById(R.id.ll_media_types);
        llVoiceRecrds = (LinearLayout) findViewById(R.id.ll_voice_record);
        llVideoRecrds = (LinearLayout) findViewById(R.id.ll_video_record);
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

        initDiseaseDetailView();
        initMediaLayout();
        initGridView();
    }

    private void initInputTemplateView(int resid){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inputTemplateView = inflater.inflate(resid == 0 ? R.layout.view_disease_input_1 : resid, null);
    }

    private void initDiseaseDetailView() {
        tvComponentName.setText(componentName);

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

        for (int i = 0; i < StoreManager.Instance.generalsTypes.length; i++) {
            if (positionName.equals(StoreManager.Instance.generalsTypes[i])) {
                spChoosePosition.setSelection(i);
                break;
            }
        }

        ArrayAdapter<String> diseases = new ArrayAdapter<String>(this, R.layout.view_spinner_item, StoreManager.Instance.diseaseTypes);
        diseases.setDropDownViewResource(R.layout.view_spinner_dropdown_item);
        spChooseDiseaseType.setAdapter(diseases);
        spChooseDiseaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    if(isNewDisease) {
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
                    if(isNewDisease) {
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

        if(!isNewDisease) {
            for (int i = 0; i < StoreManager.Instance.diseaseTypes.length; i++) {
                if (StoreManager.Instance.getDiseasesList().get(selectIndex).getDiseaseType().equals(StoreManager.Instance.diseaseTypes[i])) {
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
                if(!isNewDisease && null != diseaseDetail && nowInputMethod.getId() == diseaseDetail.getInputMethod().getId()){
                    String[] keys = nowInputMethod.getInputTitles();
                    for(int j = 0; j< keys.length ; j ++){
                        StringBuilder builder = new StringBuilder("et_");
                        builder.append(keys[j]);
                        EditText et = (EditText) inputTemplateView.findViewById(getResources().getIdentifier(builder.toString(), "id", BuildConfig.PACKAGE_NAME));
                        et.setText((String)diseaseDetail.getInputMethodValues().get(keys[j]));
                    }
                }
                int imageid = getResources().getIdentifier("btn_add_position_from_screen", "id", BuildConfig.PACKAGE_NAME);
                if(imageid > 0){
                    ImageView iv = (ImageView) inputTemplateView.findViewById(imageid);
                    if(null != iv) {
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(DiseaseDetailActivity.this, CoordinateActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        });

        if(!isNewDisease) {
            StringBuilder builder = new StringBuilder("rb_input_");
            builder.append(diseaseDetail.getInputMethod().getId());
            int xx = getResources().getIdentifier(builder.toString(), "id", BuildConfig.PACKAGE_NAME);
            rgRadioGroup.check(xx);
        }
    }

    private void saveDiseaseDetail(){
//        if(isNewDisease){
//            diseaseDetail = new DiseasesModel();
//            diseaseDetail.setComponentName(componentName);
//            diseaseDetail.setPosition(positionName);
//            diseaseDetail.setInputMethod((EDiseaseInputMethod)findViewById(rgRadioGroup.getCheckedRadioButtonId()).getTag());
//        }

        if(diseaseDetail.isHaveEmptyData()){
            Toast.makeText(this, "请输入全部数据！", Toast.LENGTH_SHORT).show();
            return ;
        }

        DiseasesModel diseasesModel = new DiseasesModel();
        diseasesModel.setComponentName(componentName);
        diseasesModel.setPosition(positionName);
        diseasesModel.setDiseaseType(StoreManager.Instance.diseaseTypes[spChooseDiseaseType.getSelectedItemPosition()]);
//        diseasesModel.set
//        diseasesModel.setDiseaseInputMethod(inputTemplate.getInputModel());
        diseasesModel.setPictureList(picturesList);
        diseasesModel.setRecordList(recordsList);
        diseasesModel.setVideoList(videosList);
        StoreManager.Instance.addDiseaseModel(diseasesModel);
        this.finish();
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
            // TODO 这里需要弹出个提示框问是否确定要退出，因为可能是误触
            Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initGridView() {
        MyGridView mgvPicturesGridView = (MyGridView) findViewById(R.id.mgv_pictures);
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
        LinearLayout.LayoutParams layoutLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, iconWidth);
        llMediaTypes.setLayoutParams(layoutLP);

        LinearLayout.LayoutParams addIconLP = new LinearLayout.LayoutParams(iconWidth, iconWidth);
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
        if(Bimp.drr.size() > 0){
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
                if (Bimp.drr.size() < 9 && resultCode == -1) {
                    Bimp.drr.add(path);
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
        }
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
    private void addMediaFile(String filePath, boolean isVoice) {
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
                        if(v.getTag().toString().contains("Voice")){
                            llVoiceRecrds.removeView(v);
                        } else {
                            llVideoRecrds.removeView(v);
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
            llVoiceRecrds.setLayoutParams(layoutLP);
            addPhotoIcon.setBackgroundResource(R.drawable.icon_voice);
            llVoiceRecrds.addView(addPhotoIcon, addIconLP);
            if(llVoiceRecrds.getChildCount() > 0){
                viewVoiceDivider.setVisibility(View.VISIBLE);
            } else {
                viewVoiceDivider.setVisibility(View.GONE);
            }
        } else {
            llVideoRecrds.setLayoutParams(layoutLP);
            addPhotoIcon.setBackgroundResource(R.drawable.icon_vedio);
            llVideoRecrds.addView(addPhotoIcon, addIconLP);
            if(llVideoRecrds.getChildCount() > 0){
                viewVideoDivider.setVisibility(View.VISIBLE);
            } else {
                viewVideoDivider.setVisibility(View.GONE);
            }
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
                    mPlayer = new MediaPlayer();
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
