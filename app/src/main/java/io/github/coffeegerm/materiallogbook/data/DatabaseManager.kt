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

package io.github.coffeegerm.materiallogbook.data

import io.github.coffeegerm.materiallogbook.data.model.EntryItem
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.util.*

class DatabaseManager {
  
  val realm: Realm = Realm.getDefaultInstance()
  
  fun getHighestGlucose(providedDate: Date): Int {
    var highest = 0
    val entriesFromLastThreeMonths = realm.where(EntryItem::class.java).greaterThan("date", providedDate).greaterThan("bloodGlucose", 0).findAll()
    entriesFromLastThreeMonths.indices
          .asSequence()
          .map { entriesFromLastThreeMonths[it]!! }
          .filter { it.bloodGlucose > highest }
          .forEach { highest = it.bloodGlucose }
    return highest
  }
  
  fun getAverageGlucose(providedDate: Date): Int {
    val entriesFromLastThreeMonths = realm.where(EntryItem::class.java).greaterThan("date", providedDate).greaterThan("bloodGlucose", 0).findAll()
    val total = entriesFromLastThreeMonths.indices
          .map { entriesFromLastThreeMonths[it]!! }
          .sumBy { it.bloodGlucose }
    return total / entriesFromLastThreeMonths.size
  }
  
  fun getLowestGlucose(providedDate: Date): Int {
    var lowest = 1000
    val entriesFromLastThreeDays = realm.where(EntryItem::class.java).greaterThan("date", providedDate).greaterThan("bloodGlucose", 0).findAll()
    entriesFromLastThreeDays.indices
          .asSequence()
          .map { entriesFromLastThreeDays[it]!! }
          .filter { it.bloodGlucose < lowest }
          .forEach { lowest = it.bloodGlucose }
    return lowest
  }
  
  fun getAllSortedAscending(): RealmResults<EntryItem> = realm.where(EntryItem::class.java).sort("date", Sort.ASCENDING).findAll()
  
  fun getAllFromDate(date: Date): RealmResults<EntryItem> = realm.where(EntryItem::class.java).greaterThan("date", date).greaterThan("bloodGlucose", 0).findAll();
  
}
