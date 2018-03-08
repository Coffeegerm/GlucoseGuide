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

package io.github.coffeegerm.glucoseguide.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.coffeegerm.glucoseguide.GlucoseGuide.Companion.syringe
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * TODO: Add class comment header
 */
class MainActivity : AppCompatActivity() {
  
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    syringe.inject(this)
    if (sharedPreferences.getBoolean(Constants.PREF_DARK_MODE, false)) setTheme(R.style.AppTheme_Dark)
    setContentView(R.layout.activity_main)
    bottom_navigation.onNavigationItemSelectedListener = NavigationOnItemSelectedListener(supportFragmentManager, context = applicationContext)
    bottom_navigation.enableShiftingMode(false)
    bottom_navigation.enableItemShiftingMode(false)
  }
}