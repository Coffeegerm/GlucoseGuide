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

package io.github.coffeegerm.materiallogbook.ui.statistics.children;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.MaterialLogbook;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.data.DatabaseManager;
import io.github.coffeegerm.materiallogbook.data.model.EntryItem;
import io.github.coffeegerm.materiallogbook.utils.Utilities;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by David Yarzebinski on 7/28/2017.
 * <p>
 * Fragment used with Statistics ViewPager to show
 * the last three days of statistics
 */

public class ThreeDayStatisticsFragment extends Fragment {
  
  @Inject
  public SharedPreferences sharedPreferences;
  
  @Inject
  public Utilities utilities;
  
  @Inject
  public DatabaseManager databaseManager;
  
  @BindView(R.id.three_days_average)
  TextView average;
  @BindView(R.id.three_days_highest)
  TextView highest;
  @BindView(R.id.three_days_lowest)
  TextView lowest;
  @BindView(R.id.imgAvg)
  ImageView ivAvg;
  @BindView(R.id.imgUpArrow)
  ImageView ivUpArrow;
  @BindView(R.id.imgDownArrow)
  ImageView ivDownArrow;
  
  Realm realm;
  
  int hyperglycemicIndex;
  int hypoglycemicIndex;
  
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MaterialLogbook.syringe.inject(this);
  }
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View threeDaysStatisticsView = inflater.inflate(R.layout.fragment_three_days_stats, container, false);
    ButterKnife.bind(this, threeDaysStatisticsView);
    realm = Realm.getDefaultInstance();
    setValues();
    setImages();
    hyperglycemicIndex = sharedPreferences.getInt("hyperglycemicIndex", 0);
    hypoglycemicIndex = sharedPreferences.getInt("hypoglycemicIndex", 0);
    return threeDaysStatisticsView;
  }
  
  private void setValues() {
    if (getValues().size() == 0) {
      average.setText(R.string.dash);
      highest.setText(R.string.dash);
      lowest.setText(R.string.dash);
    } else {
      average.setText(String.valueOf(databaseManager.getAverageGlucose(getDateThreeDaysAgo())));
      highest.setText(String.valueOf(databaseManager.getHighestGlucose(getDateThreeDaysAgo())));
      lowest.setText(String.valueOf(databaseManager.getLowestGlucose(getDateThreeDaysAgo())));
    }
  }
  
  public Date getDateThreeDaysAgo() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -3);
    return calendar.getTime();
  }
  
  private RealmResults<EntryItem> getValues() {
    return realm.where(EntryItem.class).greaterThan("date", getDateThreeDaysAgo()).greaterThan("bloodGlucose", 0).findAll();
  }
  
  private void setImages() {
    if (sharedPreferences.getBoolean("pref_dark_mode", false)) {
      ivAvg.setImageResource(R.drawable.ic_average_dark);
      ivUpArrow.setImageResource(R.drawable.ic_up_arrow_dark);
      ivDownArrow.setImageResource(R.drawable.ic_down_arrow_dark);
    }
  }
}
