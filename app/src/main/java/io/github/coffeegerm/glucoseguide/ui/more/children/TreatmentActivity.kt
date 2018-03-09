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

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.utils.Constants.BOLUS_RATIO
import io.github.coffeegerm.glucoseguide.utils.Constants.HYPERGLYCEMIC_INDEX
import io.github.coffeegerm.glucoseguide.utils.Constants.HYPOGLYCEMIC_INDEX
import io.github.coffeegerm.glucoseguide.utils.Constants.PREF_DARK_MODE
import io.github.coffeegerm.glucoseguide.utils.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_treatment.*
import javax.inject.Inject

class TreatmentActivity : AppCompatActivity() {
  
  @Inject
  lateinit var sharedPreferenceManager: SharedPreferenceManager
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GlucoseGuide.syringe.inject(this)
    if (sharedPreferenceManager.getBoolean(PREF_DARK_MODE)) setTheme(R.style.AppTheme_Dark)
    setContentView(R.layout.activity_treatment)
    setSupportActionBar(treatment_toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.setTitle(R.string.treatment)
    init()
  }
  
  private fun init() {
    checkRangeStatus()
    setHints()
    hypoglycemic_edit_text.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
      
      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
      
      override fun afterTextChanged(s: Editable) {
        if (s.toString().isNotBlank())
          sharedPreferenceManager
                .putInt(HYPOGLYCEMIC_INDEX, Integer.parseInt(s.toString()))
      }
    })
    
    hyperglycemic_edit_text.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
      
      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
      
      override fun afterTextChanged(s: Editable) {
        if (s.toString().isNotBlank())
          sharedPreferenceManager
                .putInt(HYPERGLYCEMIC_INDEX, Integer.parseInt(s.toString()))
      }
    })
    
    bolus_ratio.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
      
      }
      
      override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
      
      }
      
      override fun afterTextChanged(editable: Editable) {
        if (editable.toString().isNotBlank())
          sharedPreferenceManager
                .putInt(BOLUS_RATIO, Integer.parseInt(editable.toString()))
      }
    })
  }
  
  private fun checkRangeStatus() {
    if (sharedPreferenceManager.getInt(HYPOGLYCEMIC_INDEX) == 0 && sharedPreferenceManager.getInt(HYPOGLYCEMIC_INDEX) == 0) {
      sharedPreferenceManager.putInt(HYPOGLYCEMIC_INDEX, 80)
      sharedPreferenceManager.putInt(HYPERGLYCEMIC_INDEX, 140)
    }
    if (sharedPreferenceManager.getInt(BOLUS_RATIO) == 0) {
      sharedPreferenceManager.putInt(BOLUS_RATIO, 10)
    }
  }
  
  private fun setHints() {
    hyperglycemic_edit_text.hint = sharedPreferenceManager.getInt(HYPERGLYCEMIC_INDEX).toString()
    hypoglycemic_edit_text.hint = sharedPreferenceManager.getInt(HYPOGLYCEMIC_INDEX).toString()
    bolus_ratio.hint = sharedPreferenceManager.getInt(BOLUS_RATIO).toString()
  }
  
  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return super.onSupportNavigateUp()
  }
}
