package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.store.KeyStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 录音管理界面
 * Created by Administrator on 2014/10/7.
 */
public class VoiceRecordActivity extends AbstractActivity{
    private TextView tvVoiceTime;
    private Button btnStart;

    private static String recordPath = Environment.getExternalStorageDirectory() + "/IBridge/Record/";
    private MediaRecorder mVoiceRecorder;
    private String currentRecordName = "";
    private ArrayList<String> recordedList = new ArrayList<String>();
    private Timer timer  = new Timer();
    private int time = 0;
    private boolean canRecording = true;

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if(canRecording) {
                time++;
                timeHandler.sendEmptyMessage(0);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_record);

        tvVoiceTime = (TextView) findViewById(R.id.tv_voice_time);
        btnStart = (Button) findViewById(R.id.btn_start);
    }

    public void onStart(View v){
        try {
            File recordSaveDir = new File(recordPath);

            if (!recordSaveDir.exists()) {
                if (!recordSaveDir.mkdirs()) {
                    Toast.makeText(this, "创建录音文档目录失败", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            currentRecordName = "Voice" + df.format(new Date()) + ".amr";

            if(recordedList.contains(recordPath + currentRecordName)) {
                return;
            }

            File myRecAudioFile = new File(currentRecordName);

            mVoiceRecorder = new MediaRecorder();
            mVoiceRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mVoiceRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mVoiceRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mVoiceRecorder.setOutputFile(myRecAudioFile.getAbsolutePath());
            mVoiceRecorder.prepare();
            mVoiceRecorder.start();

            btnStart.setTextColor(getResources().getColor(R.color.color_999999));
            btnStart.setEnabled(false);
            canRecording = true;

            timer.schedule(timerTask, 0, 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tvVoiceTime.setText(String.format(getString(R.string.record_seconds), String.valueOf(time)));
        }
    };

    public void onStop(View v){
        if(null != mVoiceRecorder) {
            mVoiceRecorder.stop();
            mVoiceRecorder.release();
            mVoiceRecorder = null;
            if(!recordedList.contains(currentRecordName)) {
                recordedList.add(currentRecordName);
            }
            initStatus();
        }
    }

    private void initStatus(){
        canRecording = false;
        time = 0;
        currentRecordName = "";
        btnStart.setTextColor(getResources().getColor(R.color.color_333333));
        btnStart.setEnabled(true);
        timeHandler.sendEmptyMessage(0);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(KeyStore.KeyContent, recordedList);
        setResult(KeyStore.ResultCodeSuccess, intent);
        super.finish();
    }
}