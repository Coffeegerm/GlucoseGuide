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

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.data.model.EntryItem
import io.github.coffeegerm.glucoseguide.ui.entry.EditEntryActivity
import io.github.coffeegerm.glucoseguide.utils.Constants
import io.github.coffeegerm.glucoseguide.utils.DateFormatter
import io.github.coffeegerm.glucoseguide.utils.SharedPreferenceManager
import kotlinx.android.synthetic.main.item_list.view.*
import javax.inject.Inject

class ListViewHolder(private val entryView: View) : RecyclerView.ViewHolder(entryView) {
  
  init {
    GlucoseGuide.syringe.inject(this)
  }
  
  @Inject
  lateinit var dateFormatter: DateFormatter
  @Inject
  lateinit var sharedPreferencesManager: SharedPreferenceManager
  
  fun bindEntry(entry: EntryItem) {
    val context = entryView.context
    entryView.item_list_date.text = dateFormatter.formatDate(entry.date)
    
    if (sharedPreferencesManager.getBoolean(Constants.MILITARY_TIME)) {
      entryView.item_list_time.text = entry.date?.let { dateFormatter.twentyFourHourFormat(it) }
    } else {
      entryView.item_list_time.text = entry.date?.let { dateFormatter.twelveHourFormat(it) }
    }
    
    entryView.setOnClickListener {
      Toast.makeText(context, R.string.list_item_short_click, Toast.LENGTH_SHORT).show()
    }
    
    entryView.setOnLongClickListener {
      val editEntryActivity = Intent(context, EditEntryActivity::class.java)
      editEntryActivity.putExtra(Constants.ITEM_ID, entry.id)
      context.startActivity(editEntryActivity)
      true
    }
    
    if (entry.bloodGlucose.toString() != "0")
      entryView.item_list_blood_glucose.text = entry.bloodGlucose.toString()
    
    if (entry.carbohydrates.toString() != "0")
      entryView.item_list_carbohydrates.text = entry.carbohydrates.toString()
    
    if (entry.insulin.toString() != "0.0")
      entryView.item_list_insulin.text = entry.insulin.toString()
    
    if (entry.status > 0) {
      entryView.status_image.visibility = View.VISIBLE
      when (entry.status) {
        1 -> entryView.status_image.setImageDrawable(context.resources.getDrawable(R.drawable.breakfast))
        2 -> entryView.status_image.setImageDrawable(context.resources.getDrawable(R.drawable.lunch))
        3 -> entryView.status_image.setImageDrawable(context.resources.getDrawable(R.drawable.dinner))
        4 -> entryView.status_image.setImageDrawable(context.resources.getDrawable(R.drawable.sweets))
        5 -> entryView.status_image.setImageDrawable(context.resources.getDrawable(R.drawable.sick))
        6 -> entryView.status_image.setImageDrawable(context.resources.getDrawable(R.drawable.exercise))
      }
    }
  }
  
}