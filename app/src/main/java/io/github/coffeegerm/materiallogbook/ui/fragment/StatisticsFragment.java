package io.github.coffeegerm.materiallogbook.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.adapter.StatisticsPagerAdapter;

/**
 * Created by David Yarzebinski on 7/28/2017.
 * <p>
 * Fragment to show the data over the past through a ViewPager
 */

public class StatisticsFragment extends Fragment {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View statsView = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind(this, statsView);
        initViewPager();
        return statsView;
    }

    private void initViewPager() {
        StatisticsPagerAdapter statisticsPagerAdapter = new StatisticsPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(statisticsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
