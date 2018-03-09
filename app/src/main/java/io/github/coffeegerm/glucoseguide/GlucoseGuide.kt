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

package io.github.coffeegerm.glucoseguide

import android.app.Application
import io.github.coffeegerm.glucoseguide.BuildConfig.DEBUG
import io.github.coffeegerm.glucoseguide.dagger.AppComponent
import io.github.coffeegerm.glucoseguide.dagger.AppModule
import io.github.coffeegerm.glucoseguide.dagger.DaggerAppComponent
import io.github.coffeegerm.glucoseguide.dagger.DataModule
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber.DebugTree
import timber.log.Timber.plant

class GlucoseGuide : Application() {
  
  companion object {
    lateinit var syringe: AppComponent
  }
  
  override fun onCreate() {
    super.onCreate()
    initRealm()
    syringe = DaggerAppComponent
          .builder()
          .appModule(AppModule(this))
          .dataModule(DataModule())
          .build()
    
    if (DEBUG) plant(DebugTree())
  }
  
  private fun initRealm() {
    Realm.init(this)
    val config = RealmConfiguration.Builder()
          .schemaVersion(4)
          .deleteRealmIfMigrationNeeded()
          .build()
    Realm.setDefaultConfiguration(config)
  }
}
