package io.github.coffeegerm.materiallogbook.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;

/**
 * Created by dyarz on 8/17/2017.
 * <p>
 * Activity created to allow user to edit their entries
 * already present in database
 */

public class EditEntryActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        // TODO: 8/17/2017 Find EntryItem object and present it to user
        // TODO: 8/17/2017 take changed fields and save to database
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
