package com.example.vansh.backgrounder;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by vansh on 17-Oct-16.
 */

public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Intent it = new Intent(context, RecorderService.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(it);
            System.out.println("SCREEN TURNED OFF");
            // DO WHATEVER YOU NEED TO DO HERE
            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            context.stopService(new Intent(context, RecorderService.class));

            // THIS IS WHEN ONRESUME() IS CALLED DUE TO A SCREEN STATE CHANGE
            System.out.println("SCREEN TURNED ON");}

            // AND DO WHATEVER YOU NEED TO DO HERE
            wasScreenOn = true;
        }
    }

