package io.github.coffeegerm.materiallogbook;

import android.app.Application;

import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;

/**
 * Created by dyarz on 8/18/2017.
 * <p>
 * Initializes Instabug.
 */

public class MaterialLogbookApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new Instabug.Builder(this, "0d278e5f5680024d7c487884873c0509")
                .setInvocationEvent(InstabugInvocationEvent.NONE)
                .setShouldShowIntroDialog(false)
                .build();
    }
}
