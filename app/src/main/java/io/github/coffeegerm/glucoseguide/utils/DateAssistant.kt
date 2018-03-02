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

import java.util.*

/**
 * Class created for assistance in handling the Calendar usage in the application
 */

class DateAssistant {
  
  val today: Calendar = Calendar.getInstance()
  
  fun getTodayOfMonth(): Int = today.get(Calendar.DAY_OF_MONTH)
  
  fun getYesterdayDayOfMonth(): Int {
    today.add(Calendar.DATE, -1)
    return today.get(Calendar.DAY_OF_MONTH)
  }
  
  fun getThreeDaysAgoDate(): Date {
    today.add(Calendar.DATE, -3)
    return today.time
  }
  
  fun getSevenDaysAgoDate(): Date {
    today.add(Calendar.DATE, -7)
    return today.time
  }
  
  fun getOneMonthAgo(): Date {
    today.add(Calendar.DATE, -30)
    return today.time
  }
  
  fun getThreeMonthsAgoDate(): Date {
    today.add(Calendar.DATE, -90)
    return today.time
  }
  
  fun getSpecificCalendarDayOfMonth(calendar: Calendar) = calendar.get(Calendar.DAY_OF_MONTH)
}