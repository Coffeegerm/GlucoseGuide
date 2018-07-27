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

package io.github.coffeegerm.glucoseguide.ui.statistics.children

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.data.viewModel.StatisticsViewModel
import io.github.coffeegerm.glucoseguide.data.viewModel.StatisticsViewModelFactory
import io.github.coffeegerm.glucoseguide.utils.DateAssistant
import kotlinx.android.synthetic.main.fragment_one_month_statistics.*
import java.util.*
import javax.inject.Inject

class OneMonthStatisticsFragment : Fragment() {

  @Inject
  lateinit var dateAssistant: DateAssistant
  @Inject
  lateinit var statisticsViewModelFactory: StatisticsViewModelFactory
  private lateinit var statisticsViewModel: StatisticsViewModel

  private lateinit var oneMonthAgo: Date

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GlucoseGuide.syringe.inject(this)
    statisticsViewModel = ViewModelProviders.of(this, statisticsViewModelFactory).get(StatisticsViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_one_month_statistics, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    oneMonthAgo = dateAssistant.getOneMonthAgo()
  }

  override fun onResume() {
    super.onResume()
    getStatistics()
  }

  private fun getStatistics() {
    if (statisticsViewModel.getNumberOfEntries(oneMonthAgo) != 0) {
      one_month_average.text = statisticsViewModel.getAverageBloodGlucose(oneMonthAgo)
      one_month_highest.text = statisticsViewModel.getHighestBloodGlucose(oneMonthAgo)
      one_month_lowest.text = statisticsViewModel.getLowestBloodGlucose(oneMonthAgo)
    }
  }


}
