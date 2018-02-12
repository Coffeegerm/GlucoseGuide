/*
 * Copyright 2018 Coffee and Cream Studios
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

package io.github.coffeegerm.materiallogbook.ui.statistics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.coffeegerm.materiallogbook.R
import kotlinx.android.synthetic.main.fragment_statistics.*

/**
 * Fragment responsible for showing the pager adapter that contains the statistics of the users
 * data from the last few days, months, or all time.
 */

class StatisticsFragment : Fragment() {
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_statistics, container, false)
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val pagerAdapter = StatisticsPagerAdapter(childFragmentManager, resources)
    viewPager.adapter = pagerAdapter
    tabLayout.setupWithViewPager(viewPager)
  }
  
}