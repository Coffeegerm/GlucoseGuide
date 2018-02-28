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

package io.github.coffeegerm.glucoseguide.ui.list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.glucoseguide.GlucoseGuide;
import io.github.coffeegerm.glucoseguide.R;
import io.github.coffeegerm.glucoseguide.data.model.EntryItem;
import io.github.coffeegerm.glucoseguide.ui.entry.EditEntryActivity;
import io.github.coffeegerm.glucoseguide.utils.Constants;
import io.github.coffeegerm.glucoseguide.utils.DateAssistant;

import static io.github.coffeegerm.glucoseguide.utils.Constants.DATE_FORMAT;
import static io.github.coffeegerm.glucoseguide.utils.Constants.PREF_DARK_MODE;
import static io.github.coffeegerm.glucoseguide.utils.Constants.TWELVE_HOUR_TIME_FORMAT;
import static io.github.coffeegerm.glucoseguide.utils.Constants.TWENTY_FOUR_HOUR_TIME_FORMAT;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Adapter used for filling RecyclerView within ListFragment
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
  
  private static int shortClickHintCount = 0;
  @Inject
  public SharedPreferences sharedPreferences;
  @Inject
  public DateAssistant dateAssistant;
  @Inject
  public Resources resources;
  @Inject
  public Context context;
  
  public EntryItem item;
  private LayoutInflater inflater;
  private List<EntryItem> entryItemList;
  
  ListAdapter(Context context) {
    this.inflater = LayoutInflater.from(context);
    GlucoseGuide.syringe.inject(this);
  }
  
  public void setListItems(List<EntryItem> providedList) {
    this.entryItemList = providedList;
  }
  
  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ListViewHolder(inflater.inflate(R.layout.item_list, parent, false));
  }
  
  @Override
  public void onBindViewHolder(final ListViewHolder holder, int position) {
    item = entryItemList.get(holder.getAdapterPosition());
    
    // Check if today or yesterday and set date accordingly
    int todayDay = dateAssistant.getTodayOfMonth();
    int yesterdayDay = dateAssistant.getYesterdayDayOfMonth();
    Calendar itemCalendar = dateAssistant.getToday();
    itemCalendar.setTime(item.getDate());
    int itemDay = dateAssistant.getSpecificCalendarDayOfMonth(itemCalendar);
    if (itemDay == todayDay) holder.date.setText(R.string.today);
    else if (itemDay == yesterdayDay) holder.date.setText(R.string.yesterday);
    else {
      holder.date.setText(DATE_FORMAT.format(item.getDate()));
    }
    
    // Set time based on user preference
    if (sharedPreferences.getBoolean(Constants.MILITARY_TIME, false)) {
      holder.time.setText(TWENTY_FOUR_HOUR_TIME_FORMAT.format(item.getDate()));
    } else {
      holder.time.setText(TWELVE_HOUR_TIME_FORMAT.format(item.getDate()));
    }
    
    holder.entryView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (shortClickHintCount <= 5) {
          Toast.makeText(context, R.string.list_item_short_click, Toast.LENGTH_SHORT).show();
          shortClickHintCount++;
        }
      }
    });
    
    holder.entryView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        Intent editEntryActivity = new Intent(context, EditEntryActivity.class);
        EntryItem selectedItem = entryItemList.get(holder.getAdapterPosition());
        editEntryActivity.putExtra(Constants.ITEM_ID, selectedItem.getId());
        context.startActivity(editEntryActivity);
        return true;
      }
    });
    
    if (String.valueOf(item.getBloodGlucose()).equals("0"))
      holder.bloodGlucose.setText(R.string.dash);
    else
      holder.bloodGlucose.setText(String.valueOf(item.getBloodGlucose()));
    
    if (String.valueOf(item.getCarbohydrates()).equals("0"))
      holder.carbohydrates.setText(R.string.dash);
    else
      holder.carbohydrates.setText(String.valueOf(item.getCarbohydrates()));
    
    if (String.valueOf(item.getInsulin()).equals("0.0"))
      holder.insulin.setText(R.string.dash);
    else
      holder.insulin.setText(String.valueOf(item.getInsulin()));
  }
  
  @Override
  public int getItemCount() {
    return entryItemList.size();
  }
  
  class ListViewHolder extends RecyclerView.ViewHolder {
    
    @BindView(R.id.item_list_date)
    TextView date;
    @BindView(R.id.item_list_time)
    TextView time;
    @BindView(R.id.item_list_blood_glucose)
    TextView bloodGlucose;
    @BindView(R.id.item_list_insulin)
    TextView insulin;
    @BindView(R.id.item_list_carbohydrates)
    TextView carbohydrates;
    @BindView(R.id.item_list_insulin_image)
    ImageView insulinImage;
    @BindView(R.id.item_list_image_carbs)
    ImageView imageCarbs;
    @BindView(R.id.item_list_image_blood_sugar)
    ImageView imageBloodGlucose;
    @BindView(R.id.item_list_line_left)
    View lineLeft;
    @BindView(R.id.item_list_line_right)
    View lineRight;
    
    private View entryView;
    
    ListViewHolder(View itemView) {
      super(itemView);
      this.entryView = itemView;
      ButterKnife.bind(this, itemView);
      
      if (sharedPreferences.getBoolean(PREF_DARK_MODE, false)) {
        imageBloodGlucose.setImageResource(R.drawable.ic_finger_dark);
        imageCarbs.setImageResource(R.drawable.ic_food_dark);
        insulinImage.setImageResource(R.drawable.ic_syringe_dark);
        int white = resources.getColor(R.color.white);
        lineLeft.setBackgroundColor(white);
        lineRight.setBackgroundColor(white);
      } else {
        imageBloodGlucose.setImageResource(R.drawable.ic_finger);
        imageCarbs.setImageResource(R.drawable.ic_food);
        insulinImage.setImageResource(R.drawable.ic_syringe);
      }
    }
  }
}
