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

package io.github.coffeegerm.materiallogbook.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.coffeegerm.materiallogbook.MaterialLogbook.Companion.syringe
import io.github.coffeegerm.materiallogbook.R
import io.github.coffeegerm.materiallogbook.ui.settings.children.SettingsDataActivity
import io.github.coffeegerm.materiallogbook.ui.settings.children.SettingsTreatmentActivity
import io.github.coffeegerm.materiallogbook.utils.Constants
import io.github.coffeegerm.materiallogbook.utils.Constants.PREF_DARK_MODE
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {
  
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    syringe.inject(this)
    if (sharedPreferences.getBoolean(Constants.PREF_DARK_MODE, false)) setTheme(R.style.AppTheme_Dark)
    setContentView(R.layout.activity_settings)
    initView()
  }
  
  private fun initView() {
    setupToolbar()
    
    toggle_dark_mode.isChecked = sharedPreferences.getBoolean(Constants.PREF_DARK_MODE, false)
    toggle_dark_mode.setOnCheckedChangeListener({ _, isChecked -> sharedPreferences.edit().putBoolean(PREF_DARK_MODE, isChecked).apply() })
    
    military_time_switch.isChecked = sharedPreferences.getBoolean(Constants.MILITARY_TIME, false)
    military_time_switch.setOnCheckedChangeListener { _, isChecked -> sharedPreferences.edit().putBoolean(Constants.MILITARY_TIME, isChecked).apply() }
    
    treatment_section.setOnClickListener({ startActivity(Intent(applicationContext, SettingsTreatmentActivity::class.java)) })
    
    data_section.setOnClickListener { startActivity(Intent(applicationContext, SettingsDataActivity::class.java)) }
  }
  
  private fun setupToolbar() {
    setSupportActionBar(setting_toolbar)
    if (supportActionBar != null) {
      supportActionBar!!.setDisplayHomeAsUpEnabled(true)
      supportActionBar!!.setDisplayShowHomeEnabled(true)
      supportActionBar!!.setTitle(R.string.settings)
    }
  }
  
  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return super.onSupportNavigateUp()
  }
}