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

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.data.DatabaseManager
import io.github.coffeegerm.glucoseguide.data.model.EntryItem
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_all_stats.*
import javax.inject.Inject

/**
 * Fragment used with Statistics ViewPager to show
 * the amount of all statistics
 */

class AllStatisticsFragment : Fragment() {
  
  @Inject
  lateinit var databaseManager: DatabaseManager
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GlucoseGuide.syringe.inject(this)
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_all_stats, container, false)
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val entryItems = databaseManager.getAllSortedDescending()
    if (entryItems.size == 0) {
      all_days_statistics_average.setText(R.string.dash)
      highest_of_all_glucose.setText(R.string.dash)
      lowest_of_all_glucose.setText(R.string.dash)
    } else {
      all_days_statistics_average.text = getAverage(entryItems).toString()
      highest_of_all_glucose.text = getHighestBloodGlucose(entryItems).toString()
      lowest_of_all_glucose.text = getLowestBloodGlucose(entryItems).toString()
    }
  }
  
  fun getAverage(entryItems: RealmResults<EntryItem>): Int {
    var averageCalculated = 0
    if (entryItems.size == 0) {
      Toast.makeText(context, "Unable to show data at this time.", Toast.LENGTH_SHORT).show()
    } else {
      var total = 0
      for (position in entryItems.indices) {
        val item = entryItems[position]!!
        total += item.bloodGlucose
      }
      averageCalculated = total / entryItems.size
    }
    return averageCalculated
  }
  
  fun getHighestBloodGlucose(entryItems: RealmResults<EntryItem>): Int {
    var highest = 0
    for (position in entryItems.indices) {
      val item = entryItems[position]!!
      if (item.bloodGlucose > highest) {
        highest = item.bloodGlucose
      }
    }
    return highest
  }
  
  
  fun getLowestBloodGlucose(entryItems: RealmResults<EntryItem>): Int {
    var lowest = 1000
    for (position in entryItems.indices) {
      val item = entryItems[position]!!
      if (item.bloodGlucose < lowest) {
        lowest = item.bloodGlucose
      }
    }
    return lowest
  }
}
