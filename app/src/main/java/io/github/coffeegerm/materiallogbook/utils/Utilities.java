package io.github.coffeegerm.materiallogbook.utils;

import java.util.Date;

import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.realm.Realm;
import io.realm.RealmResults;

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

    public static int getHighestGlucose(Date providedDate) {
        Realm realm = Realm.getDefaultInstance();
        int highest = 0;
        RealmResults<EntryItem> entriesFromLastThreeMonths = realm.where(EntryItem.class).greaterThan("date", providedDate).greaterThan("bloodGlucose", 0).findAll();
        for (int position = 0; position < entriesFromLastThreeMonths.size(); position++) {
            EntryItem currentItem = entriesFromLastThreeMonths.get(position);
            if (currentItem.getBloodGlucose() > highest) {
                highest = currentItem.getBloodGlucose();
            }
        }
        return highest;
    }

    public static int getAverageGlucose(Date providedDate) {
        Realm realm = Realm.getDefaultInstance();
        int total = 0;
        RealmResults<EntryItem> entriesFromLastThreeMonths = realm.where(EntryItem.class).greaterThan("date", providedDate).greaterThan("bloodGlucose", 0).findAll();
        for (int position = 0; position < entriesFromLastThreeMonths.size(); position++) {
            EntryItem currentItem = entriesFromLastThreeMonths.get(position);
            total += currentItem.getBloodGlucose();
        }
        return total / entriesFromLastThreeMonths.size();
    }

    public static int getLowestGlucose(Date providedDate) {
        Realm realm = Realm.getDefaultInstance();
        int lowest = 1000;
        RealmResults<EntryItem> entriesFromLastThreeDays = realm.where(EntryItem.class).greaterThan("date", providedDate).greaterThan("bloodGlucose", 0).findAll();
        for (int position = 0; position < entriesFromLastThreeDays.size(); position++) {
            EntryItem currentItem = entriesFromLastThreeDays.get(position);
            if (currentItem.getBloodGlucose() < lowest) {
                lowest = currentItem.getBloodGlucose();
            }
        }
        return lowest;
    }
}