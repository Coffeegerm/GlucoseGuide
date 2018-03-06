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

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import io.github.coffeegerm.glucoseguide.GlucoseGuide.Companion.syringe
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.data.model.EntryItem
import io.github.coffeegerm.glucoseguide.utils.Constants.*
import io.github.coffeegerm.glucoseguide.utils.NotificationPublisher
import io.github.coffeegerm.glucoseguide.utils.Utilities
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_new_entry.*
import java.util.*
import javax.inject.Inject

class NewEntryActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
  
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  
  @Inject
  lateinit var utilities: Utilities
  
  private var realm: Realm = Realm.getDefaultInstance()
  private lateinit var calendarToBeSaved: Calendar
  private lateinit var calendar: Calendar
  private lateinit var alarmCalendar: Calendar
  private var status = 0
  private var wantsReminder = false
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    syringe.inject(this)
    if (sharedPreferences.getBoolean(PREF_DARK_MODE, false))
      setTheme(R.style.AppTheme_Dark)
    setContentView(R.layout.activity_new_entry)
    setSupportActionBar(new_entry_toolbar)
    supportActionBar?.setTitle(R.string.create_entry)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    
    calendar = Calendar.getInstance()
    // Calendar for saving entered Date and Time
    calendarToBeSaved = Calendar.getInstance()
    
    // Set date and time to current date and time on initial create
    val year = calendar.get(Calendar.YEAR)
    var month = calendar.get(Calendar.MONTH)
    month++
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    
    new_entry_date.setText(utilities.formatDate(month, day, year))
    new_entry_time.setText(utilities.checkTimeString(hour, minute))
    
    new_entry_carbohydrates_amount.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
      
      override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
      
      override fun afterTextChanged(carbValue: Editable) {
        if (carbValue.toString() != "") {
          insulin_suggestion.visibility = View.VISIBLE
          calculateInsulin(java.lang.Float.parseFloat(carbValue.toString()))
        } else {
          insulin_suggestion.visibility = View.GONE
        }
      }
    })
    
    new_entry_date.setOnClickListener {
      val dialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
              var correctMonth = month
              correctMonth++
              new_entry_date.setText(utilities.formatDate(correctMonth, dayOfMonth, year))
              correctMonth--
              calendarToBeSaved.set(year, correctMonth, dayOfMonth)
            }, calendar.get(Calendar.YEAR), // year
            calendar.get(Calendar.MONTH), // month
            calendar.get(Calendar.DAY_OF_MONTH)) // day
      
      if (Build.VERSION.SDK_INT < 21)
        if (dialog.window != null)
          dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
      
      dialog.show()
    }
    
    new_entry_time.setOnClickListener {
      val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
              new_entry_time.setText(utilities.checkTimeString(hourOfDay, minute))
              calendarToBeSaved.set(Calendar.HOUR_OF_DAY, hourOfDay)
              calendarToBeSaved.set(Calendar.MINUTE, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY), // current hour
            calendar.get(Calendar.MINUTE), // current minute
            false) //no 24 hour view
      timePickerDialog.show()
    }
    
    reminder_alarm.setOnClickListener { alarmTimePicker() }
    save_entry_fab.setOnClickListener { saveEntry() }
  
    val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.status_selection))
    status_selector.adapter = spinnerAdapter
    status_selector.onItemSelectedListener = this
  }
  
  override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
    status_selector.setSelection(position)
    status = position
  }
  
  override fun onNothingSelected(p0: AdapterView<*>?) {}
  
  private fun saveEntry() {
    // Checks to make sure there is a blood glucose given.
    if (new_entry_blood_glucose_level.text.toString() == "")
      Toast.makeText(this, R.string.no_glucose_toast, Toast.LENGTH_SHORT).show()
    else {
      realm.executeTransaction {
        // Save Entry to database
        val entryItem = this.realm.createObject(EntryItem::class.java)
        // Creates Date object made from the DatePicker and TimePicker
        val date = calendarToBeSaved.time
        entryItem.date = date
        entryItem.status = status
        entryItem.bloodGlucose = Integer.parseInt(new_entry_blood_glucose_level.text.toString())
        // Prevention of NullPointerException
        if (new_entry_carbohydrates_amount.text.toString() != "") {
          entryItem.carbohydrates = Integer.parseInt(new_entry_carbohydrates_amount.text.toString())
        }
        // Prevention of NullPointerException
        if (new_entry_insulin_units.text.toString() != "") {
          entryItem.insulin = java.lang.Double.parseDouble(new_entry_insulin_units.text.toString())
        }
      }
      // If the user chooses to have a reminder at certain time.
      if (wantsReminder) createReminder(getNotification())
      // After save returns to MainActivity ListFragment
      finish()
    }
  }
  
  private fun calculateInsulin(carbValue: Float) {
    val bolusRatio = sharedPreferences.getInt(BOLUS_RATIO, 0).toFloat()
    val suggestionInsulin = carbValue / bolusRatio
    insulin_suggestion_value.text = suggestionInsulin.toString()
  }
  
  private fun alarmTimePicker() {
    // Get Current Time
    val defaultTime = Calendar.getInstance()
    alarmCalendar = Calendar.getInstance()
    val hour = defaultTime.get(Calendar.HOUR_OF_DAY) + 2
    val minute = defaultTime.get(Calendar.MINUTE)
    
    // Launch Time Picker Dialog
    val timePickerDialog = TimePickerDialog(this,
          TimePickerDialog.OnTimeSetListener { _, hourOfDay, anotherMinute ->
            alarmCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            alarmCalendar.set(Calendar.MINUTE, anotherMinute)
            reminder_alarm.setText(utilities.checkTimeString(hourOfDay, anotherMinute))
            wantsReminder = true
          }, hour, minute, false)
    timePickerDialog.show()
  }
  
  private fun createReminder(notification: Notification) {
    val notificationIntent = Intent(this, NotificationPublisher::class.java)
    notificationIntent.putExtra(NOTIFICATION_ID, 1)
    notificationIntent.putExtra(NOTIFICATION, notification)
    val pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    val delay = alarmCalendar.timeInMillis - Calendar.getInstance().timeInMillis
    val futureInMillis = SystemClock.elapsedRealtime() + delay
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmHour = alarmCalendar.get(Calendar.HOUR_OF_DAY).toString()
    val alarmMinute = alarmCalendar.get(Calendar.MINUTE).toString()
    val alarmString = StringBuilder().append("Alarm set for ").append(alarmHour).append(":").append(alarmMinute)
    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)
    Toast.makeText(this, alarmString, Toast.LENGTH_SHORT).show()
  }
  
  private fun getNotification(): Notification {
    val channelId = "Reminders"
    val newEntryActivityPendingIntent = PendingIntent.getActivity(this, 1, Intent(this, NewEntryActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
    val builder = NotificationCompat.Builder(this, channelId)
          .setContentTitle(getString(R.string.app_name))
          .setContentText(getString(R.string.reminder_content))
          .setTicker(getString(R.string.app_name))
          .setSmallIcon(R.drawable.notebook_notification_white)
          .setDefaults(Notification.DEFAULT_SOUND)
          .setAutoCancel(true)
          .setContentIntent(newEntryActivityPendingIntent)
    return builder.build()
  }
  
  public override fun onDestroy() {
    realm.close()
    super.onDestroy()
  }
  
  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return super.onSupportNavigateUp()
  }
}