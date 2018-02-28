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

package io.github.coffeegerm.glucoseguide.ui.statistics.children;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.glucoseguide.GlucoseGuide;
import io.github.coffeegerm.glucoseguide.R;
import io.github.coffeegerm.glucoseguide.data.DatabaseManager;
import io.github.coffeegerm.glucoseguide.data.model.EntryItem;
import io.realm.RealmResults;

/**
 * Fragment used with Statistics ViewPager to show
 * the amount of all statistics
 */

public class AllStatisticsFragment extends Fragment {
  
  @Inject
  public SharedPreferences sharedPreferences;
  @Inject
  public DatabaseManager databaseManager;
  
  @BindView(R.id.all_days_statistics_average)
  TextView averageBloodGlucose;
  @BindView(R.id.highest_of_all_glucose)
  TextView highestBloodGlucose;
  @BindView(R.id.lowest_of_all_glucose)
  TextView lowestBloodGlucose;
  
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GlucoseGuide.syringe.inject(this);
  }
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View allStatsView = inflater.inflate(R.layout.fragment_all_stats, container, false);
    ButterKnife.bind(this, allStatsView);
    setValues();
    return allStatsView;
  }
  
  public void setValues() {
    RealmResults<EntryItem> entryItems = databaseManager.getAllSortedDescending();
    if (entryItems.size() == 0) {
      averageBloodGlucose.setText(R.string.dash);
      highestBloodGlucose.setText(R.string.dash);
      lowestBloodGlucose.setText(R.string.dash);
    } else {
      averageBloodGlucose.setText(String.valueOf(getAverage(entryItems)));
      highestBloodGlucose.setText(String.valueOf(getHighestBloodGlucose(entryItems)));
      lowestBloodGlucose.setText(String.valueOf(getLowestBloodGlucose(entryItems)));
    }
  }
  
  public int getAverage(RealmResults<EntryItem> entryItems) {
    int averageCalculated = 0;
    if (entryItems.size() == 0) {
      Toast.makeText(getContext(), "Unable to show data at this time.", Toast.LENGTH_SHORT).show();
    } else {
      int total = 0;
      for (int position = 0; position < entryItems.size(); position++) {
        EntryItem item = entryItems.get(position);
        assert item != null;
        total += item.getBloodGlucose();
      }
      averageCalculated = total / entryItems.size();
    }
    return averageCalculated;
  }
  
  public int getHighestBloodGlucose(RealmResults<EntryItem> entryItems) {
    int highest = 0;
    for (int position = 0; position < entryItems.size(); position++) {
      EntryItem item = entryItems.get(position);
      assert item != null;
      if (item.getBloodGlucose() > highest) {
        highest = item.getBloodGlucose();
      }
    }
    return highest;
  }
  
  
  public int getLowestBloodGlucose(RealmResults<EntryItem> entryItems) {
    int lowest = 1000;
    for (int position = 0; position < entryItems.size(); position++) {
      EntryItem item = entryItems.get(position);
      assert item != null;
      if (item.getBloodGlucose() < lowest) {
        lowest = item.getBloodGlucose();
      }
    }
    return lowest;
  }
}
