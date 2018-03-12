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

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.data.model.EntryItem

class ListAdapter internal constructor(var context: Context) : RecyclerView.Adapter<ListViewHolder>() {
  
  
  private var inflater: LayoutInflater = LayoutInflater.from(context)
  private var entryItemList: List<EntryItem>? = null
  
  fun setListItems(providedList: List<EntryItem>) {
    this.entryItemList = providedList
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder = ListViewHolder(inflater.inflate(R.layout.item_list, parent, false))
  
  override fun onBindViewHolder(holder: ListViewHolder, position: Int) = holder.bindEntry(entryItemList!![position])
  
  override fun getItemCount(): Int = entryItemList!!.size
}
