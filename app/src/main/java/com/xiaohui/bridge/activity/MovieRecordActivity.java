package com.xiaohui.bridge.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xiaohui.bridge.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 录制视频界面
 * Created by Janzon on 2014/10/6.
 */
//public class MovieRecordActivity extends AbstractActivity implements SurfaceHolder.Callback {
//
//    private SurfaceView surfaceView;
//    private SurfaceHolder surfaceHolder;
//    private Camera camera;
//    private boolean previewRunning;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_movie_record);
//
//        surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
//        surfaceHolder = surfaceView.getHolder();
//        surfaceHolder.addCallback(this);
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//    }
//
//    private MediaRecorder mediaRecorder;
//    private final int maxDurationInMs = 20000;
//    private final long maxFileSizeInBytes = 500000;
//    private final int videoFramesPerSecond = 20;
//    private static String moviePath = Environment.getExternalStorageDirectory() + "/IBridge/Movie/";
//
//    public void onStart(View v) {
//        try {
////            camera.unlock();
//
//            mediaRecorder = new MediaRecorder();
//
//            mediaRecorder.setCamera(camera);
//            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//
//            mediaRecorder.setMaxDuration(maxDurationInMs);
//
//            File cameraPhotoSaveDir = new File(moviePath);
//            if (!cameraPhotoSaveDir.exists()) {
//                if (!cameraPhotoSaveDir.mkdirs()) {
//                    Toast.makeText(this, "创建目录失败", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//
//            mediaRecorder.setOutputFile(moviePath+"/1.3gp");
//
//            mediaRecorder.setVideoFrameRate(videoFramesPerSecond);
//            mediaRecorder.setVideoSize(surfaceView.getWidth(), surfaceView.getHeight());
//
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
//
//            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
//
//            mediaRecorder.setMaxFileSize(maxFileSizeInBytes);
//
//            // 加上这两句后recorder的start方法就不会报start failed错误，但是没有实时预览了
//            camera.stopPreview();
//            camera.release();
//
//            mediaRecorder.prepare();
//
//            mediaRecorder.start();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void onStop(View v) {
//        mediaRecorder.stop();
////        camera.lock();
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder surfaceHolder) {
//        camera = Camera.open();
//        if (camera != null) {
//            Camera.Parameters params = camera.getParameters();
//            camera.setParameters(params);
//        } else {
//            Toast.makeText(getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
//            finish();
//        }
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        if (previewRunning) {
//            camera.stopPreview();
//        }
//        Camera.Parameters p = camera.getParameters();
//        List<Camera.Size> previewSize = p.getSupportedPreviewSizes();
//        p.setPreviewSize(previewSize.get(0).width, previewSize.get(0).height);
////        p.setPreviewFormat(PixelFormat.JPEG);
//        camera.setParameters(p);
//
//        try {
//            camera.setPreviewDisplay(holder);
//            camera.startPreview();
//            previewRunning = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//        camera.stopPreview();
//        previewRunning = false;
//        camera.release();
//    }
//}
public class MovieRecordActivity extends AbstractActivity implements View.OnClickListener {
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera.CameraInfo[] mCameraInfo;
    private Button startButton;
    private boolean mIsRecording = false;
    private MediaRecorder mediaRecorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSurfaceView = (SurfaceView)findViewById(R.id.surfaceview);
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
        startButton = (Button)findViewById(R.id.start);
        startButton.setOnClickListener(this);
    }

    protected void releaseCamera() {
        if(mCamera!=null){
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setCameraDisplayOrientation(this, Camera.CameraInfo.CAMERA_FACING_BACK,mCamera);
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
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.start:

                if(mIsRecording==false) {
                    startmediaRecorder();
                }else{
                    stopmediaRecorder();
                }
                if(mIsRecording){
                    startButton.setText("stop");
                }else{
                    startButton.setText("start");
                }
                break;
            default:
                break;
        }

    }

    private void stopmediaRecorder() {
        if(mediaRecorder!=null){
            if(mIsRecording){
                mediaRecorder.stop();
                //mCamera.lock();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder=null;
                mIsRecording = false;
                try {
                    mCamera.reconnect();
                } catch (IOException e) {
                    Toast.makeText(this, "reconect fail", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
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
//		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//		mediaRecorder.setOutputFile(getName());
//		mediaRecorder.setVideoFrameRate(5);
//		mediaRecorder.setVideoSize(640, 480);
        CamcorderProfile mCamcorderProfile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_BACK, CamcorderProfile.QUALITY_LOW);
        mediaRecorder.setProfile(mCamcorderProfile);
        mediaRecorder.setOutputFile(getName());
        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();

//			mediaRecorder.set
        } catch (Exception e) {

            mIsRecording = false;
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            mCamera.lock();
        }

    }

    private String getName() {
//		String fileName = Environment.getExternalStorageDirectory()+""+System.currentTimeMillis()+".3gp";
        String fileName = Environment.getExternalStorageDirectory() + "/IBridge/Movie/"+System.currentTimeMillis()+".3gp";
        return fileName;
    }

}

