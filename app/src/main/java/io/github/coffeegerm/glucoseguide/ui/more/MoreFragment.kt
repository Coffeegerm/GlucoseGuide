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

package io.github.coffeegerm.glucoseguide.ui.more

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.content.edit
import io.github.coffeegerm.glucoseguide.GlucoseGuide.Companion.syringe
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.ui.more.children.DataActivity
import io.github.coffeegerm.glucoseguide.ui.more.children.TreatmentActivity
import io.github.coffeegerm.glucoseguide.utils.Constants
import io.github.coffeegerm.glucoseguide.utils.Constants.PREF_DARK_MODE
import kotlinx.android.synthetic.main.fragment_more.*
import javax.inject.Inject

class MoreFragment : Fragment() {
  
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_more, container, false)
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    syringe.inject(this)
    toggle_dark_mode.isChecked = sharedPreferences.getBoolean(Constants.PREF_DARK_MODE, false)
    toggle_dark_mode.setOnCheckedChangeListener({ _, isChecked -> changeTheme(isChecked) })
    
    military_time_switch.isChecked = sharedPreferences.getBoolean(Constants.MILITARY_TIME, false)
    military_time_switch.setOnCheckedChangeListener { _, isChecked -> sharedPreferences.edit().putBoolean(Constants.MILITARY_TIME, isChecked).apply() }
    
    treatment_section.setOnClickListener({ startActivity(Intent(context, TreatmentActivity::class.java)) })
    
    data_section.setOnClickListener { startActivity(Intent(context, DataActivity::class.java)) }
  }
  
  fun changeTheme(isChecked: Boolean) {
    sharedPreferences.edit {
      putBoolean(PREF_DARK_MODE, isChecked).apply()
    }
  }
}