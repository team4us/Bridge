package com.xiaohui.bridge.activity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.business.BusinessManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 录制视频界面
 * Created by Janzon on 2014/10/6.
 */
public class MovieRecordActivity extends AbstractActivity implements View.OnClickListener {
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera.CameraInfo[] mCameraInfo;
    private Button startButton;
    private boolean mIsRecording = false;
    private MediaRecorder mediaRecorder;
    private String fileName = "";

    private static String videoPath = BusinessManager.USER_MEDIA_FILE_PATH + "Movie/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_record);
        setTitle("视频录制");

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseCamera();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initpreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {

            }
        });
        startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(this);
    }

    protected void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    protected void initpreview() {
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setCameraDisplayOrientation(this, Camera.CameraInfo.CAMERA_FACING_BACK, mCamera);
        mCamera.startPreview();
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.start:

                if (mIsRecording == false) {
                    startmediaRecorder();
                } else {
                    stopmediaRecorder();
                }
                if (mIsRecording) {
                    startButton.setText("stop");
                } else {
                    startButton.setText("start");
                }
                break;
            default:
                break;
        }

    }

    private void stopmediaRecorder() {
        if (mediaRecorder != null) {
            if (mIsRecording) {
                mediaRecorder.stop();
                //mCamera.lock();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
                mIsRecording = false;
                try {
                    mCamera.reconnect();
                } catch (IOException e) {
                    Toast.makeText(this, "reconect fail", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent();
            intent.putExtra(Keys.KeyContent, fileName);
            setResult(RESULT_OK, intent);
            this.finish();
        }
    }

    private void startmediaRecorder() {
        mCamera.unlock();
        mIsRecording = true;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//      mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//      mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//      mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//      mediaRecorder.setOutputFile(getName());
//      mediaRecorder.setVideoFrameRate(5);
//      mediaRecorder.setVideoSize(640, 480);
        CamcorderProfile mCamcorderProfile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_BACK, CamcorderProfile.QUALITY_LOW);
        mediaRecorder.setProfile(mCamcorderProfile);
        fileName = getName();
        mediaRecorder.setOutputFile(fileName);
        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();

//      mediaRecorder.set
        } catch (Exception e) {

            mIsRecording = false;
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            mCamera.lock();
        }

    }

    private String getName() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        File recordSaveDir = new File(videoPath);

        if (!recordSaveDir.exists()) {
            if (!recordSaveDir.mkdirs()) {
                Toast.makeText(this, "创建视频文档目录失败", Toast.LENGTH_SHORT).show();
                return "";
            }
        }

        return videoPath + "Video" + df.format(new Date()) + ".3gp";
    }

}

