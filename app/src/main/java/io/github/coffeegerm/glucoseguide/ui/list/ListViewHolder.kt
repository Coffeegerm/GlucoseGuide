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

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import io.github.coffeegerm.glucoseguide.R

class ListViewHolder(val entryView: View) : RecyclerView.ViewHolder(entryView) {
  
  @BindView(R.id.item_list_date)
  lateinit var date: TextView
  @BindView(R.id.item_list_time)
  lateinit var time: TextView
  @BindView(R.id.item_list_blood_glucose)
  lateinit var bloodGlucose: TextView
  @BindView(R.id.item_list_insulin)
  lateinit var insulin: TextView
  @BindView(R.id.item_list_carbohydrates)
  lateinit var carbohydrates: TextView
  @BindView(R.id.status_image)
  lateinit var statusImage: ImageView
  
  init {
    ButterKnife.bind(this, entryView)
  }
}