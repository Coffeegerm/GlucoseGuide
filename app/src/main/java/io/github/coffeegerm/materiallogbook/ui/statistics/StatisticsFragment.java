/*
 * Copyright 2017 Coffee and Cream Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.coffeegerm.materiallogbook.ui.statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class StatisticsFragment extends Fragment {
  
  @BindView(R.id.tabLayout)
  TabLayout tabLayout;
  @BindView(R.id.viewPager)
  ViewPager viewPager;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View statsView = inflater.inflate(R.layout.fragment_statistics, container, false);
    ButterKnife.bind(this, statsView);
    StatisticsPagerAdapter statisticsPagerAdapter = new StatisticsPagerAdapter(getChildFragmentManager());
    viewPager.setAdapter(statisticsPagerAdapter);
    tabLayout.setupWithViewPager(viewPager);
    return statsView;
  }
}
