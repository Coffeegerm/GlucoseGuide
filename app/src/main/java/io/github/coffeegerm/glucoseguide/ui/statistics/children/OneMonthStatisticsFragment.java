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

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.glucoseguide.GlucoseGuide;
import io.github.coffeegerm.glucoseguide.R;
import io.github.coffeegerm.glucoseguide.data.DatabaseManager;
import io.github.coffeegerm.glucoseguide.utils.Utilities;

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
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View oneMonth = inflater.inflate(R.layout.fragment_one_month_statistics, container, false);
    GlucoseGuide.syringe.inject(this);
    ButterKnife.bind(this, oneMonth);
    setValues();
    return oneMonth;
  }
  
  private void setValues() {
    if (databaseManager.getAllFromDate(getOneMonthAgo()).size() == 0) {
      average.setText(R.string.dash);
      highest.setText(R.string.dash);
      lowest.setText(R.string.dash);
    } else {
      average.setText(String.valueOf(databaseManager.getAverageGlucose(getOneMonthAgo())));
      highest.setText(String.valueOf(databaseManager.getHighestGlucose(getOneMonthAgo())));
      lowest.setText(String.valueOf(databaseManager.getLowestGlucose(getOneMonthAgo())));
    }
  }
  
  private Date getOneMonthAgo() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -30);
    return calendar.getTime();
  }
}
