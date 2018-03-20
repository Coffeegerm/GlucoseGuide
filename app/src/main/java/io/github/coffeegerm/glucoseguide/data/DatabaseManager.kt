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

package io.github.coffeegerm.glucoseguide.data

import io.github.coffeegerm.glucoseguide.data.model.Entry
import io.github.coffeegerm.glucoseguide.utils.DateAssistant
import io.github.coffeegerm.glucoseguide.utils.SharedPreferencesManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.util.*
import javax.inject.Inject

class DatabaseManager @Inject constructor(private var realmTransactions: RealmTransactions, private var dateAssistant: DateAssistant, private var sharedPreferencesManager: SharedPreferencesManager) {
  
  val realm: Realm = Realm.getDefaultInstance()
  
  fun getEntryFromId(entryId: String) = realm.where(Entry::class.java).equalTo("id", entryId).findFirst()
  
  fun insertToRealm(item: Entry) = realmTransactions.insertEntryToRealm(item)
  
  fun deleteEntry(item: Entry) = realmTransactions.deleteEntry(item)
  
  fun copyToRealm(item: Entry) = realmTransactions.copyEntryToRealm(item)
  
  fun getAverage(): Int {
    val entryItems = realm.where(Entry::class.java).findAll()
    var total = 0
    for (position in entryItems.indices) {
      val item = entryItems[position]!!
      total += item.bloodGlucose
    }
    return total / entryItems.size
  }
  
  fun getHighestBloodGlucose(): Int {
    val entryItems = realm.where(Entry::class.java).findAll()
    var highest = 0
    for (position in entryItems.indices) {
      val item = entryItems[position]!!
      if (item.bloodGlucose > highest) {
        highest = item.bloodGlucose
      }
    }
    return highest
  }
  
  
  fun getLowestBloodGlucose(): Int {
    val entryItems = realm.where(Entry::class.java).findAll()
    var lowest = 1000
    for (position in entryItems.indices) {
      val item = entryItems[position]!!
      if (item.bloodGlucose < lowest) {
        lowest = item.bloodGlucose
      }
    }
    return lowest
  }
  
  fun getHighestGlucoseFromDate(providedDate: Date): Int {
    var highest = 0
    val entriesToCheck = realm.where(Entry::class.java).greaterThan("date", providedDate).greaterThan("bloodGlucose", 0).findAll()
    entriesToCheck.indices
          .asSequence()
          .map { entriesToCheck[it]!! }
          .filter { it.bloodGlucose > highest }
          .forEach { highest = it.bloodGlucose }
    return highest
  }
  
  fun getAverageGlucoseFromDate(providedDate: Date): Int {
    val entriesToCheck = realm.where(Entry::class.java).greaterThan("date", providedDate).greaterThan("bloodGlucose", 0).findAll()
    val total = entriesToCheck.indices
          .map { entriesToCheck[it]!! }
          .sumBy { it.bloodGlucose }
    return total / entriesToCheck.size
  }
  
  fun getLowestGlucoseFromDate(providedDate: Date): Int {
    var lowest = 1000
    val entriesTOCheck = realm.where(Entry::class.java).greaterThan("date", providedDate).greaterThan("bloodGlucose", 0).findAll()
    entriesTOCheck.indices
          .asSequence()
          .map { entriesTOCheck[it]!! }
          .filter { it.bloodGlucose < lowest }
          .forEach { lowest = it.bloodGlucose }
    return lowest
  }
  
  fun getGlucoseGrade(): String {
    val grade: String
    val hyperglycemicIndex = sharedPreferencesManager.getInt("hyperglycemicIndex")
    val entriesFromLastThreeDays = getAllFromDate(dateAssistant.getThreeDaysAgoDate())
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
  
  fun getAllSortedDescending(): RealmResults<Entry> = realm.where(Entry::class.java).sort("date", Sort.DESCENDING).findAll()
  
  fun getAllFromDate(date: Date): RealmResults<Entry> = realm.where(Entry::class.java).greaterThan("date", date).greaterThan("bloodGlucose", 0).findAll()
  
}
