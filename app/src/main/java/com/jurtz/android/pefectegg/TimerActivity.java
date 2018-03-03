package com.jurtz.android.pefectegg;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public class TimerActivity extends AppCompatActivity {

    TextView lblTime;
    Button cmdStartTimer;
    int cookingTime;

    NotificationCompat.Builder notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                cookingTime = 0;
            } else {
                cookingTime = extras.getInt(SettingsHelper.EXTRA_COOKINGTIME);
                boolean cancel = extras.getBoolean(SettingsHelper.EXTRA_CANCEL_ALARM, false);
                if (cancel) {
                    new AlertDialog.Builder(this, R.style.dialog)
                            .setTitle(getString(R.string.cancel_confirmation_dialog_title))
                            .setMessage(getString(R.string.cancel_confirmation_dialog_text))
                            .setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int clickedButton) {
                                    finish();
                                }
                            })
                            .setPositiveButton(getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int clickedButton) {
                                    stopAlert();
                                    SettingsHelper.cancelNotification((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
                                }
                            }).show();
                }
            }
        } else {
            cookingTime = (int) savedInstanceState.getSerializable(SettingsHelper.EXTRA_COOKINGTIME);
        }

        // Test
        cookingTime = 10;

        lblTime = (TextView) findViewById(R.id.lblTimer);
        int minutes = cookingTime / 60;
        int seconds = cookingTime % 60;
        lblTime.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, cookingTime);
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        notification = new NotificationCompat.Builder(this);
        //notification.setAutoCancel(true); -- Disable auto hide

        cmdStartTimer = (Button) findViewById(R.id.cmdStartTimer);
        cmdStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlert(cookingTime);
                loadNotification(sdf.format(calendar.getTime()));
                finish();
            }
        });
    }

    public void startAlert(int seconds) {
        Intent intent = new Intent(this, CustomBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), SettingsHelper.NOTIFICATION_ID, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (seconds * 1000), pendingIntent);
    }

    public void stopAlert() {
        PendingIntent alarmIntent;
        alarmIntent = PendingIntent.getBroadcast(this, SettingsHelper.NOTIFICATION_ID,
                new Intent(this, CustomBroadcastReceiver.class),
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(alarmIntent);
    }

    private void loadNotification(String formattedAlarmTime) {
        notification.setSmallIcon(getNotificationIcon());
        notification.setTicker(getString(R.string.notification_ticker_text));
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(getString(R.string.app_name));
        notification.setContentText(getString(R.string.notification_content) + " " + formattedAlarmTime);

        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra(SettingsHelper.EXTRA_CANCEL_ALARM, true);
        intent.putExtra(SettingsHelper.EXTRA_COOKINGTIME, cookingTime);
        notification.setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(SettingsHelper.NOTIFICATION_ID, notification.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notification_icon : R.drawable.notification_icon;
        // TODO return useWhiteIcon ? R.drawable.icon_silhouette : R.drawable.ic_launcher;
    }
}
