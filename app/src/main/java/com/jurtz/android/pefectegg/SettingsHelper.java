package com.jurtz.android.pefectegg;

import android.app.NotificationManager;

public class SettingsHelper {
    public static final int NOTIFICATION_ID = 65124;
    public static final int ALARM_ID = 94172;
    public static final String EXTRA_COOKINGTIME = "cooking_time";
    public static final String EXTRA_CANCEL_ALARM = "cancel_alarm";

    public static final int TEMP_DEG_FRIDGE = 4;
    public static final int TEMP_DEG_ROOM = 21;

    public static final int TEMP_DEG_SOFT = 62;
    public static final int TEMP_DEG_MEDIUM = 72;
    public static final int TEMP_DEG_HARD = 82;

    public static void cancelNotification(NotificationManager nm) {
        nm.cancel(SettingsHelper.NOTIFICATION_ID);
    }
}
