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
import io.github.coffeegerm.materiallogbook.MaterialLogbookApplication;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.data.DatabaseManager;
import io.github.coffeegerm.materiallogbook.data.model.EntryItem;
import io.github.coffeegerm.materiallogbook.utils.Utilities;
import io.realm.Realm;
import io.realm.RealmResults;

public class ThreeMonthsStatisticsFragment extends Fragment {
  
  @Inject
  public SharedPreferences sharedPreferences;
  
  @Inject
  public Utilities utilities;
  
  @Inject
  public DatabaseManager databaseManager;
  
  @BindView(R.id.a_one_c)
  TextView a1c;
  @BindView(R.id.average)
  TextView average;
  @BindView(R.id.highest)
  TextView highest;
  @BindView(R.id.lowest)
  TextView lowest;
  @BindView(R.id.imgAvg)
  ImageView ivAvg;
  @BindView(R.id.imgUpArrow)
  ImageView ivUpArrow;
  @BindView(R.id.imgDownArrow)
  ImageView ivDownArrow;
  @BindView(R.id.ivA1C)
  ImageView ivA1C;
  
  Realm realm;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View threeMonths = inflater.inflate(R.layout.fragment_three_months_statistics, container, false);
    ButterKnife.bind(this, threeMonths);
    MaterialLogbookApplication.syringe.inject(this);
    realm = Realm.getDefaultInstance();
    setImages();
    setValues();
    return threeMonths;
  }
  
  private void setValues() {
    RealmResults<EntryItem> entries = realm.where(EntryItem.class).greaterThan("date", getThreeMonthsAgo()).greaterThan("bloodGlucose", 0).findAll();
    if (entries.size() == 0) {
      average.setText(R.string.dash);
      highest.setText(R.string.dash);
      lowest.setText(R.string.dash);
    } else {
      average.setText(String.valueOf(databaseManager.getAverageGlucose(getThreeMonthsAgo())));
      highest.setText(String.valueOf(databaseManager.getHighestGlucose(getThreeMonthsAgo())));
      lowest.setText(String.valueOf(databaseManager.getLowestGlucose(getThreeMonthsAgo())));
    }
    
    if (entries.size() < 300) {
      a1c.setText(R.string.dash);
    } else {
      a1c.setText(String.valueOf(getA1C(databaseManager.getAverageGlucose(getThreeMonthsAgo()))));
    }
  }
  
  public Date getThreeMonthsAgo() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -90);
    return calendar.getTime();
  }
  
  public double getA1C(int average) {
    return (46.7 + average) / 28.7;
  }
  
  private void setImages() {
    if (sharedPreferences.getBoolean("pref_dark_mode", false)) {
      ivAvg.setImageResource(R.drawable.ic_average_dark);
      ivUpArrow.setImageResource(R.drawable.ic_up_arrow_dark);
      ivDownArrow.setImageResource(R.drawable.ic_down_arrow_dark);
      ivA1C.setImageResource(R.drawable.ic_a1c_dark);
    }
  }
}
