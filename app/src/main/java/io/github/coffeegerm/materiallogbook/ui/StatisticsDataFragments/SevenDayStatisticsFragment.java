package io.github.coffeegerm.materiallogbook.ui.StatisticsDataFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.github.coffeegerm.materiallogbook.ui.MainActivity;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by David Yarzebinski on 7/28/2017.
 * <p>
 * Fragment used with Statistics ViewPager to show
 * the last seven days of statistics
 */

public class SevenDayStatisticsFragment extends Fragment {
    private static final String TAG = "SevenDaysStatistics";

    @BindView(R.id.seven_days_average)
    TextView averageBloodGlucose;
    @BindView(R.id.seven_days_highest)
    TextView highestBloodGlucose;
    @BindView(R.id.seven_days_lowest)
    TextView lowestBloodGlucose;
    @BindView(R.id.seven_days_average_label)
    TextView averageLabel;
    @BindView(R.id.seven_days_highest_label)
    TextView highestLabel;
    @BindView(R.id.seven_days_lowest_label)
    TextView lowestLabel;

    @BindView(R.id.imgAvg)
    ImageView ivAvg;
    @BindView(R.id.imgUpArrow)
    ImageView ivUpArrow;
    @BindView(R.id.imgDownArrow)
    ImageView ivDownArrow;

    Realm realm;
    String pageTitle;
    int pageNumber;

    public static SevenDayStatisticsFragment newInstance(int pageNumber, String pageTitle) {
        SevenDayStatisticsFragment sevenDayStatisticsFragment = new SevenDayStatisticsFragment();
        Bundle args = new Bundle();
        args.putInt("pageNumber", pageNumber);
        args.putString("pageTitle", pageTitle);
        sevenDayStatisticsFragment.setArguments(args);
        return sevenDayStatisticsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageTitle = getArguments().getString("pageTitle");
        pageNumber = getArguments().getInt("pageNumber");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View sevenDaysView = inflater.inflate(R.layout.fragment_seven_days_stats, container, false);
        ButterKnife.bind(this, sevenDaysView);
        realm = Realm.getDefaultInstance();
        setValues();
        setImages();
        return sevenDaysView;
    }

    private void setValues() {
        Date sevenDaysAgo = getSevenDaysAgo();
        RealmResults<EntryItem> entriesFromLastWeek = realm.where(EntryItem.class).greaterThan("date", sevenDaysAgo).greaterThan("bloodGlucose", 0).findAll();
        if (entriesFromLastWeek.size() == 0) {
            averageBloodGlucose.setText(R.string.dash);
            highestBloodGlucose.setText(R.string.dash);
            lowestBloodGlucose.setText(R.string.dash);
        } else {
            averageBloodGlucose.setText(String.valueOf(getAverageGlucose(sevenDaysAgo)));
            highestBloodGlucose.setText(String.valueOf(getHighestGlucose(sevenDaysAgo)));
            lowestBloodGlucose.setText(String.valueOf(getLowestGlucose(sevenDaysAgo)));
        }
    }

    public int getAverageGlucose(Date sevenDaysAgo) {
        int total = 0;
        RealmResults<EntryItem> entriesFromLastWeek = realm.where(EntryItem.class).greaterThan("date", sevenDaysAgo).greaterThan("bloodGlucose", 0).findAll();
        for (int position = 0; position < entriesFromLastWeek.size(); position++) {
            EntryItem currentItem = entriesFromLastWeek.get(position);
            total += currentItem.getBloodGlucose();
        }
        return total / entriesFromLastWeek.size();
    }

    public int getHighestGlucose(Date sevenDaysAgo) {
        int highest = 0;
        RealmResults<EntryItem> entriesFromPastWeek = realm.where(EntryItem.class).greaterThan("date", sevenDaysAgo).greaterThan("bloodGlucose", 0).findAll();
        for (int position = 0; position < entriesFromPastWeek.size(); position++) {
            EntryItem currentItem = entriesFromPastWeek.get(position);
            if (currentItem.getBloodGlucose() > highest) {
                highest = currentItem.getBloodGlucose();
            }
        }
        return highest;
    }

    public int getLowestGlucose(Date sevenDaysAgo) {
        int lowest = 1000;
        RealmResults<EntryItem> entriesFromLastWeek = realm.where(EntryItem.class).greaterThan("date", sevenDaysAgo).greaterThan("bloodGlucose", 0).findAll();
        for (int position = 0; position < entriesFromLastWeek.size(); position++) {
            EntryItem currentItem = entriesFromLastWeek.get(position);
            if (currentItem.getBloodGlucose() < lowest) {
                lowest = currentItem.getBloodGlucose();
            }
        }
        return lowest;
    }

    public Date getSevenDaysAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        return calendar.getTime();
    }

    private void setImages() {
        if (MainActivity.sharedPreferences.getBoolean("pref_dark_mode", false)) {
            ivAvg.setImageResource(R.drawable.ic_average_dark);
            ivUpArrow.setImageResource(R.drawable.ic_up_arrow_dark);
            ivDownArrow.setImageResource(R.drawable.ic_down_arrow_dark);
        } else {
            ivAvg.setImageResource(R.drawable.ic_average);
            ivUpArrow.setImageResource(R.drawable.ic_up_arrow);
            ivDownArrow.setImageResource(R.drawable.ic_down_arrow);
        }
    }
}
