package com.jurtz.android.pefectegg;

import android.app.NotificationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CompletedActivity extends AppCompatActivity {

    Button cmdStopAlarmSound;
    Uri sound;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);

        getSupportActionBar().hide();

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if(sound == null){
            sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        SettingsHelper.cancelNotification((NotificationManager)getSystemService(NOTIFICATION_SERVICE));

        mp = MediaPlayer.create(getApplicationContext(), sound);
        mp.start();

        cmdStopAlarmSound = (Button)findViewById(R.id.cmdStopAlarm);
        cmdStopAlarmSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
            }
        });
    }
}
