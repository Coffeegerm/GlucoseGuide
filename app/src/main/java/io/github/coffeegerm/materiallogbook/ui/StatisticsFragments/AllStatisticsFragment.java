package io.github.coffeegerm.materiallogbook.ui.StatisticsFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.coffeegerm.materiallogbook.R;

/**
 * Created by David Yarzebinski on 7/29/2017.
 * <p>
 * Fragment used with Statistics ViewPager to show
 * the all amount of statistics
 */

public class AllStatisticsFragment extends Fragment {
    private String pageTitle;
    private int pageNumber;

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
        View view = inflater.inflate(R.layout.fragment_all_stats, container, false);

        return view;
    }
}
