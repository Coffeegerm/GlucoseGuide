package io.github.coffeegerm.materiallogbook.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by dyarz on 10/6/2017.
 * <p>
 * Constants used throughout application.
 */

public class Constants {
    public static final String PREF_DARK_MODE = "pref_dark_mode";
    public static final String HYPERGLYCEMIC_INDEX = "hyperglycemicIndex";
    public static final String HYPOGLYCEMIC_INDEX = "hypoglycemicIndex";
    public static final String BOLUS_RATIO = "BOLUS_RATIO";
    public static final String MILITARY_TIME = "MILITARY_TIME";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    public static final SimpleDateFormat TWELVE_HOUR_TIME_FORMAT = new SimpleDateFormat("hh:mm aa", Locale.US);
    public static final SimpleDateFormat TWENTY_FOUR_HOUR_TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);
    public static final String NOTIFICATION_ID = "notification-id";
    public static final String NOTIFICATION = "notification";

    public static final String PAYPAL_URL = "https://paypal.me/DavidYarzebinski/1.99";

    public static final String ARTICLE_LINK = "http://www.diabetes.co.uk/News/rss/newsindex.xml";

    public static final String INSTABUG_KEY = "0d278e5f5680024d7c487884873c0509";
}
