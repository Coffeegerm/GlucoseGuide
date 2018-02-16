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

package io.github.coffeegerm.glucoseguide.ui.statistics

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.ui.statistics.children.*

/**
 * Created by David Yarzebinski on 7/28/2017
 *
 * Adapter used for ViewPager within StatisticsFragment
 */

class StatisticsPagerAdapter internal constructor(fragmentManager: FragmentManager, resources: Resources) : FragmentPagerAdapter(fragmentManager) {
  
  private val tabTitles = resources.getStringArray(R.array.statistic_tab_titles)
  
  override fun getItem(position: Int): Fragment? {
    return when (position) {
      0 -> ThreeDayStatisticsFragment()
      1 -> SevenDayStatisticsFragment()
      2 -> OneMonthStatisticsFragment()
      3 -> ThreeMonthsStatisticsFragment()
      4 -> AllStatisticsFragment()
      else -> null
    }
  }
  
  override fun getCount(): Int = 5
  
  override fun getPageTitle(position: Int): CharSequence? = tabTitles[position]
  
}
