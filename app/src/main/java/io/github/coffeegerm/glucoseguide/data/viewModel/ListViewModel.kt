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

package io.github.coffeegerm.glucoseguide.data.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.coffeegerm.glucoseguide.data.DatabaseManager
import io.github.coffeegerm.glucoseguide.data.model.Entry
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.realm.Sort

class ListViewModel(var databaseManager: DatabaseManager) : ViewModel() {
  
  private val entriesLiveData = MutableLiveData<RealmResults<Entry>>()
  private val entries: RealmResults<Entry> = databaseManager.getAllSortedDescending()
  
  init {
    entriesLiveData.postValue(databaseManager.getAllSortedDescending())
    entries.addChangeListener(RealmChangeListener<RealmResults<Entry>> {
      it.sort("date", Sort.DESCENDING)
      entriesLiveData.postValue(it)
    })
  }
}
