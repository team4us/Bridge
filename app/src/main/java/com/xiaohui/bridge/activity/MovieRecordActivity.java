package com.xiaohui.bridge.activity;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.xiaohui.bridge.R;

import java.io.File;
import java.io.IOException;

/**
 * 录制视频界面
 * Created by Janzon on 2014/10/6.
 */
public class MovieRecordActivity extends AbstractActivity implements SurfaceHolder.Callback {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private boolean previewRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_record);

        surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private MediaRecorder mediaRecorder;
    private final int maxDurationInMs = 20000;
    private final long maxFileSizeInBytes = 500000;
    private final int videoFramesPerSecond = 20;
    private static String moviePath = Environment.getExternalStorageDirectory() + "/IBridge/Movie/";

    public void onStart(View v) {
        try {
            camera.unlock();

            mediaRecorder = new MediaRecorder();

            mediaRecorder.setCamera(camera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            mediaRecorder.setMaxDuration(maxDurationInMs);

            File cameraPhotoSaveDir = new File(moviePath + "1.3gp");


            if (!cameraPhotoSaveDir.exists()) {
                if (!cameraPhotoSaveDir.mkdirs()) {
                    Toast.makeText(this, "创建目录失败", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            mediaRecorder.setOutputFile(cameraPhotoSaveDir.getPath());

            mediaRecorder.setVideoFrameRate(videoFramesPerSecond);
            mediaRecorder.setVideoSize(surfaceView.getWidth(), surfaceView.getHeight());

            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

            mediaRecorder.setMaxFileSize(maxFileSizeInBytes);

            mediaRecorder.prepare();

            mediaRecorder.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onStop(View v) {
        mediaRecorder.stop();
        camera.lock();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();
        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            camera.setParameters(params);
        } else {
            Toast.makeText(getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (previewRunning) {
            camera.stopPreview();
        }
        Camera.Parameters p = camera.getParameters();
        p.setPreviewSize(width, height);
        p.setPreviewFormat(PixelFormat.JPEG);
        camera.setParameters(p);

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            previewRunning = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        previewRunning = false;
        camera.release();
    }
}
