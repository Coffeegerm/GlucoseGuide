package io.github.coffeegerm.materiallogbook.ui.StatisticsDataFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;

import io.github.coffeegerm.materiallogbook.R;

/**
 * Created by David Yarzebinski on 7/28/2017.
 * <p>
 * Fragment used with Statistics ViewPager to show
 * the last seven days of statistics
 */

public class SevenDayStatisticsFragment extends Fragment {

    private static final String TAG = "SevenDaysStatistics";
    private String pageTitle;
    private int pageNumber;

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
        View view = inflater.inflate(R.layout.fragment_seven_days_stats, container, false);

        return view;
    }

    public Date getSevenDaysAgoDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        return calendar.getTime();
    }
}
