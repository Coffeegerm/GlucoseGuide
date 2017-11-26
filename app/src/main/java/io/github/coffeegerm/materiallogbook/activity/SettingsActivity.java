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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.utils.AvenirRegularMedium;

import static io.github.coffeegerm.materiallogbook.utils.Constants.MILITARY_TIME;
import static io.github.coffeegerm.materiallogbook.utils.Constants.PAYPAL_URL;
import static io.github.coffeegerm.materiallogbook.utils.Constants.PREF_DARK_MODE;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Activity for changing and showing chosen settings of Material Logbook.
 */

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    @BindView(R.id.toggle_dark_mode)
    Switch toggleDarkMode;
    @BindView(R.id.military_time_switch)
    Switch militaryTimeSwitch;
    @BindView(R.id.setting_toolbar)
    Toolbar settingsToolbar;
    @BindView(R.id.treatment_section)
    LinearLayout treatmentSection;
    @BindView(R.id.data_section)
    LinearLayout dataSection;
    @BindView(R.id.paypal_webview)
    WebView paypalWebview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.sharedPreferences.getBoolean(PREF_DARK_MODE, false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tipjar:
                showTipjar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        setupToolbar();

        toggleDarkMode.setChecked(MainActivity.sharedPreferences.getBoolean(PREF_DARK_MODE, false));
        toggleDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.sharedPreferences.edit().putBoolean(PREF_DARK_MODE, isChecked).apply();
                recreate();
            }
        });

        militaryTimeSwitch.setChecked(MainActivity.sharedPreferences.getBoolean(MILITARY_TIME, false));
        militaryTimeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                MainActivity.sharedPreferences.edit().putBoolean(MILITARY_TIME, isChecked).apply();
            }
        });

        treatmentSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsTreatmentActivity.class));
            }
        });

        dataSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsDataActivity.class));
            }
        });
    }

    public void setupToolbar() {
        setSupportActionBar(settingsToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.settings);
        }
    }

    private void showTipjar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        @SuppressLint("InflateParams") final View tipjarView = getLayoutInflater().inflate(R.layout.tipjar_prompt, null);
        final AvenirRegularMedium no = tipjarView.findViewById(R.id.tipjar_no);
        final AvenirRegularMedium yes = tipjarView.findViewById(R.id.tipjar_yes);
        builder.setView(tipjarView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                setupWebView();
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        paypalWebview.getSettings().setLoadsImagesAutomatically(true);
        paypalWebview.getSettings().setJavaScriptEnabled(true);
        paypalWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        loadPaypal();
    }

    private void loadPaypal() {
        paypalWebview.loadUrl(PAYPAL_URL);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
