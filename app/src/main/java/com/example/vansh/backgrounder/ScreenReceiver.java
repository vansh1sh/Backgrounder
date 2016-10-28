package com.example.vansh.backgrounder;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by vansh on 17-Oct-16.
 */

public class ScreenReceiver extends BroadcastReceiver {

    // THANKS JASON
    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

            // DO WHATEVER YOU NEED TO DO HERE
            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            // AND DO WHATEVER YOU NEED TO DO HERE
            wasScreenOn = true;
        }
    }

}