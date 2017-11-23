package io.github.coffeegerm.materiallogbook

import android.app.Application
import com.instabug.library.Instabug
import com.instabug.library.invocation.InstabugInvocationEvent
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

    override fun onCreate() {
        super.onCreate()
        buildInstabug()
        initRealm()

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
