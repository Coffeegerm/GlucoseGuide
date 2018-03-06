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

package io.github.coffeegerm.glucoseguide.ui.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.data.model.EntryItem
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.item_empty_list.*
import javax.inject.Inject

class ListFragment : Fragment() {
  
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  @Inject
  lateinit var listViewModelFactory: ListViewModelFactory
  
  private lateinit var listAdapter: ListAdapter
  private lateinit var listViewModel: ListViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GlucoseGuide.syringe.inject(this)
    listAdapter = context?.let { ListAdapter(it) }!!
    listViewModel = ViewModelProviders.of(this, listViewModelFactory).get(ListViewModel::class.java)
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_list, container, false)
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    list_recycler_view.adapter = listAdapter
    listViewModel.getLiveData().observe(this, Observer<RealmResults<EntryItem>> { entries -> checkListSize(entries!!) })
  }
  
  private fun checkListSize(entriesToShow: RealmResults<EntryItem>) {
    if (entriesToShow.size == 0) {
      list_recycler_view.visibility = View.GONE
      empty_item_list.visibility = View.VISIBLE
    } else {
      list_recycler_view.visibility = View.VISIBLE
      empty_item_list.visibility = View.GONE
      list_recycler_view.layoutManager = LinearLayoutManager(activity)
      setAdapterItems(entriesToShow)
    }
  }
  
  private fun setAdapterItems(entriesToShow: RealmResults<EntryItem>) {
    listAdapter.setListItems(entriesToShow)
    listAdapter.notifyDataSetChanged()
  }
}
