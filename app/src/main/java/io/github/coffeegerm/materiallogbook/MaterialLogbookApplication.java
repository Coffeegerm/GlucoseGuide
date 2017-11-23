package io.github.coffeegerm.materiallogbook;

import android.app.Application;

import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

import static io.github.coffeegerm.materiallogbook.utils.Constants.INSTABUG_KEY;

/**
 * Created by dyarz on 8/18/2017.
 * <p>
 * Initializes Instabug.
 */

public class MaterialLogbookApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        buildInstabug();
        initRealm();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // do stuff
        }
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private void buildInstabug() {
        new Instabug.Builder(this, INSTABUG_KEY)
                .setInvocationEvent(InstabugInvocationEvent.NONE)
                .setShouldShowIntroDialog(false)
                .build();
    }
}
