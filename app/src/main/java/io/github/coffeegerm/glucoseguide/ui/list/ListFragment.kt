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
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.data.EntryListViewModel
import io.github.coffeegerm.glucoseguide.data.model.Entry
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.item_empty_list.*
import java.util.*

class ListFragment : Fragment() {
  
  private lateinit var listAdapter: ListAdapter
  private lateinit var entryListViewModel: EntryListViewModel
  private var entryList: List<Entry> = Collections.emptyList()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    listAdapter = context?.let { ListAdapter(it) }!!
    entryListViewModel = ViewModelProviders.of(this).get(EntryListViewModel::class.java)
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_list, container, false)
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    list_recycler_view.adapter = listAdapter
    entryListViewModel.getEntries().observe(this, Observer<List<Entry>> { entries ->
      entryList = entries!!
      updateUI(entryList)
    })
  }
  
  private fun updateUI(entriesToShow: List<Entry>) {
    if (entriesToShow.isEmpty()) {
      list_recycler_view.visibility = View.GONE
      empty_item_list.visibility = View.VISIBLE
    } else {
      list_recycler_view.visibility = View.VISIBLE
      empty_item_list.visibility = View.GONE
      list_recycler_view.layoutManager = LinearLayoutManager(activity)
      list_recycler_view.setHasFixedSize(true)
      listAdapter.updateEntries(entriesToShow)
    }
  }
}
