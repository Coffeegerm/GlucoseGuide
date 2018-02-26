/*
 * Copyright 2017 Coffee and Cream Studios
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

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.utils.Constants.*
import kotlinx.android.synthetic.main.activity_settings_treatment.*
import javax.inject.Inject

/**
 * Created by dyarz on 10/6/2017.
 *
 *
 * Sub menu of SettingsActivity for Treatment section
 */

class TreatmentActivity : AppCompatActivity() {
  
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GlucoseGuide.syringe.inject(this)
    if (sharedPreferences.getBoolean(PREF_DARK_MODE, false)) setTheme(R.style.AppTheme_Dark)
    setContentView(R.layout.activity_settings_treatment)
    init()
  }
  
  private fun init() {
    setupToolbar()
    checkRangeStatus()
    setHints()
    hypoglycemic_edit_text.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
      
      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
      
      override fun afterTextChanged(s: Editable) {
        if (s.toString() != "")
          sharedPreferences.edit()
                .putInt(HYPOGLYCEMIC_INDEX, Integer.parseInt(s.toString())).apply()
      }
    })
    
    hyperglycemic_edit_text.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
      
      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
      
      override fun afterTextChanged(s: Editable) {
        if (s.toString() != "")
          sharedPreferences.edit()
                .putInt(HYPERGLYCEMIC_INDEX, Integer.parseInt(s.toString())).apply()
      }
    })
    
    bolus_ratio.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
      
      }
      
      override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
      
      }
      
      override fun afterTextChanged(editable: Editable) {
        if (editable.toString() != "")
          sharedPreferences.edit()
                .putInt(BOLUS_RATIO, Integer.parseInt(editable.toString())).apply()
      }
    })
  }
  
  private fun checkRangeStatus() {
    val hyperglycemicIndex = sharedPreferences.getInt(HYPERGLYCEMIC_INDEX, 0)
    val hypoglycemicIndex = sharedPreferences.getInt(HYPOGLYCEMIC_INDEX, 0)
    val bolusRatio = sharedPreferences.getInt(BOLUS_RATIO, 0)
    if (hyperglycemicIndex == 0 && hypoglycemicIndex == 0) {
      sharedPreferences.edit().putInt(HYPOGLYCEMIC_INDEX, 80).apply()
      sharedPreferences.edit().putInt(HYPERGLYCEMIC_INDEX, 140).apply()
    }
    if (bolusRatio == 0) {
      sharedPreferences.edit().putInt(BOLUS_RATIO, 10).apply()
    }
  }
  
  private fun setHints() {
    hyperglycemic_edit_text.hint = sharedPreferences.getInt(HYPERGLYCEMIC_INDEX, 0).toString()
    hypoglycemic_edit_text.hint = sharedPreferences.getInt(HYPOGLYCEMIC_INDEX, 0).toString()
    bolus_ratio.hint = sharedPreferences.getInt(BOLUS_RATIO, 0).toString()
  }
  
  private fun setupToolbar() {
    setSupportActionBar(treatment_toolbar)
    if (supportActionBar != null) {
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
      supportActionBar?.setDisplayShowHomeEnabled(true)
      supportActionBar?.setTitle(R.string.treatment)
    }
  }
  
  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return super.onSupportNavigateUp()
  }
}
