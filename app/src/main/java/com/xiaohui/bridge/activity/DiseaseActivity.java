package com.xiaohui.bridge.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xiaohui.bridge.BuildConfig;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.bean.Disease;
import com.xiaohui.bridge.business.enums.EDiseaseMethod;
import com.xiaohui.bridge.component.DataAdapter;
import com.xiaohui.bridge.component.MyGridView;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.model.DiseaseModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.util.ListUtil;
import com.xiaohui.bridge.util.MathUtil;
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
public class DiseaseActivity extends AbstractOrmLiteActivity<DatabaseHelper>
        implements IDiseaseView, View.OnClickListener {

    public static final int MODE_NEW = 0;
    public static final int MODE_EDIT = 1;
    public static final int MODE_CHECK = 2;

    public static final int MEDIA_PICTURE = 0;
    public static final int MEDIA_VOICE = 1;
    public static final int MEDIA_VIDEO = 2;

    private static final int MAX_PICTURE_COUNT = 9;
    private static final int MAX_VOICE_COUNT = 9;
    private static final int MAX_VIDEO_COUNT = 9;
    private RadioGroup rgMethods;
    private LinearLayout llMethodView;
    private EditText etComment;
    private EditText etOther;
    private View viewPicture;
    private View viewVoice;
    private View viewVideo;
    private Spinner spLocations;
    private Spinner spDiseaseType;
    private DiseaseViewModel viewModel;
    private DiseaseModel diseaseModel;
    private Map<String, String> methodValues;
    private List<String> pictureList;
    private List<String> voiceList;
    private List<String> videoList;
    private DisplayImageOptions options;
    private EDiseaseMethod currentMethod;
    private int mode;
    private boolean isOther;
    private DataAdapter<String> pictureAdapter;
    private DataAdapter<String> voiceAdapter;
    private DataAdapter<String> videoAdapter;
    private String picturePath;
    private Map<EDiseaseMethod, View> methodViews = new HashMap<EDiseaseMethod, View>();
    private List<RadioButton> radioButtons = new ArrayList<RadioButton>();
    private PointF pointStart;
    private PointF pointStop;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
        }
    };

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
                .showImageOnFail(R.drawable.icon_no_picture)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        initDiseaseModel();
        initViews();
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
            RadioButton rb = radioButtons.get(i);
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
                        radioButtonId = rb.getId();
                    }
                }
            }
        }

        if (count > 1) {
            rgMethods.setVisibility(View.VISIBLE);
        } else {
            rgMethods.setVisibility(View.GONE);
        }

        rgMethods.check(radioButtonId);
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
    public void onClick(View v) {
        if (v.getId() == R.id.iv_coordinate) {
            pickCoordinate();
        }
    }

    @Override
    public void takePhoto() {
        if (pictureList.size() >= MAX_PICTURE_COUNT) {
            Toast.makeText(this, R.string.more_than_nine_pictures, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, R.string.more_than_nine_pictures, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, PicturePickerActivity.class);
        intent.putExtra(Keys.PictureCount, MAX_PICTURE_COUNT - pictureList.size()); //还可以选择多少张
        startActivityForResult(intent, Keys.RequestCodePickPicture);
    }

    @Override
    public void takeVoice() {
        if (voiceList.size() >= MAX_VOICE_COUNT) {
            Toast.makeText(this, R.string.more_than_nine_voices, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, VoiceRecordActivity.class);
        startActivityForResult(intent, Keys.RequestCodeTakeVoice);
    }

    @Override
    public void takeVideo() {
        if (videoList.size() >= MAX_VIDEO_COUNT) {
            Toast.makeText(this, R.string.more_than_nine_videos, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, VideoRecordActivity.class);
        startActivityForResult(intent, Keys.RequestCodeTakeVideo);
    }

    public void pickCoordinate() {
        Intent intent = new Intent(this, CoordinateActivity.class);
        intent.putExtra(Keys.Content, currentMethod);
        intent.putExtra(Keys.PointStart, pointStart);
        intent.putExtra(Keys.PointStop, pointStop);
        startActivityForResult(intent, Keys.RequestCodeCoordinate);
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
            case Keys.RequestCodeRemovePicture:
                onResultRemovePicture(data);
                break;
        }
    }

    private void onResultTakePhoto() {
        pictureList.add(picturePath);
        updatePictureView();
    }

    private void onResultPickPictures(Intent data) {
        if (data == null)
            return;
        pictureList.addAll(data.getStringArrayListExtra(Keys.Content));
        updatePictureView();
    }

    private void onResultTakeVoice(Intent data) {
        if (data == null)
            return;
        voiceList.add(data.getExtras().getString(Keys.Content));
        updateVoiceView();
    }

    private void onResultTakeVideo(Intent data) {
        if (data == null)
            return;
        videoList.add(data.getStringExtra(Keys.Content));
        updateVideoView();
    }

    private void onResultCoordinate(Intent data) {
        if (data == null)
            return;
        pointStart = data.getParcelableExtra(Keys.PointStart);
        pointStop = data.getParcelableExtra(Keys.PointStop);
        View methodView = methodViews.get(currentMethod);
        if (currentMethod == EDiseaseMethod.MethodOne) {
            int id = getResources().getIdentifier("et_0", "id", BuildConfig.PACKAGE_NAME);
            EditText editText = (EditText) methodView.findViewById(id);
            editText.setText(String.format("%.1f, %.1f", pointStart.x, pointStart.y));

            id = getResources().getIdentifier("et_1", "id", BuildConfig.PACKAGE_NAME);
            editText = (EditText) methodView.findViewById(id);
            editText.setText(String.format("%.1f, %.1f", pointStop.x, pointStop.y));

            id = getResources().getIdentifier("et_2", "id", BuildConfig.PACKAGE_NAME);
            editText = (EditText) methodView.findViewById(id);
            editText.setText(String.format("%.1f", MathUtil.lengthWithTwoPoint(pointStart, pointStop)));
        } else if (currentMethod == EDiseaseMethod.MethodTwo) {
            int id = getResources().getIdentifier("et_0", "id", BuildConfig.PACKAGE_NAME);
            EditText editText = (EditText) methodView.findViewById(id);
            editText.setText(pointStart.x + "," + pointStart.y);
        }
    }

    private void onResultRemovePicture(Intent data) {
        if (data == null)
            return;
        List<Integer> removedList = data.getIntegerArrayListExtra(Keys.Content);
        for (Integer index : removedList) {
            pictureList.remove(index.intValue());
        }
        updatePictureView();
    }

    private void initDiseaseModel() {
        if (mode == MODE_NEW) {
            diseaseModel = new DiseaseModel();
            diseaseModel.setComponent((ComponentModel) getCookie().get(Keys.COMPONENT));
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
        }
    }

    private void initViews() {
        for (int i = 0; i < 6; i++) {
            EDiseaseMethod diseaseMethod = EDiseaseMethod.values()[i];
            int id = getResources().getIdentifier("rb_method_" + diseaseMethod.getId(), "id", BuildConfig.PACKAGE_NAME);
            RadioButton rb = (RadioButton) findViewById(id);
            rb.setTag(diseaseMethod);
            radioButtons.add(rb);
        }

        rgMethods = (RadioGroup) findViewById(R.id.rg_methods);
        rgMethods.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switchMethodView((EDiseaseMethod) findViewById(checkedId).getTag());
            }
        });
        llMethodView = (LinearLayout) findViewById(R.id.ll_method_view);
        etComment = (EditText) findViewById(R.id.et_comment);
        etOther = (EditText) findViewById(R.id.et_other);
        viewPicture = findViewById(R.id.ll_photo);
        viewVoice = findViewById(R.id.ll_voice);
        viewVideo = findViewById(R.id.ll_video);
        spLocations = (Spinner) findViewById(R.id.sp_locations);
        spDiseaseType = (Spinner) findViewById(R.id.sp_disease_type);
        if (mode != MODE_NEW) {
            initDataForEdit();
        } else {
            initDataForNew();
        }

        initMediaView();

        handler.sendEmptyMessageDelayed(0, 100);//由于Spinner的setSelection方法会延迟执行Listener方法，所以这样处理
    }

    private void initDataForEdit() {
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

        EDiseaseMethod method = EDiseaseMethod.valueOf(disease.getMethod());
        rgMethods.check(radioButtons.get(method.getId()).getId());
        View methodView = switchMethodView(method);
        int count = method.getCount();
        methodValues = disease.getValues();
        for (int i = 0; i < count; i++) {
            TextView textView = (TextView) methodView.findViewById(getResources().getIdentifier("tv_" + i, "id", BuildConfig.PACKAGE_NAME));
            String key = textView.getText().toString();
            String value = methodValues.get(key);

            EditText editText = (EditText) methodView.findViewById(getResources().getIdentifier("et_" + i, "id", BuildConfig.PACKAGE_NAME));
            editText.setText(value);

            if (method == EDiseaseMethod.MethodOne) {
                if (i == 0 && !TextUtils.isEmpty(value)) {
                    String[] values = value.split(",");
                    if (values.length == 2) {
                        pointStart = new PointF(Float.valueOf(values[0]), Float.valueOf(values[1]));
                    }
                }

                if (i == 1 && !TextUtils.isEmpty(value)) {
                    String[] values = value.split(",");
                    if (values.length == 2) {
                        pointStop = new PointF(Float.valueOf(values[0]), Float.valueOf(values[1]));
                    }
                }
            } else if (method == EDiseaseMethod.MethodTwo) {
                if (i == 0 && !TextUtils.isEmpty(value)) {
                    String[] values = value.split(",");
                    if (values.length == 2) {
                        pointStart = new PointF(Float.valueOf(values[0]), Float.valueOf(values[1]));
                        pointStop = pointStart;
                    }
                }
            }
        }

        etComment.setText(disease.getComment());
    }

    private void initDataForNew() {
        viewModel.onItemClickDiseaseType(0);
        viewModel.onItemClickLocation(0);
    }

    private void initMediaView() {
        MyGridView gvPicture = (MyGridView) findViewById(R.id.mgv_picture);
        gvPicture.setSelector(new ColorDrawable(Color.TRANSPARENT));
        pictureAdapter = new DataAdapter<String>(this, new IconViewCreator(MEDIA_PICTURE));
        gvPicture.setAdapter(pictureAdapter);
        gvPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(DiseaseActivity.this, PictureRemoverActivity.class);
                intent.putExtra(Keys.SelectedIndex, position);
                intent.putStringArrayListExtra(Keys.Content, new ArrayList<String>(pictureList));
                startActivityForResult(intent, Keys.RequestCodeRemovePicture);
            }
        });

        MyGridView gvVoice = (MyGridView) findViewById(R.id.mgv_voice);
        gvVoice.setSelector(new ColorDrawable(Color.TRANSPARENT));
        voiceAdapter = new DataAdapter<String>(this, new IconViewCreator(MEDIA_VOICE));
        gvVoice.setAdapter(voiceAdapter);
        gvVoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String mediaPath = voiceList.get(position);
                MediaPlayer player = new MediaPlayer();
                try {
                    player.setDataSource(mediaPath);
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    Toast.makeText(DiseaseActivity.this, "播放录音失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gvVoice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DiseaseActivity.this);
                builder.setMessage("确认要删除该音频吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        voiceList.remove(position);
                        updateVoiceView();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });

        MyGridView gvVideo = (MyGridView) findViewById(R.id.mgv_video);
        gvVideo.setSelector(new ColorDrawable(Color.TRANSPARENT));
        videoAdapter = new DataAdapter<String>(this, new IconViewCreator(MEDIA_VIDEO));
        gvVideo.setAdapter(videoAdapter);
        gvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String mediaPath = videoList.get(position);
                Uri uri = Uri.parse("file://" + mediaPath);
                //调用系统自带的播放器
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/3gpp");
                startActivity(intent);
            }
        });
        gvVideo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DiseaseActivity.this);
                builder.setMessage("确认要删除该视频吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        videoList.remove(position);
                        updateVideoView();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });

        updatePictureView();
        updateVoiceView();
        updateVideoView();
    }

    private void updatePictureView() {
        if (ListUtil.sizeOfList(pictureList) > 0) {
            viewPicture.setVisibility(View.VISIBLE);
            pictureAdapter.setContent(pictureList);
            pictureAdapter.notifyDataSetChanged();
        } else {
            viewPicture.setVisibility(View.GONE);
        }
    }

    private void updateVoiceView() {
        if (ListUtil.sizeOfList(voiceList) > 0) {
            viewVoice.setVisibility(View.VISIBLE);
            voiceAdapter.setContent(voiceList);
            voiceAdapter.notifyDataSetChanged();
        } else {
            viewVoice.setVisibility(View.GONE);
        }
    }

    private void updateVideoView() {
        if (ListUtil.sizeOfList(videoList) > 0) {
            viewVideo.setVisibility(View.VISIBLE);
            videoAdapter.setContent(videoList);
            videoAdapter.notifyDataSetChanged();
        } else {
            viewVideo.setVisibility(View.GONE);
        }
    }

    private View switchMethodView(EDiseaseMethod method) {
        currentMethod = method;
        llMethodView.removeAllViews();
        View view = methodViews.get(method);
        if (view == null) {
            view = getMethodView(method);
        }
        methodViews.put(method, view);
        llMethodView.addView(view);
        return view;
    }

    private View getMethodView(EDiseaseMethod method) {
        int id = getResources().getIdentifier("view_disease_method_" + method.getId(),
                "layout", BuildConfig.PACKAGE_NAME);
        View methodView = View.inflate(this, id, null);
        if (method == EDiseaseMethod.MethodOne || method == EDiseaseMethod.MethodTwo) {
            View view = methodView.findViewById(R.id.iv_coordinate);
            view.setTag(method);
            view.setOnClickListener(this);
        }
        return methodView;
    }

    private void save() {
        Disease disease = new Disease();
        methodValues.clear();
        disease.setLocation(viewModel.getLocation());
        if (isOther && !TextUtils.isEmpty(etOther.getText())) {
            disease.setType(etOther.getText().toString());
        } else {
            disease.setType(viewModel.getType());
        }
        disease.setComment(etComment.getText().toString());
        disease.setMethod(currentMethod.toString());
        View methodView = methodViews.get(currentMethod);
        if (methodView != null) {
            int count = currentMethod.getCount();
            for (int i = 0; i < count; i++) {
                TextView textView = (TextView) methodView.findViewById(getResources().getIdentifier("tv_" + i, "id", BuildConfig.PACKAGE_NAME));
                EditText editText = (EditText) methodView.findViewById(getResources().getIdentifier("et_" + i, "id", BuildConfig.PACKAGE_NAME));
                methodValues.put(textView.getText().toString(), editText.getText().toString());
            }
            disease.setValues(methodValues);
        }
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

    private final class IconViewCreator implements DataAdapter.IViewCreator<String> {

        private int mediaType;

        public IconViewCreator(int mediaType) {
            this.mediaType = mediaType;
        }

        @Override
        public View createView(LayoutInflater inflater, ViewGroup parent) {
            return inflater.inflate(R.layout.view_media_item, parent, false);
        }

        @Override
        public void bindDataToView(View view, String data, int position) {
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
            if (mediaType == MEDIA_PICTURE) {
                ImageLoader.getInstance().displayImage("file://" + data, imageView, options);
            } else if (mediaType == MEDIA_VIDEO) {
                imageView.setImageResource(R.drawable.icon_video);
            } else {
                imageView.setImageResource(R.drawable.icon_voice);
            }
        }
    }
}
