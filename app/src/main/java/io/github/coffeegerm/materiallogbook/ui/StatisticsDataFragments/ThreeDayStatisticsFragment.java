package io.github.coffeegerm.materiallogbook.ui.StatisticsDataFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by David Yarzebinski on 7/28/2017.
 * <p>
 * Fragment used with Statistics ViewPager to show
 * the last three days of statistics
 */

public class ThreeDayStatisticsFragment extends Fragment {

    private static final String TAG = "ThreeDaysStatistics";

    @BindView(R.id.three_days_lowest)
    TextView lowestGlucloseTextView;
    @BindView(R.id.three_days_highest)
    TextView highestGlucoseTextView;
    @BindView(R.id.three_days_average)
    TextView averageTextView;
    private String pageTitle;
    private int pageNumber;
    private Realm realm;

    public static ThreeDayStatisticsFragment newInstance(int pageNumber, String pageTitle) {
        ThreeDayStatisticsFragment threeDayStatisticsFragment = new ThreeDayStatisticsFragment();
        Bundle args = new Bundle();
        args.putInt("pageNumber", pageNumber);
        args.putString("pageTitle", pageTitle);
        threeDayStatisticsFragment.setArguments(args);
        return threeDayStatisticsFragment;
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
        View threeDaysStatisticsView = inflater.inflate(R.layout.fragment_three_days_stats, container, false);
        ButterKnife.bind(this, threeDaysStatisticsView);
        realm = Realm.getDefaultInstance();
        Date threeDaysAgo = getThreeDaysAgo();
        RealmResults<EntryItem> entriesFromThreeDaysAgo = realm.where(EntryItem.class).greaterThan("date", threeDaysAgo).findAllSorted("date");
        Log.i(TAG, "onCreateView: " + entriesFromThreeDaysAgo);
        return threeDaysStatisticsView;
    }

    public int getAverageGlucose() {
        return 0;
    }

    public int getHighestGlucose() {
        return 0;
    }

    public int getLowestGlucose() {
        return 0;
    }

    public Date getThreeDaysAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -3);
        return calendar.getTime();
    }
}
