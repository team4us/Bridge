package com.xiaohui.bridge.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaohui.bridge.BuildConfig;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.bean.Disease;
import com.xiaohui.bridge.business.enums.EDiseaseMethod;
import com.xiaohui.bridge.component.DataAdapter;
import com.xiaohui.bridge.component.MyGridView;
import com.xiaohui.bridge.component.PickPicture.PhotoActivity;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.model.DiseaseModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.util.DeviceParamterUtil;
import com.xiaohui.bridge.view.IDiseaseView;
import com.xiaohui.bridge.viewmodel.DiseaseViewModel;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xhChen on 14/10/29.
 */
public class DiseaseActivity extends AbstractOrmLiteActivity<DatabaseHelper> implements IDiseaseView {

    public static final int MODE_NEW = 0;
    public static final int MODE_EDIT = 1;
    public static final int MODE_CHECK = 2;

    private static final int MAX_PICTURE_COUNT = 9;
    private final int iconWidth = DeviceParamterUtil.dip2px(60);
    private RadioGroup rgMethods;
    private LinearLayout llMethodView;
    private Spinner spLocations;
    private Spinner spDiseaseType;
    private View methodView;
    private EditText etComment;
    private EditText etOther;
    private LinearLayout llVoice;
    private LinearLayout llVideo;
    private View viewPicture;
    private View viewVoice;
    private View viewVideo;
    private DiseaseViewModel viewModel;
    private EDiseaseMethod currentMethod;
    private DiseaseModel diseaseModel;
    private Map<String, String> methodValues;
    private List<String> pictureList;
    private List<String> voiceList;
    private List<String> videoList;
    private DisplayImageOptions options;
    private int mode;
    private boolean isOther;
    private DataAdapter<String> pictureAdapter;
    private String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = getIntent().getIntExtra(Keys.MODE, MODE_NEW);
        viewModel = new DiseaseViewModel(this, getCookie());
        setContentView(R.layout.activity_disease, viewModel);
        setTitle(mode == MODE_NEW ? "病害新增" : "病害编辑");
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_no_picture)
                .showImageForEmptyUri(R.drawable.icon_no_picture)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        initViews();
        fillData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCookie().remove(Keys.DISEASE);
        ImageLoader.getInstance().stop();
    }

    @Override
    public void updateMethodView(boolean isOther, int method) {
        this.isOther = isOther;
        etOther.setVisibility(isOther ? View.VISIBLE : View.GONE);
        int radioButtonId = -1;
        int count = 0;
        for (int i = 0; i < 6; i++) {
            EDiseaseMethod diseaseMethod = EDiseaseMethod.values()[i];
            RadioButton rb = (RadioButton) findViewById(diseaseMethod.getRadioButtonResId());
            if ((method & (1 << i)) == 0) {
                rb.setVisibility(View.GONE);
            } else {
                count++;
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

        if (count > 1) {
            rgMethods.setVisibility(View.VISIBLE);
        } else {
            rgMethods.setVisibility(View.GONE);
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
        if (pictureList.size() >= MAX_PICTURE_COUNT) {
            Toast.makeText(this, R.string.more_than_nine, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = "pic_" + DateTime.now().toString("yyyyMMddHHmmss") + ".jpg";
        picturePath = getGlobalApplication().getCachePathForPicture() + fileName;
        File file = new File(picturePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, Keys.RequestCodeTakePicture);
    }

    @Override
    public void pickPictures() {
        if (pictureList.size() >= MAX_PICTURE_COUNT) {
            Toast.makeText(this, R.string.more_than_nine, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, PictureSelectActivity.class);
        intent.putExtra(Keys.PictureCount, MAX_PICTURE_COUNT - pictureList.size()); //还可以选择多少张
        startActivityForResult(intent, Keys.RequestCodePickPicture);
    }

    @Override
    public void takeVoice() {
        Intent intent = new Intent(this, VoiceRecordActivity.class);
        startActivityForResult(intent, Keys.RequestCodeTakeVoice);
    }

    @Override
    public void takeMovie() {
        Intent intent = new Intent(this, MovieRecordActivity.class);
        startActivityForResult(intent, Keys.RequestCodeTakeVideo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case Keys.RequestCodeTakePicture:
                onResultTakePhoto();
                break;
            case Keys.RequestCodePickPicture:
                onResultPickPictures(data);
                break;
            case Keys.RequestCodeTakeVoice:
                onResultTakeVoice(data);
                break;
            case Keys.RequestCodeTakeVideo:
                onResultTakeVideo(data);
                break;
            case Keys.RequestCodeCoordinate:
                onResultCoordinate(data);
                break;
        }
    }

    private void onResultTakePhoto() {
        pictureList.add(picturePath);
        pictureAdapter.setContent(pictureList);
        pictureAdapter.notifyDataSetChanged();
        viewPicture.setVisibility(View.VISIBLE);
    }

    private void onResultPickPictures(Intent data) {
        pictureList.addAll(data.getStringArrayListExtra(Keys.Content));
        pictureAdapter.setContent(pictureList);
        pictureAdapter.notifyDataSetChanged();
        viewPicture.setVisibility(View.VISIBLE);
    }

    private void onResultTakeVoice(Intent data) {
        if (data == null)
            return;
        Bundle bundle = data.getExtras();
        String recordPath = bundle.getString(Keys.Content);
        if (!TextUtils.isEmpty(recordPath)) {
            addMediaFile(recordPath, true);
        }
    }

    private void onResultTakeVideo(Intent data) {
        if (data == null)
            return;
        Bundle bundle = data.getExtras();
        String videoPath = bundle.getString(Keys.Content);
        if (!TextUtils.isEmpty(videoPath)) {
            addMediaFile(videoPath, false);
        }
    }

    private void onResultCoordinate(Intent data) {
        if (data == null)
            return;
//                View view = methodView.findViewById(R.id.et_startpoint);
//                if (view != null) {
//                    String startPoint = random() + "," + random();
//                    ((EditText) view).setText(startPoint);
//
//                    EditText editText = (EditText) methodView.findViewById(R.id.et_endpoint);
//                    String endPoint = random() + "," + random();
//                    editText.setText(endPoint);
//                } else {
//                    view = methodView.findViewById(R.id.et_position);
//                    if (view != null) {
//                        EditText editText = (EditText) view;
//                        String endPoint = random() + "," + random();
//                        editText.setText(endPoint);
//                    }
//                }
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
        viewPicture = findViewById(R.id.ll_photo);
        viewVoice = findViewById(R.id.ll_voice);
        viewVideo = findViewById(R.id.ll_video);
        llVoice = (LinearLayout) findViewById(R.id.ll_voice_record);
        llVideo = (LinearLayout) findViewById(R.id.ll_video_record);
        spLocations = (Spinner) findViewById(R.id.sp_locations);
        spDiseaseType = (Spinner) findViewById(R.id.sp_disease_type);

        MyGridView mgvPicturesGridView = (MyGridView) findViewById(R.id.mgv_pictures);
        mgvPicturesGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        pictureAdapter = new DataAdapter<String>(this, new DataAdapter.IViewCreator<String>() {
            @Override
            public View createView(LayoutInflater inflater, ViewGroup parent) {
                return inflater.inflate(R.layout.view_picture_item, parent, false);
            }

            @Override
            public void bindDataToView(View view, String data, int position) {
                ImageView imageView = (ImageView) view.findViewById(R.id.iv_picture);
                ImageLoader.getInstance().displayImage("file://" + data, imageView, options);
            }
        });
        pictureAdapter.setContent(pictureList);
        mgvPicturesGridView.setAdapter(pictureAdapter);
        mgvPicturesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(DiseaseActivity.this, PhotoActivity.class);
                intent.putExtra("ID", arg2);
                startActivity(intent);
            }
        });

        spLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.onItemClickLocation(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private void initMediaResource() {


//            pictureList = diseaseModel.getDisease().getPictureList();
//            voiceList = diseaseModel.getDisease().getVoiceList();
//            videoList = diseaseModel.getDisease().getVideoList();
//            Bimp.drr = pictureList;
//            mgvPicturesAdapter.update();
//            for (int i = 0; i < voiceList.size(); i++) {
//                addMediaFile(voiceList.get(i), true);
//            }
//            for (int i = 0; i < videoList.size(); i++) {
//                addMediaFile(diseaseModel.getDisease().getVideoList().get(i), false);
//            }
//        }
    }

    private void fillData() {
        if (mode == MODE_NEW) {
            diseaseModel = new DiseaseModel();
            diseaseModel.setComponent((ComponentModel) getCookie().get(Keys.COMPONENT));
            viewModel.onItemClickDiseaseType(0);
            methodValues = new HashMap<String, String>();
            pictureList = new ArrayList<String>();
            voiceList = new ArrayList<String>();
            videoList = new ArrayList<String>();
        } else {
            diseaseModel = (DiseaseModel) getCookie().get(Keys.DISEASE);
            Disease disease = diseaseModel.getDisease();
            pictureList = disease.getPictureList();
            if (pictureList == null) {
                pictureList = new ArrayList<String>();
            }

            voiceList = disease.getVoiceList();
            if (voiceList == null) {
                voiceList = new ArrayList<String>();
            }

            videoList = disease.getVideoList();
            if (videoList == null) {
                videoList = new ArrayList<String>();
            }

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
        diseaseModel.setTime(DateTime.now().getMillis());

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
        addPhotoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mediaPath = (String) v.getTag();
                // 这里不是视频就是音频文件了
                if (mediaPath.contains("Video")) {
                    Uri uri = Uri.parse("file://" + mediaPath);
                    //调用系统自带的播放器
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "video/3gpp");
                    startActivity(intent);
                } else {
                    MediaPlayer player = new MediaPlayer();
                    try {
                        player.setDataSource(mediaPath);
                        player.prepare();
                        player.start();
                    } catch (IOException e) {
                        Toast.makeText(DiseaseActivity.this, "播放录音失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        addPhotoIcon.setTag(filePath);
        addPhotoIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DiseaseActivity.this);
                builder.setMessage("确认删除该多媒体文件吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (v.getTag().toString().contains("Voice")) {
                            llVoice.removeView(v);
                            voiceList.remove(filePath);
                        } else {
                            llVideo.removeView(v);
                            videoList.remove(filePath);
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
            llVoice.setLayoutParams(layoutLP);
            addPhotoIcon.setBackgroundResource(R.drawable.icon_voice);
            llVoice.addView(addPhotoIcon, addIconLP);
            if (llVoice.getChildCount() > 0) {
                viewVoice.setVisibility(View.VISIBLE);
            } else {
                viewVoice.setVisibility(View.GONE);
            }
            voiceList.add(filePath);
        } else {
            llVideo.setLayoutParams(layoutLP);
            addPhotoIcon.setBackgroundResource(R.drawable.icon_vedio);
            llVideo.addView(addPhotoIcon, addIconLP);
            if (llVideo.getChildCount() > 0) {
                viewVideo.setVisibility(View.VISIBLE);
            } else {
                viewVideo.setVisibility(View.GONE);
            }
            videoList.add(filePath);
        }
    }
}
