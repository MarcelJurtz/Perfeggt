package com.jurtz.android.pefectegg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class CustomBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent stopIntent = new Intent(context, CompletedActivity.class);
        context.startActivity(stopIntent);
    }
}
