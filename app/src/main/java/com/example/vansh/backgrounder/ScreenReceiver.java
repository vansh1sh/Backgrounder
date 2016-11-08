package com.example.vansh.backgrounder;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by vansh on 17-Oct-16.
 */

public class ScreenReceiver extends BroadcastReceiver {

    private static final String TAG = ScreenReceiver.class.getCanonicalName();
    public static boolean wasScreenOn = true;
    Intent i;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: patanahi");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i(TAG, "onReceive: true");
            // DO WHATEVER YOU NEED TO DO HERE
            i = new Intent(context, RecordService.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Settings.canDrawOverlays(context);
            }
            context.startService(i);
            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.i(TAG, "onReceive: false");
            // AND DO WHATEVER YOU NEED TO DO HERE
            wasScreenOn = true;
            context.stopService(i);
        }
    }

}