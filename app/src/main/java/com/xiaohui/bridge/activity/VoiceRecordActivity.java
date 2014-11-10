package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.business.BusinessManager;

import org.joda.time.DateTime;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 录音管理界面
 * Created by Administrator on 2014/10/7.
 */
public class VoiceRecordActivity extends AbstractActivity {
    private TextView tvVoiceTime;
    private Button btnStart;
    private MediaRecorder mVoiceRecorder;
    private Timer timer = new Timer();
    private int time = 0;
    private String filePath;

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            time++;
            timeHandler.sendEmptyMessage(0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_record);

        tvVoiceTime = (TextView) findViewById(R.id.tv_voice_time);
        btnStart = (Button) findViewById(R.id.btn_start);
    }

    public void onStart(View v) {
        try {
            String name= "voice_" + DateTime.now().toString("yyyyMMddHHmmss") + ".amr";
            filePath = getGlobalApplication().getCachePathForVoice() + name;
            File myRecAudioFile = new File(filePath);
            mVoiceRecorder = new MediaRecorder();
            mVoiceRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mVoiceRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mVoiceRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mVoiceRecorder.setOutputFile(myRecAudioFile.getAbsolutePath());
            mVoiceRecorder.prepare();
            mVoiceRecorder.start();

            btnStart.setTextColor(getResources().getColor(R.color.color_999999));
            btnStart.setEnabled(false);

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

    public void onStop(View v) {
        if (null != mVoiceRecorder) {
            mVoiceRecorder.stop();
            mVoiceRecorder.release();
            mVoiceRecorder = null;
            finish();
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(Keys.Content, filePath);
        setResult(TextUtils.isEmpty(filePath) ? RESULT_CANCELED : RESULT_OK, intent);
        super.finish();
    }
}
