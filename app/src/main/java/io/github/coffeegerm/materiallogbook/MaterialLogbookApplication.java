package io.github.coffeegerm.materiallogbook;

import android.app.Application;

import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;

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
        // Build instabug for reporting bugs.
        new Instabug.Builder(this, INSTABUG_KEY)
                .setInvocationEvent(InstabugInvocationEvent.NONE)
                .setShouldShowIntroDialog(false)
                .build();
    }
}
