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

package io.github.coffeegerm.glucoseguide.ui.statistics

import android.arch.lifecycle.ViewModel
import io.github.coffeegerm.glucoseguide.data.DatabaseManager
import io.github.coffeegerm.glucoseguide.utils.DateAssistant
import java.util.*

/**
 * ViewModel responsible for logic in all Statistics Fragments
 */

class StatisticsViewModel(var databaseManager: DatabaseManager, var dateAssistant: DateAssistant) : ViewModel() {
  
  fun getNumberOfEntries(): Int = databaseManager.getAllSortedDescending().size
  
  fun getNumberOfEntries(date: Date): Int = databaseManager.getAllFromDate(date).size
  
  fun getAverageBloodGlucose(): String = databaseManager.getAverage().toString()
  
  fun getHighestBloodGlucose(): String = databaseManager.getHighestBloodGlucose().toString()
  
  fun getLowestBloodGlucose(): String = databaseManager.getLowestBloodGlucose().toString()
  
  fun getAverageBloodGlucose(date: Date): String = databaseManager.getAverageGlucoseFromDate(date).toString()
  
  fun getHighestBloodGlucose(date: Date): String = databaseManager.getHighestGlucoseFromDate(date).toString()
  
  fun getLowestBloodGlucose(date: Date): String = databaseManager.getLowestGlucoseFromDate(date).toString()
  
  fun getA1C(): String = ((46.7 + databaseManager.getAverageGlucoseFromDate(dateAssistant.getThreeMonthsAgoDate())) / 28.7).toString()
}