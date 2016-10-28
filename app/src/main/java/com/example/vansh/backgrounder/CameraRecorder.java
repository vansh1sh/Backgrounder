package com.example.vansh.backgrounder;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class CameraRecorder extends Activity implements SurfaceHolder.Callback {

	private static final String TAG = CameraRecorder.class.getSimpleName();
	public static SurfaceView mSurfaceView;
	public static SurfaceHolder mSurfaceHolder;
	public static Camera mCamera;
	public static boolean mPreviewRunning;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		Button btnStart = (Button) findViewById(R.id.StartService);
		// INITIALIZE RECEIVER
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		BroadcastReceiver mReceiver = new ScreenReceiver();
		registerReceiver(mReceiver, filter);
		// YOUR CODE
		btnStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {



						Intent intent = new Intent(CameraRecorder.this, RecorderService.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					Settings.canDrawOverlays(CameraRecorder.this);
				}
						startService(intent);
						finish();
					}

		});

		Button btnStop = (Button) findViewById(R.id.StopService);
		btnStop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopService(new Intent(CameraRecorder.this, RecorderService.class));
			}
		});
}

	@Override
	protected void onPause() {
		// WHEN THE SCREEN IS ABOUT TO TURN OFF
		if (ScreenReceiver.wasScreenOn) {
			Intent intent = new Intent(CameraRecorder.this, RecorderService.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startService(intent);
			System.out.println("SCREEN TURNED OFF");
		} else {
			// THIS IS WHEN ONPAUSE() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// ONLY WHEN SCREEN TURNS ON
		if (!ScreenReceiver.wasScreenOn) {
			if (getIntent().getAction().equals(Intent.ACTION_SCREEN_ON)){
			stopService(new Intent(CameraRecorder.this, RecorderService.class));

			// THIS IS WHEN ONRESUME() IS CALLED DUE TO A SCREEN STATE CHANGE
			System.out.println("SCREEN TURNED ON");}
		} else {
			stopService(new Intent(CameraRecorder.this, RecorderService.class));

			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED

		}
		super.onResume();
	}




	public void requestCameraPermission() {


		// Camera permission has not been granted yet. Request it directly.
		ActivityCompat.requestPermissions(CameraRecorder.this, new String[]{Manifest.permission.CAMERA},
				1);
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}


}
