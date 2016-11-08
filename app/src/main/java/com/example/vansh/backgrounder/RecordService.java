package com.example.vansh.backgrounder;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by vansh on 08-Nov-16.
 */

public class RecordService extends Service {

    private static final String TAG = "RecorderService";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private static Camera mServiceCamera;
    private boolean mRecordingStatus;
    private MediaRecorder mMediaRecorder;


    public RecordService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRecordingStatus = false;
        mSurfaceView = CameraRecorder.mSurfaceView;
        mServiceCamera=Camera.open(1);
        mSurfaceHolder = CameraRecorder.mSurfaceHolder;
        Log.i(TAG, "onCreate: camera opened");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mRecordingStatus == false)
            startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        stopRecording();
        mRecordingStatus = false;

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public boolean startRecording() {
        Log.i(TAG, "startRecording: ");
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        //As soon as the screen is off i.e. sleep mode is active, start recording service will activate


        wl.acquire();

// screen and CPU will stay awake during this section
        //as soon as the screen is turned on it will terminate the wake lock and the recording will stop

        wl.release();
        Log.i(TAG, "startRecording: wlrelease");

        try {
            Toast.makeText(getBaseContext(), "Recording Started", Toast.LENGTH_SHORT).show();

            //mServiceCamera = Camera.open();
            Camera.Parameters params = mServiceCamera.getParameters();
            mServiceCamera.setParameters(params);
            Camera.Parameters p = mServiceCamera.getParameters();

            final List<Camera.Size> listPreviewSize = p.getSupportedPreviewSizes();
            for (Camera.Size size : listPreviewSize) {
                Log.i(TAG, String.format("Supported Preview Size (%d, %d)", size.width, size.height));
            }

            Camera.Size previewSize = listPreviewSize.get(0);
            p.setPreviewSize(previewSize.width, previewSize.height);
            mServiceCamera.setParameters(p);

            try {
                mServiceCamera.setPreviewDisplay(mSurfaceHolder);
                mServiceCamera.startPreview();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

            mServiceCamera.unlock();

            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setCamera(mServiceCamera);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + "/video.mp4");
            mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

            mMediaRecorder.prepare();
            mMediaRecorder.start();

            Log.i(TAG, "startRecording: mediarecorder");
            mRecordingStatus = true;

            return true;

        } catch (IllegalStateException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
            return false;

        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void stopRecording() {
        Toast.makeText(getBaseContext(), "Recording Stopped", Toast.LENGTH_SHORT).show();
        try {
            if(mServiceCamera!=null) {
                mServiceCamera.reconnect();

                mMediaRecorder.stop();
                mMediaRecorder.reset();

                mServiceCamera.stopPreview();
                mMediaRecorder.release();

                mServiceCamera.release();
                mServiceCamera = null;
            }else{
                Log.i(TAG, "stopRecording: mservicecamera null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
