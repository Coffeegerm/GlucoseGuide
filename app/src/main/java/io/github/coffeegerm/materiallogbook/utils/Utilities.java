package io.github.coffeegerm.materiallogbook.utils;

/**
 * Created by David Yarzebinski on 7/6/2017.
 * <p>
 * Class made for various methods used throughout code.
 */

public final class Utilities {

    public static String checkTimeString(int hourOfDay, int minute) {
        int hour = hourOfDay;
        String timeSet = "";
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

        String min = "";
        if (minute < 10)
            min = "0" + minute;
        else
            min = String.valueOf(minute);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hour).append(':')
                .append(min).append(" ").append(timeSet).toString();

        return aTime;
    }

}
