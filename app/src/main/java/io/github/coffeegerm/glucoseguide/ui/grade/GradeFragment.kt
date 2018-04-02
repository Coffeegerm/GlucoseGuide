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

package io.github.coffeegerm.glucoseguide.ui.grade

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.data.DatabaseManager
import io.github.coffeegerm.glucoseguide.data.viewModel.GradeViewModel
import io.github.coffeegerm.glucoseguide.data.viewModel.GradeViewModelFactory
import io.github.coffeegerm.glucoseguide.utils.DateAssistant
import kotlinx.android.synthetic.main.fragment_grade.*
import javax.inject.Inject

class GradeFragment : Fragment() {
  
  @Inject
  lateinit var gradeViewModelFactory: GradeViewModelFactory
  @Inject
  lateinit var databaseManager: DatabaseManager
  @Inject
  lateinit var dateAssistant: DateAssistant
  
  private lateinit var gradeViewModel: GradeViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GlucoseGuide.syringe.inject(this)
    gradeViewModel = ViewModelProviders.of(this, gradeViewModelFactory).get(GradeViewModel::class.java)
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_grade, container, false)
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    gradeViewModel.getGrade().observe(this, Observer<String> { grade -> overall_grade.text = grade })
    
    val numberOfEntries = databaseManager.getAllFromDate(dateAssistant.getThreeDaysAgoDate()).size
    
    lineChartStyling()
    if (numberOfEntries != 0) {
      lineChart.setVisibleXRangeMaximum(numberOfEntries.toFloat())
      lineChart.data = getGraphData()
    }
    
    lineChart.invalidate()
  }
  
  private fun getGraphData(): LineData {
    val points = mutableListOf<Entry>() // points that are going to be presented on the line chart
    
    val entriesFromLastThreeDays = databaseManager.getAllFromDate(dateAssistant.getThreeDaysAgoDate())
    
    entriesFromLastThreeDays.forEach {
      points.add(Entry(it.date!!.time.toFloat(), it.bloodGlucose.toFloat()))
    }
    
    // data set that is going to be passed to the line chart to view
    // will be last three days of glucose measurements
    val lineDataSet = LineDataSet(points, "entries")
    
    return LineData(lineDataSet)
  }
  
  private fun lineChartStyling() {
    lineChart.setDrawGridBackground(false)
    lineChart.setPinchZoom(false)
    lineChart.axisLeft.isEnabled = false
    lineChart.axisRight.isEnabled = false
    lineChart.xAxis.isEnabled = false
    lineChart.isDoubleTapToZoomEnabled = false
    lineChart.legend.isEnabled = false
    lineChart.description.isEnabled = false
  }
}