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

package io.github.coffeegerm.materiallogbook.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;

import static io.github.coffeegerm.materiallogbook.utils.Constants.BOLUS_RATIO;
import static io.github.coffeegerm.materiallogbook.utils.Constants.HYPERGLYCEMIC_INDEX;
import static io.github.coffeegerm.materiallogbook.utils.Constants.HYPOGLYCEMIC_INDEX;
import static io.github.coffeegerm.materiallogbook.utils.Constants.PREF_DARK_MODE;

/**
 * Created by dyarz on 10/6/2017.
 * <p>
 * Sub menu of SettingsActivity for Treatment section
 */

public class SettingsTreatmentActivity extends AppCompatActivity {

    @BindView(R.id.hyperglycemic_edit_text)
    EditText hyperglycemicEditText;
    @BindView(R.id.hypoglycemic_edit_text)
    EditText hypoglycemicEditText;
    @BindView(R.id.bolus_ratio)
    EditText bolusRatio;
    @BindView(R.id.treatment_toolbar)
    Toolbar treatmentToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.sharedPreferences.getBoolean(PREF_DARK_MODE, false)) {
            setTheme(R.style.AppTheme_Dark);
        }
        setContentView(R.layout.activity_settings_treatment);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setupToolbar();
        checkRangeStatus();
        setHints();
        hypoglycemicEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(""))
                    MainActivity.sharedPreferences.edit()
                            .putInt(HYPOGLYCEMIC_INDEX, Integer.parseInt(s.toString())).apply();
            }
        });

        hyperglycemicEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) MainActivity.sharedPreferences.edit()
                        .putInt(HYPERGLYCEMIC_INDEX, Integer.parseInt(s.toString())).apply();
            }
        });

        bolusRatio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) MainActivity.sharedPreferences.edit()
                        .putInt(BOLUS_RATIO, Integer.parseInt(editable.toString())).apply();
            }
        });
    }

    public void checkRangeStatus() {
        int hyperglycemicIndex = MainActivity.sharedPreferences.getInt(HYPERGLYCEMIC_INDEX, 0);
        int hypoglycemicIndex = MainActivity.sharedPreferences.getInt(HYPOGLYCEMIC_INDEX, 0);
        int bolusRatio = MainActivity.sharedPreferences.getInt(BOLUS_RATIO, 0);
        if (hyperglycemicIndex == 0 && hypoglycemicIndex == 0) {
            MainActivity.sharedPreferences.edit().putInt(HYPOGLYCEMIC_INDEX, 80).apply();
            MainActivity.sharedPreferences.edit().putInt(HYPERGLYCEMIC_INDEX, 140).apply();
        }
        if (bolusRatio == 0) {
            MainActivity.sharedPreferences.edit().putInt(BOLUS_RATIO, 10).apply();
        }
    }

    public void setHints() {
        hyperglycemicEditText.setHint(String.valueOf(MainActivity.sharedPreferences.getInt(HYPERGLYCEMIC_INDEX, 0)));
        hypoglycemicEditText.setHint(String.valueOf(MainActivity.sharedPreferences.getInt(HYPOGLYCEMIC_INDEX, 0)));
        bolusRatio.setHint(String.valueOf(MainActivity.sharedPreferences.getInt(BOLUS_RATIO, 0)));
    }

    public void setupToolbar() {
        setSupportActionBar(treatmentToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.treatment);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
