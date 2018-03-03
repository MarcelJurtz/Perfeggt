package com.jurtz.android.pefectegg;

import android.app.NotificationManager;

public class SettingsHelper {
    public static final int NOTIFICATION_ID = 65124;
    public static final int ALARM_ID = 94172;
    public static final String EXTRA_COOKINGTIME = "cooking_time";
    public static final String EXTRA_CANCEL_ALARM = "cancel_alarm";

    public static void cancelNotification(NotificationManager nm) {
        nm.cancel(SettingsHelper.NOTIFICATION_ID);
    }
}
