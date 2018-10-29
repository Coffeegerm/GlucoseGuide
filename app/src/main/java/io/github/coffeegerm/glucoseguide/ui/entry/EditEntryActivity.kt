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

package io.github.coffeegerm.glucoseguide.ui.entry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.data.DatabaseManager
import io.github.coffeegerm.glucoseguide.data.model.Entry
import io.github.coffeegerm.glucoseguide.ui.MainActivity
import io.github.coffeegerm.glucoseguide.utils.Constants
import io.github.coffeegerm.glucoseguide.utils.DateFormatter
import io.github.coffeegerm.glucoseguide.utils.SharedPreferencesManager
import kotlinx.android.synthetic.main.activity_edit_entry.*
import java.util.*
import javax.inject.Inject


class EditEntryActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
  
  companion object {
    var wasDateChanged = false
  }
  
  @Inject
  lateinit var sharedPreferencesManager: SharedPreferencesManager
  @Inject
  lateinit var databaseManager: DatabaseManager
  @Inject
  lateinit var dateFormatter: DateFormatter
  
  // Original values from old. Compare to possible updated values to find what needs to be updated in database
  private lateinit var originalDate: Date
  private var originalStatus: Int = 0
  private var originalBloodGlucose: Int = 0
  private var originalCarbohydrates: Int = 0
  private var originalInsulin: Double = 0.0
  
  // items to be used to altered to show that the old has been updated
  private var updatedStatus: Int = 0
  internal var updatedBloodGlucose: Int = 0
  internal var updatedCarbohydrates: Int = 0
  internal var updatedInsulin: Double = 0.0
  private var originalCalendar: Calendar = Calendar.getInstance()
  private var updatedCalendar: Calendar = Calendar.getInstance()
  
  private lateinit var itemId: String
  private lateinit var old: Entry
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GlucoseGuide.syringe.inject(this)
    if (sharedPreferencesManager.getBoolean(Constants.PREF_DARK_MODE))
      setTheme(R.style.AppTheme_Dark)
    setContentView(R.layout.activity_edit_entry)
    setSupportActionBar(edit_entry_toolbar)
    supportActionBar?.setTitle(R.string.edit_entry_toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    
    itemId = intent.getStringExtra(Constants.ITEM_ID)
    old = databaseManager.getEntryFromId(itemId)!!
    
    getOriginalValues()
    
    val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.status_selection))
    edit_status_selector.adapter = spinnerAdapter
    edit_status_selector.onItemSelectedListener = this
    
    setHints()
    
    edit_entry_date.setOnClickListener {
      val dialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
              var correctMonth = month
              correctMonth++
              edit_entry_date.setText(dateFormatter.formatDateForEditText(correctMonth, dayOfMonth, year))
              correctMonth--
              if (!wasDateChanged) wasDateChanged = true
              updatedCalendar.set(year, correctMonth, dayOfMonth)
            }, originalCalendar.get(Calendar.YEAR),
            originalCalendar.get(Calendar.MONTH),
            originalCalendar.get(Calendar.DAY_OF_MONTH))
      
      dialog.show()
    }
    
    edit_entry_time.setOnClickListener {
      val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
              edit_entry_time.setText(dateFormatter.formatTimeForEditText(hourOfDay, minute))
              updatedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
              updatedCalendar.set(Calendar.MINUTE, minute)
              if (!wasDateChanged) wasDateChanged = true
            },
            originalCalendar.get(Calendar.HOUR_OF_DAY),
            originalCalendar.get(Calendar.MINUTE),
            false)
      timePickerDialog.show()
    }
    
    edit_entry_blood_glucose_level.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
      
      }
      
      override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
      
      }
      
      override fun afterTextChanged(editable: Editable) {
        updatedBloodGlucose = Integer.parseInt(editable.toString())
      }
    })
    
    edit_entry_carbohydrates_amount.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
      
      }
      
      override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
      
      }
      
      override fun afterTextChanged(editable: Editable) {
        updatedCarbohydrates = Integer.parseInt(editable.toString())
      }
    })
    
    edit_entry_insulin_units.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
      
      }
      
      override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
      
      }
      
      override fun afterTextChanged(editable: Editable) {
        updatedInsulin = java.lang.Double.parseDouble(editable.toString())
      }
    })
    
    
    
    delete_entry.setOnClickListener {
      val builder: AlertDialog.Builder = AlertDialog.Builder(this)
      builder.setTitle(R.string.delete_single_entry)
            .setMessage(R.string.delete_single_entry_message)
            .setPositiveButton(android.R.string.yes) { _, _ ->
              databaseManager.deleteEntry(old)
              Toast.makeText(this, R.string.entry_deleted, Toast.LENGTH_SHORT).show()
              startActivity(Intent(applicationContext, MainActivity::class.java))
            }
            .setNegativeButton(android.R.string.no) { dialog, _ ->
              dialog.dismiss()
            }
            .setIcon(R.drawable.ic_trash)
            .show()
    }
    update_fab.setOnClickListener { updateEntry() }
  }
  
  private fun getOriginalValues() {
    originalStatus = old.status
    originalCalendar.time = old.date
    originalDate = originalCalendar.time
    originalBloodGlucose = old.bloodGlucose
    originalCarbohydrates = old.carbohydrates
    originalInsulin = old.insulin
  }
  
  private fun setHints() {
    edit_status_selector.setSelection(originalStatus)
    edit_entry_date.hint = dateFormatter.formatDate(originalDate)
    edit_entry_time.hint = dateFormatter.twelveHourFormat(originalDate)
    edit_entry_blood_glucose_level.hint = originalBloodGlucose.toString()
    if (originalCarbohydrates != 0)
      edit_entry_carbohydrates_amount.hint = originalCarbohydrates.toString()
    else
      edit_entry_carbohydrates_amount.setHint(R.string.dash)
    if (originalInsulin != 0.0)
      edit_entry_insulin_units.hint = originalInsulin.toString()
    else
      edit_entry_insulin_units.setHint(R.string.dash)
  }
  
  private fun updateEntry() {
    val dateToSave = Date()
    val itemToSave = Entry()
    itemToSave.status = updatedStatus
    when (wasDateChanged) {
      true -> {
        dateToSave.time = updatedCalendar.timeInMillis
        itemToSave.date = dateToSave
      }
      false -> {
        dateToSave.time = originalCalendar.timeInMillis
        itemToSave.date = dateToSave
      }
    }
    if (edit_entry_blood_glucose_level.text.isNotEmpty()) itemToSave.bloodGlucose = edit_entry_blood_glucose_level.text.toString().toInt() else itemToSave.bloodGlucose = originalBloodGlucose
    if (edit_entry_carbohydrates_amount.text.isNotEmpty()) itemToSave.carbohydrates = edit_entry_carbohydrates_amount.text.toString().toInt() else itemToSave.carbohydrates = originalCarbohydrates
    if (edit_entry_insulin_units.text.isNotEmpty()) itemToSave.insulin = edit_entry_insulin_units.text.toString().toDouble() else itemToSave.insulin = originalInsulin
    databaseManager.deleteEntry(old)
    databaseManager.copyToRealm(itemToSave)
    finish()
  }
  
  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return super.onSupportNavigateUp()
  }
  
  override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
    edit_status_selector.setSelection(position)
    updatedStatus = position
  }
  
  override fun onNothingSelected(p0: AdapterView<*>?) {}
}