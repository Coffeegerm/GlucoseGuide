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

import java.text.SimpleDateFormat
import java.util.*

/**
 * Class dedicated to formatting and returning
 * formatted dates for various classes to use
 */

class DateFormatter {
  
  private val standardDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
  private val twelveHourTimeFormat = SimpleDateFormat("hh:mm aa", Locale.US)
  private val twentyFourHourTimeFormat = SimpleDateFormat("HH:mm", Locale.US)
  
  fun formatDate(providedDate: Date?): String = standardDateFormat.format(providedDate)
  
  fun twelveHourFormat(providedDate: Date): String = twelveHourTimeFormat.format(providedDate)
  
  fun twentyFourHourFormat(providedDate: Date): String = twentyFourHourTimeFormat.format(providedDate)
}