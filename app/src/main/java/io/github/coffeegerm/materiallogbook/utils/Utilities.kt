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

package io.github.coffeegerm.materiallogbook.utils

import android.content.SharedPreferences
import io.github.coffeegerm.materiallogbook.MaterialLogbookApplication
import io.github.coffeegerm.materiallogbook.model.EntryItem
import io.realm.Realm
import java.util.*
import javax.inject.Inject

class Utilities {
  
  init {
    MaterialLogbookApplication.syringe.inject(this)
  }
  
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  
  // Static method to edit Time String in NewEntryActivity
  fun checkTimeString(hourOfDay: Int, minute: Int): String {
    var hour = hourOfDay
    val timeSet: String
    val min: String = if (minute < 10)
      "0" + minute
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
  
  // Calculates the glucose grade based on user
  // sugar from last three days
  fun getGlucoseGrade(): String {
    val realm = Realm.getDefaultInstance()
    val grade: String
    val hyperglycemicIndex = sharedPreferences.getInt("hyperglycemicIndex", 0)
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -3)
    val threeDaysAgo = calendar.time
    val entriesFromLastThreeDays = realm.where(EntryItem::class.java).greaterThan("date", threeDaysAgo).greaterThan("bloodGlucose", 0).findAll()
    val hyperglycemicCount = entriesFromLastThreeDays.indices
          .map { entriesFromLastThreeDays[it]!! }
          .count { it.bloodGlucose > hyperglycemicIndex }
    when {
      hyperglycemicCount == 0 -> grade = "-"
      hyperglycemicCount <= 3 -> grade = "A+"
      hyperglycemicCount == 4 -> grade = "A"
      hyperglycemicCount == 5 -> grade = "A-"
      hyperglycemicCount == 6 -> grade = "B+"
      hyperglycemicCount == 7 -> grade = "B"
      hyperglycemicCount == 8 -> grade = "B-"
      hyperglycemicCount == 9 -> grade = "C+"
      hyperglycemicCount == 10 -> grade = "C"
      hyperglycemicCount == 11 -> grade = "C-"
      hyperglycemicCount == 12 -> grade = "D+"
      hyperglycemicCount == 13 -> grade = "D"
      hyperglycemicCount == 14 -> grade = "D-"
      else -> grade = "F"
    }
    return grade
  }
  
}