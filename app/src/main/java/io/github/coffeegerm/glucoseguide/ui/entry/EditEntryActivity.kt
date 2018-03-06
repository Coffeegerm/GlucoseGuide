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
import android.content.SharedPreferences
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
import io.github.coffeegerm.glucoseguide.data.model.EntryItem
import io.github.coffeegerm.glucoseguide.utils.Constants
import io.github.coffeegerm.glucoseguide.utils.Constants.*
import io.github.coffeegerm.glucoseguide.utils.Utilities
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_edit_entry.*
import java.util.*
import javax.inject.Inject

/**
 * TODO create class header
 */

class EditEntryActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
  
  @Inject
  lateinit var utilities: Utilities
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  @Inject
  lateinit var databaseManager: DatabaseManager
  
  /* Original values from oldItem. Compare to possible updated values to find what needs to be updated in database */
  private lateinit var originalDate: Date
  private var originalStatus: Int = 0
  private var originalBloodGlucose: Int = 0
  private var originalCarbohydrates: Int = 0
  private var originalInsulin: Double = 0.toDouble()
  
  /* items to be used to altered to show that the oldItem has been updated */
  private var updatedStatus: Int = 0
  internal var updatedBloodGlucose: Int = 0
  internal var updatedCarbohydrates: Int = 0
  internal var updatedInsulin: Double = 0.toDouble()
  private var originalCalendar: Calendar = Calendar.getInstance()
  private var updatedCalendar: Calendar = Calendar.getInstance()
  
  private var realm: Realm = Realm.getDefaultInstance()
  private lateinit var itemId: String
  private lateinit var oldItem: EntryItem
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GlucoseGuide.syringe.inject(this)
    if (sharedPreferences.getBoolean(PREF_DARK_MODE, false))
      setTheme(R.style.AppTheme_Dark)
    setContentView(R.layout.activity_edit_entry)
    setSupportActionBar(edit_entry_toolbar)
    supportActionBar?.setTitle(R.string.edit_entry_toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    
    itemId = intent.getStringExtra(Constants.ITEM_ID)
    oldItem = databaseManager.getEntryFromId(itemId)!!
    
    getOriginalValues() // must call before hints are set
  
    val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.status_selection))
    edit_status_selector.adapter = spinnerAdapter
    edit_status_selector.onItemSelectedListener = this
    
    setHints()
    
    edit_entry_date.setOnClickListener {
      val dialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
              var correctMonth = month
              correctMonth++
              edit_entry_date.setText(utilities.formatDate(correctMonth, dayOfMonth, year))
              correctMonth--
              updatedCalendar.set(year, correctMonth, dayOfMonth)
            }, originalCalendar.get(Calendar.YEAR), // year
            originalCalendar.get(Calendar.MONTH), // month
            originalCalendar.get(Calendar.DAY_OF_MONTH)) // day
      
      if (Build.VERSION.SDK_INT < 21)
        if (dialog.window != null)
          dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
      
      dialog.show()
    }
    
    edit_entry_time.setOnClickListener {
      val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
              edit_entry_time.setText(utilities.checkTimeString(hourOfDay, minute))
              updatedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
              updatedCalendar.set(Calendar.MINUTE, minute)
            },
            originalCalendar.get(Calendar.HOUR_OF_DAY), // current hour
            originalCalendar.get(Calendar.MINUTE), // current minute
            false) //no 24 hour view
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
      val builder: AlertDialog.Builder = AlertDialog.Builder(this@EditEntryActivity)
      
      // Sets theme based on VERSION_CODE
      
      builder.setTitle(R.string.delete_single_entry)
            .setMessage(R.string.delete_single_entry_message)
            .setPositiveButton(android.R.string.yes) { _, _ ->
              // continue with delete
              realm.executeTransaction {
                oldItem.deleteFromRealm()
                Toast.makeText(this, R.string.entry_deleted, Toast.LENGTH_SHORT).show()
                finish()
              }
            }
            .setNegativeButton(android.R.string.no) { dialog, _ ->
              // do nothing
              dialog.dismiss()
            }
            .setIcon(R.drawable.ic_trash)
            .show()
    }
    
    update_fab.setOnClickListener { updateEntry() }
  }
  
  private fun getOriginalValues() {
    originalStatus = oldItem.status
    originalCalendar.time = oldItem.date
    originalDate = originalCalendar.time
    originalBloodGlucose = oldItem.bloodGlucose
    originalCarbohydrates = oldItem.carbohydrates
    originalInsulin = oldItem.insulin
  }
  
  private fun setHints() {
    edit_status_selector.setSelection(originalStatus)
    edit_entry_date.hint = DATE_FORMAT.format(originalDate)
    edit_entry_time.hint = TWELVE_HOUR_TIME_FORMAT.format(originalDate)
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
    try {
      val dateToSave = Date()
      realm.beginTransaction()
      databaseManager.deleteEntry(oldItem)
      val itemToSave = EntryItem()
      itemToSave.status = updatedStatus
      if (updatedCalendar.timeInMillis != originalCalendar.timeInMillis) {
        dateToSave.time = updatedCalendar.timeInMillis
        itemToSave.date = dateToSave
      } else {
        dateToSave.time = originalCalendar.timeInMillis
        itemToSave.date = dateToSave
      }
      if (edit_entry_blood_glucose_level.text.toString() != "") {
        itemToSave.bloodGlucose = Integer.parseInt(edit_entry_blood_glucose_level.text.toString())
      }
      if (edit_entry_carbohydrates_amount.text.toString() != "") {
        itemToSave.carbohydrates = Integer.parseInt(edit_entry_carbohydrates_amount.text.toString())
      }
      if (edit_entry_insulin_units.text.toString() != "") {
        itemToSave.insulin = java.lang.Double.parseDouble(edit_entry_insulin_units.text.toString())
      }
      realm.copyToRealm(itemToSave)
      realm.commitTransaction()
    } finally {
      realm.close()
      finish()
    }
  }
  
  public override fun onDestroy() {
    realm.close()
    super.onDestroy()
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