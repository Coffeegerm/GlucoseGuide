package io.github.coffeegerm.materiallogbook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.coffeegerm.materiallogbook.ui.fragment.AllStatisticsFragment;
import io.github.coffeegerm.materiallogbook.ui.fragment.SevenDayStatisticsFragment;
import io.github.coffeegerm.materiallogbook.ui.fragment.ThreeDayStatisticsFragment;

/**
 * Created by David Yarzebinski on 7/28/2017.
 * <p>
 * Adapter used for ViewPager within StatisticsFragment
 */

public class StatisticsPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;
    private String tabTitles[] = new String[]{"3 Days", "7 Days", "All"};

    public StatisticsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: //  three days stats returned
                return ThreeDayStatisticsFragment.newInstance(0, "Three Days");
            case 1: // 7 days stats returned
                return SevenDayStatisticsFragment.newInstance(1, "Seven Days");
            case 2: // all stats returned
                return AllStatisticsFragment.newInstance(2, "All");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
