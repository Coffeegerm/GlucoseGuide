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

package io.github.coffeegerm.glucoseguide.ui.list

import android.arch.lifecycle.ViewModel
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.data.DatabaseManager
import io.github.coffeegerm.glucoseguide.data.model.EntryItem
import io.realm.RealmResults
import javax.inject.Inject

/**
 * TODO: Add class comment header
 */
class ListViewModel : ViewModel() {
  
  @Inject
  lateinit var databaseManager: DatabaseManager
  
  init {
    GlucoseGuide.syringe.inject(this)
  }
  
  fun getEntries(): RealmResults<EntryItem> {
    return databaseManager.getAllSortedAscending()
  }
}