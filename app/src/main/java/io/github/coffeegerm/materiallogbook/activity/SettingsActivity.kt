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

package io.github.coffeegerm.materiallogbook.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.github.coffeegerm.materiallogbook.MaterialLogbookApplication
import io.github.coffeegerm.materiallogbook.R
import io.github.coffeegerm.materiallogbook.utils.Constants
import io.github.coffeegerm.materiallogbook.utils.Constants.PAYPAL_URL
import io.github.coffeegerm.materiallogbook.utils.Constants.PREF_DARK_MODE
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.tipjar_prompt.view.*
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MaterialLogbookApplication.syringe.inject(this)
        if (sharedPreferences.getBoolean(Constants.PREF_DARK_MODE, false)) setTheme(R.style.AppTheme_Dark)
        setContentView(R.layout.activity_settings)
        initView()
    }

    private fun initView() {
        setupToolbar()

        toggle_dark_mode.isChecked = sharedPreferences.getBoolean(Constants.PREF_DARK_MODE, false)
        toggle_dark_mode.setOnCheckedChangeListener({ _, isChecked -> sharedPreferences.edit().putBoolean(PREF_DARK_MODE, isChecked).apply() })

        military_time_switch.isChecked = sharedPreferences.getBoolean(Constants.MILITARY_TIME, false)
        military_time_switch.setOnCheckedChangeListener { _, isChecked -> sharedPreferences.edit().putBoolean(Constants.MILITARY_TIME, isChecked).apply() }

        treatment_section.setOnClickListener({ startActivity(Intent(applicationContext, SettingsTreatmentActivity::class.java)) })

        data_section.setOnClickListener { startActivity(Intent(applicationContext, SettingsDataActivity::class.java)) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tipjar -> showTipjar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(setting_toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setTitle(R.string.settings)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    @SuppressLint("InflateParams")
    private fun showTipjar() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val tipjarView = layoutInflater.inflate(R.layout.tipjar_prompt, null)
        alertDialogBuilder.setView(tipjarView)
        val dialog: AlertDialog = alertDialogBuilder.create()
        dialog.show()

        tipjarView.tipjar_no.setOnClickListener { dialog.dismiss() }
        tipjarView.tipjar_yes.setOnClickListener {
            dialog.dismiss()
            setupWebView()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        paypal_webview.settings.loadsImagesAutomatically = true
        paypal_webview.settings.javaScriptEnabled = true
        paypal_webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        loadPaypal()
    }

    private fun loadPaypal() {
        paypal_webview.loadUrl(PAYPAL_URL)
    }
}