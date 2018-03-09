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

package io.github.coffeegerm.glucoseguide.utils

import android.content.SharedPreferences
import javax.inject.Inject

/**
 * Class made to pull out the logic in classes handling SharedPreferences
 */

class SharedPreferenceManager @Inject constructor(private var sharedPreferences: SharedPreferences) {
  
  fun getBoolean(key: String): Boolean = sharedPreferences.getBoolean(key, false)
  
  fun getInt(key: String): Int = sharedPreferences.getInt(key, 0)
  
  fun putBoolean(key: String, value: Boolean) = sharedPreferences.edit().putBoolean(key, value).apply()
  
  fun putInt(key: String, value: Int) = sharedPreferences.edit().putInt(key, value).apply()
}