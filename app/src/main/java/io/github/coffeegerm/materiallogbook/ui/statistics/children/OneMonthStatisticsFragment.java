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

/**
 * Created by dyarz on 8/15/2017.
 * <p>
 * Fragment for Statistics ViewPager to show
 * one month data for user.
 */

public class OneMonthStatisticsFragment extends Fragment {
  
  @Inject
  public SharedPreferences sharedPreferences;
  
  @Inject
  public Utilities utilities;
  
  @Inject
  public DatabaseManager databaseManager;
  
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
  private Realm realm;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View oneMonth = inflater.inflate(R.layout.fragment_one_month_statistics, container, false);
    MaterialLogbookApplication.syringe.inject(this);
    ButterKnife.bind(this, oneMonth);
    realm = Realm.getDefaultInstance();
    setImages();
    setValues();
    return oneMonth;
  }
  
  private void setValues() {
    Date oneMonthAgo = getOneMonthAgo();
    RealmResults<EntryItem> entriesFromPastMonth = realm.where(EntryItem.class).greaterThan("date", oneMonthAgo).greaterThan("bloodGlucose", 0).findAll();
    if (entriesFromPastMonth.size() == 0) {
      average.setText(R.string.dash);
      highest.setText(R.string.dash);
      lowest.setText(R.string.dash);
    } else {
      average.setText(String.valueOf(databaseManager.getAverageGlucose(oneMonthAgo)));
      highest.setText(String.valueOf(databaseManager.getHighestGlucose(oneMonthAgo)));
      lowest.setText(String.valueOf(databaseManager.getLowestGlucose(oneMonthAgo)));
    }
  }
  
  private Date getOneMonthAgo() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -30);
    return calendar.getTime();
  }
  
  private void setImages() {
    if (sharedPreferences.getBoolean("pref_dark_mode", false)) {
      ivAvg.setImageResource(R.drawable.ic_average_dark);
      ivUpArrow.setImageResource(R.drawable.ic_up_arrow_dark);
      ivDownArrow.setImageResource(R.drawable.ic_down_arrow_dark);
    }
  }
}
