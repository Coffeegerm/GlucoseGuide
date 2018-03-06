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

package io.github.coffeegerm.glucoseguide.ui.more.children

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.content.edit
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.ui.MainActivity
import io.github.coffeegerm.glucoseguide.utils.Constants
import kotlinx.android.synthetic.main.activity_ui.*
import javax.inject.Inject

/**
 * TODO: Add class comment header
 */

class UiActivity : AppCompatActivity() {
  
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GlucoseGuide.syringe.inject(this)
    if (sharedPreferences.getBoolean(Constants.PREF_DARK_MODE, false)) setTheme(R.style.AppTheme_Dark)
    setContentView(R.layout.activity_ui)
    setSupportActionBar(ui_toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.title = getString(R.string.ui)
    
    toggle_dark_mode.isChecked = sharedPreferences.getBoolean(Constants.PREF_DARK_MODE, false)
    toggle_dark_mode.setOnCheckedChangeListener({ _, isChecked -> changeTheme(isChecked) })
    
    military_time_switch.isChecked = sharedPreferences.getBoolean(Constants.MILITARY_TIME, false)
    military_time_switch.setOnCheckedChangeListener { _, isChecked -> sharedPreferences.edit().putBoolean(Constants.MILITARY_TIME, isChecked).apply() }
  }
  
  private fun changeTheme(isChecked: Boolean) {
    sharedPreferences.edit { putBoolean(Constants.PREF_DARK_MODE, isChecked).apply() }
    finish()
    startActivity(Intent(this, UiActivity::class.java))
  }
  
  override fun onSupportNavigateUp(): Boolean {
    finish()
    startActivity(Intent(this, MainActivity::class.java))
    return super.onSupportNavigateUp()
  }
}