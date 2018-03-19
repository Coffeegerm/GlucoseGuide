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
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.R
import kotlinx.android.synthetic.main.fragment_grade.*
import javax.inject.Inject

/**
 * TODO: Add class comment header
 */

class GradeFragment : Fragment() {
  
  @Inject
  lateinit var gradeViewModelFactory: GradeViewModelFactory
  
  private lateinit var gradeViewModel: GradeViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GlucoseGuide.syringe.inject(this)
    gradeViewModel = ViewModelProviders.of(this, gradeViewModelFactory).get(GradeViewModel::class.java)
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_grade, container, false)
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    gradeViewModel.grade.observe(this, Observer<String> { grade -> overall_grade.text = grade })
    
    val points = mutableListOf<Entry>()
    val numberOfItemsInDatabase = mutableListOf<Int>() // should be the size of the database for reference
    
    val lineDataSet = LineDataSet(points, "entries")
    
    val textColor = context?.let { ContextCompat.getColor(it, R.color.colorAccent) }
    
    textColor?.let { handleXAxis(it) }
    textColor?.let { handleLeftAxis(it) }
    handleRightAxis()
    
    lineChart.setDrawGridBackground(false)
    lineChart.setPinchZoom(false)
    lineChart.isDoubleTapToZoomEnabled = false
    lineChart.legend.isEnabled = false
    val description = Description()
    description.text = ""
    lineChart.description = description
    
    lineChart.invalidate()
  }
  
  private fun handleXAxis(textColor: Int) {
    lineChart.xAxis.setLabelCount(4, true)
    lineChart.xAxis.setDrawAxisLine(false)
    lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    lineChart.xAxis.setDrawGridLines(false)
    lineChart.xAxis.textColor = textColor
  }
  
  private fun handleLeftAxis(textColor: Int) {
    lineChart.axisLeft.setLabelCount(3, true)
    lineChart.axisLeft.textColor = textColor
    lineChart.axisLeft.setDrawAxisLine(false)
    lineChart.axisLeft.yOffset = -8f
  }
  
  private fun handleRightAxis() {
    lineChart.axisRight.setDrawLabels(false)
    lineChart.axisRight.setDrawGridLines(false)
    lineChart.axisRight.setDrawAxisLine(false)
  }
}