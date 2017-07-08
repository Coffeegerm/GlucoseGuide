package io.github.coffeegerm.materiallogbook.utils;

import java.util.ArrayList;
import java.util.List;

import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by David Yarzebinski on 7/6/2017.
 * <p>
 * Class made for various static methods used throughout code.
 */

public final class Utilities {

    // Static method to edit Time String in NewEntryActivity
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


    /*
    * Method used to sort Realm Entries by date.
    * */
    public static List getSortedRealmList() {
        Realm realm = Realm.getDefaultInstance();

        RealmQuery<EntryItem> entryQuery = realm.where(EntryItem.class);
        RealmResults<EntryItem> entryItems = entryQuery.findAllSorted("mDate", Sort.DESCENDING);
        List<EntryItem> realmEntries = new ArrayList<>(entryItems);

        return realmEntries;
    }

}
