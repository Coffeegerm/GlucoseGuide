package io.github.coffeegerm.materiallogbook.utils;

import android.os.Build;

/**
 * Created by David Yarzebinski on 7/6/2017.
 * <p>
 * Class made for various static methods used throughout code.
 */

public final class Utilities {

    // Static method to edit Time String in NewEntryActivity
    public static String checkTimeString(int hourOfDay, int minute) {
        int hour = hourOfDay;
        String timeSet;
        String min;

        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        if (minute < 10)
            min = "0" + minute;
        else
            min = String.valueOf(minute);

        return hour + ":" + min + " " + timeSet;
    }

    public static boolean isKitKatPlus() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean isLollipopPlus() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}