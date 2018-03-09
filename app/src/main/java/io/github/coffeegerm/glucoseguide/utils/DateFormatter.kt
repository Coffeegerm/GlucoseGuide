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

package io.github.coffeegerm.glucoseguide.utils

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Class dedicated to formatting and returning
 * formatted dates for various classes to use
 */

class DateFormatter @Inject constructor() {
  
  private val standardDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
  private val twelveHourTimeFormat = SimpleDateFormat("hh:mm aa", Locale.US)
  private val twentyFourHourTimeFormat = SimpleDateFormat("HH:mm", Locale.US)
  
  fun formatDate(providedDate: Date?): String = standardDateFormat.format(providedDate)
  
  fun twelveHourFormat(providedDate: Date?): String = twelveHourTimeFormat.format(providedDate)
  
  fun twentyFourHourFormat(providedDate: Date?): String = twentyFourHourTimeFormat.format(providedDate)
  
  fun formatDateForEditText(month: Int, day: Int, year: Int): StringBuilder = StringBuilder().append(month).append("/").append(day).append("/").append(year)
  
  fun formatTimeForEditText(hourOfDay: Int, minute: Int): String {
    var hour = hourOfDay
    val timeSet: String
    val min: String = if (minute < 10)
      "0$minute"
    else
      minute.toString()
    when {
      hour > 12 -> {
        hour -= 12
        timeSet = "PM"
      }
      hour == 0 -> {
        hour += 12
        timeSet = "AM"
      }
      hour == 12 -> timeSet = "PM"
      else -> timeSet = "AM"
    }
    return hour.toString() + ":" + min + " " + timeSet
  }
}