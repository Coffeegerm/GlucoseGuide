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

import android.content.Context
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.MenuItem
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.ui.entry.NewEntryActivity
import io.github.coffeegerm.glucoseguide.ui.grade.GradeFragment
import io.github.coffeegerm.glucoseguide.ui.list.ListFragment
import io.github.coffeegerm.glucoseguide.ui.more.MoreFragment
import io.github.coffeegerm.glucoseguide.ui.statistics.StatisticsFragment

/**
 * Class dedicated to controlling what fragment should be presented base on user selection
 */

class NavigationOnItemSelectedListener(private val supportFragmentManager: FragmentManager, var context: Context) : BottomNavigationView.OnNavigationItemSelectedListener {
  
  private var coldLaunch = true
  
  private val listFragment: ListFragment by lazy { ListFragment() }
  private val statsFragment: StatisticsFragment by lazy { StatisticsFragment() }
  private val gradeFragment: GradeFragment by lazy { GradeFragment() }
  private val moreFragment: MoreFragment by lazy { MoreFragment() }
  
  init {
    if (coldLaunch) showFragment(listFragment)
    coldLaunch = false
  }
  
  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    var changesSelectedItem = true
    when (item.itemId) {
      
      R.id.nav_list -> {
        showFragment(listFragment)
        changesSelectedItem = true
      }
      
      R.id.nav_stats -> {
        showFragment(statsFragment)
        changesSelectedItem = true
      }
      
      R.id.nav_add_new_entry -> {
        context.startActivity(Intent(context, NewEntryActivity::class.java))
        changesSelectedItem = false
      }
      
      R.id.nav_grading -> {
        showFragment(gradeFragment)
        changesSelectedItem = true
      }
      
      R.id.nav_settings -> {
        showFragment(moreFragment)
        changesSelectedItem = true
      }
    }
    return changesSelectedItem
  }
  
  private fun showFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commitNowAllowingStateLoss()
}