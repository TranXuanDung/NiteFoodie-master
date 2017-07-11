package com.t3h.nitefoodie.ui;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.TimePicker;

import com.t3h.nitefoodie.model.TimeStore;

import java.util.Calendar;

public class Utils {
    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static TimeStore convertIntToTime(int time) {
        int hour = time / 60;
        int minute = time - hour * 60;
        return new TimeStore(hour, minute);
    }

    public static int convertTimeToInt(TimeStore timeStore) {
        return timeStore.getHour() * 60 + timeStore.getMinute();
    }

    public static TimeStore getTimeInTimePicker(TimePicker timePicker) {
        int hour = 0;
        int min = 0;

        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            hour = timePicker.getHour();
            min = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            min = timePicker.getCurrentMinute();
        }
        return new TimeStore(hour, min);
    }

    public static boolean isStoreOpen(int openTime, int closeTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int time = hour * 60 + minute;

        if ((time >= openTime && time <= closeTime) || (time <= openTime && time <= closeTime)) {
            return true;
        } else {
            return false;
        }
    }

}
