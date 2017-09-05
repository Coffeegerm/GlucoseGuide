package io.github.coffeegerm.materiallogbook.statistics;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by David Yarzebinski on 7/28/2017.
 * <p>
 * Adapter used for ViewPager within StatisticsFragment
 */

public class StatisticsPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 5;
    private String tabTitles[] = new String[]{"3 Days", "7 Days", "One Month", "Three Months", "All"};

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
            case 2:
                return OneMonthStatisticsFragment.newInstance(2, "One Month");
            case 3:
                return ThreeMonthsStatisticsFragment.newInstance(3, "Three Months");
            case 4: // all stats returned
                return AllStatisticsFragment.newInstance(4, "All");
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
