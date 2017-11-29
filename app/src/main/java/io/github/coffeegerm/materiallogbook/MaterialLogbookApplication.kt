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

package io.github.coffeegerm.materiallogbook

import android.app.Application
import com.instabug.library.Instabug
import com.instabug.library.invocation.InstabugInvocationEvent
import io.github.coffeegerm.materiallogbook.dagger.AppComponent
import io.github.coffeegerm.materiallogbook.dagger.AppModule
import io.github.coffeegerm.materiallogbook.dagger.DaggerAppComponent
import io.github.coffeegerm.materiallogbook.utils.Constants.INSTABUG_KEY
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber

/**
 * Created by dyarz on 8/18/2017.
 *
 * Application level class used to initialize
 * Instabug and Realm as well as plant a Tree
 *
 */

class MaterialLogbookApplication : Application() {

    companion object {
        lateinit var syringe: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        buildInstabug()
        initRealm()
        syringe = DaggerAppComponent.builder().appModule(AppModule(this)).build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // do stuff
        }
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }

    private fun buildInstabug() {
        Instabug.Builder(this, INSTABUG_KEY)
                .setInvocationEvent(InstabugInvocationEvent.NONE)
                .setShouldShowIntroDialog(false)
                .build()
    }
}
