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

package io.github.coffeegerm.materiallogbook.dagger

import dagger.Component
import io.github.coffeegerm.materiallogbook.activity.*
import io.github.coffeegerm.materiallogbook.list.ListAdapter
import io.github.coffeegerm.materiallogbook.list.ListFragment
import io.github.coffeegerm.materiallogbook.statistics.*
import io.github.coffeegerm.materiallogbook.utils.ConvertToCSV
import io.github.coffeegerm.materiallogbook.utils.Utilities
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(listFragment: ListFragment)
    fun inject(settingsActivity: SettingsActivity)
    fun inject(newEntryActivity: NewEntryActivity)
    fun inject(editEntryActivity: EditEntryActivity)
    fun inject(listAdapter: ListAdapter)
    fun inject(allStatisticsFragment: AllStatisticsFragment)
    fun inject(oneMonthStatisticsFragment: OneMonthStatisticsFragment)
    fun inject(sevenDayStatisticsFragment: SevenDayStatisticsFragment)
    fun inject(threeDayStatisticsFragment: ThreeDayStatisticsFragment)
    fun inject(threeMonthsStatisticsFragment: ThreeMonthsStatisticsFragment)
    fun inject(convertToCSV: ConvertToCSV)
    fun inject(settingsDataActivity: SettingsDataActivity)
    fun inject(settingsTreatmentActivity: SettingsTreatmentActivity)
    fun inject(utilities: Utilities)
}
