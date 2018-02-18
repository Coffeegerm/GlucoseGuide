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

package io.github.coffeegerm.glucoseguide.ui.more.children

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.data.model.EntryItem
import io.github.coffeegerm.glucoseguide.utils.Constants
import io.github.coffeegerm.glucoseguide.utils.ConvertToCSV
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_settings_data.*
import javax.inject.Inject

/**
 * Created by david_yarz on 11/8/17.
 *
 *
 * Activity which handles data within the app.
 *
 *
 * Responsible for deleting and exporting data as well.
 */

class SettingsDataActivity : AppCompatActivity() {
  
  private val convertToCsv: ConvertToCSV by lazy { ConvertToCSV(this) }
  
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  
  private var realm: Realm? = null
  
  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GlucoseGuide.syringe.inject(this)
    if (sharedPreferences.getBoolean(Constants.PREF_DARK_MODE, false))
      setTheme(R.style.AppTheme_Dark)
    setContentView(R.layout.activity_settings_data)
    init()
  }
  
  private fun init() {
    setupToolbar()
    realm = Realm.getDefaultInstance()
    export_entries.setOnClickListener { exportEntries() }
    delete_all.setOnClickListener { deleteAllEntries() }
  }
  
  private fun exportEntries() {
    convertToCsv.createCSVFile()
  }
  
  private fun deleteAllEntries() {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this@SettingsDataActivity)
    
    builder.setTitle("Delete all entries")
          .setMessage("Are you sure you want to delete all entries?")
          .setPositiveButton(android.R.string.yes) { _, _ ->
            // continue with delete
            try {
              realm?.executeTransaction { realm -> realm.delete(EntryItem::class.java) }
            } finally {
              realm?.close()
            }
            Toast.makeText(this@SettingsDataActivity, R.string.all_deleted, Toast.LENGTH_SHORT).show()
          }
          .setNegativeButton(android.R.string.no) { dialog, _ ->
            // do nothing
            dialog.dismiss()
          }
          .setIcon(R.drawable.ic_trash)
          .show()
  }
  
  private fun setupToolbar() {
    setSupportActionBar(data_toolbar)
    if (supportActionBar != null) {
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
      supportActionBar?.setDisplayShowHomeEnabled(true)
      supportActionBar?.setTitle(R.string.data)
    }
  }
  
  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return super.onSupportNavigateUp()
  }
}
