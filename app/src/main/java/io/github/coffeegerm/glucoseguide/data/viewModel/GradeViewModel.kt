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

package io.github.coffeegerm.glucoseguide.data.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.github.coffeegerm.glucoseguide.data.DatabaseManager
import io.github.coffeegerm.glucoseguide.utils.DateAssistant

/**
 * TODO: Add class comment header
 */

class GradeViewModel(private var databaseManager: DatabaseManager, var dateAssistant: DateAssistant) : ViewModel() {
  
  private var grade = MutableLiveData<String>()
  private var liveDataLineData = MutableLiveData<LineData>()
  
  fun getGrade(): LiveData<String> {
    grade.value = databaseManager.getGlucoseGrade()
    return grade
  }
  
  fun getPointsForGraph(): LiveData<LineData> {
    val points = mutableListOf<Entry>() // points that are going to be presented on the line chart
    
    val entriesFromLastThreeDays = databaseManager.getAllFromDate(dateAssistant.getThreeDaysAgoDate())
    
    entriesFromLastThreeDays.forEach {
      points.add(Entry(it.date!!.time.toFloat(), it.bloodGlucose.toFloat()))
    }
    
    // data set that is going to be passed to the line chart to view
    // will be last three days of glucose measurements
    val lineDataSet = LineDataSet(points, "entries")
    liveDataLineData.value = LineData(lineDataSet)
    return liveDataLineData
  }
}