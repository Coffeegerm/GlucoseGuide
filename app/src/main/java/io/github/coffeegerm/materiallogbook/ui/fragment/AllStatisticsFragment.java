package io.github.coffeegerm.materiallogbook.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.github.coffeegerm.materiallogbook.ui.activity.MainActivity;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by David Yarzebinski on 7/29/2017.
 * <p>
 * Fragment used with Statistics ViewPager to show
 * the all amount of statistics
 */

public class AllStatisticsFragment extends Fragment {
    private static final String TAG = "AllStatisticsFragment";

    @BindView(R.id.all_days_statistics_average)
    TextView averageBloodGlucose;
    @BindView(R.id.highest_of_all_glucose)
    TextView highestBloodGlucose;
    @BindView(R.id.lowest_of_all_glucose)
    TextView lowestBloodGlucose;
    @BindView(R.id.imgAvg)
    ImageView ivAvg;
    @BindView(R.id.imgUpArrow)
    ImageView ivUpArrow;
    @BindView(R.id.imgDownArrow)
    ImageView ivDownArrow;

    Realm realm;
    String pageTitle;
    int pageNumber;

    public static AllStatisticsFragment newInstance(int pageNumber, String pageTitle) {
        AllStatisticsFragment allStatisticsFragment = new AllStatisticsFragment();
        Bundle args = new Bundle();
        args.putInt("pageNumber", pageNumber);
        args.putString("pageTitle", pageTitle);
        allStatisticsFragment.setArguments(args);
        return allStatisticsFragment;
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
        View allStatsView = inflater.inflate(R.layout.fragment_all_stats, container, false);
        ButterKnife.bind(this, allStatsView);
        realm = Realm.getDefaultInstance();
        setValues();
        setImages();
        return allStatsView;
    }

    public void setValues() {
        RealmResults<EntryItem> entryItems = realm.where(EntryItem.class).greaterThan("bloodGlucose", 0).findAll();
        if (entryItems.size() == 0) {
            averageBloodGlucose.setText(R.string.dash);
            highestBloodGlucose.setText(R.string.dash);
            lowestBloodGlucose.setText(R.string.dash);
        } else {
            averageBloodGlucose.setText(String.valueOf(getAverage()));
            highestBloodGlucose.setText(String.valueOf(getHighestBloodGlucose()));
            lowestBloodGlucose.setText(String.valueOf(getLowestBloodGlucose()));
        }
    }

    public int getAverage() {
        int averageCalculated = 0;
        RealmResults<EntryItem> entryItems = realm.where(EntryItem.class).greaterThan("bloodGlucose", 0).findAll();
        if (entryItems.size() == 0) {
            Log.e(TAG, "onCreateView: no entries");
        } else {
            int total = 0;
            for (int position = 0; position < entryItems.size(); position++) {
                EntryItem item = entryItems.get(position);
                total += item.getBloodGlucose();
            }
            averageCalculated = total / entryItems.size();
        }
        return averageCalculated;
    }

    public int getHighestBloodGlucose() {
        int highest = 0;
        RealmResults<EntryItem> entryItems = realm.where(EntryItem.class).greaterThan("bloodGlucose", 0).findAll();
        for (int position = 0; position < entryItems.size(); position++) {
            EntryItem item = entryItems.get(position);
            if (item.getBloodGlucose() > highest) {
                highest = item.getBloodGlucose();
            }
        }
        return highest;
    }


    public int getLowestBloodGlucose() {
        int lowest = 1000;
        RealmResults<EntryItem> entryItems = realm.where(EntryItem.class).greaterThan("bloodGlucose", 0).findAll();
        for (int position = 0; position < entryItems.size(); position++) {
            EntryItem item = entryItems.get(position);
            if (item.getBloodGlucose() < lowest) {
                lowest = item.getBloodGlucose();
            }
        }
        return lowest;
    }

    private void setImages() {
        if (MainActivity.sharedPreferences.getBoolean("pref_dark_mode", false)) {
            ivAvg.setImageResource(R.drawable.ic_average_dark);
            ivUpArrow.setImageResource(R.drawable.ic_up_arrow_dark);
            ivDownArrow.setImageResource(R.drawable.ic_down_arrow_dark);
        }
    }
}
