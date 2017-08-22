package io.github.coffeegerm.materiallogbook.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.realm.Realm;

/**
 * Created by dyarz on 8/17/2017.
 * <p>
 * Activity created to allow user to edit their entries
 * already present in database
 */

public class EditEntryActivity extends AppCompatActivity {

    private static final String TAG = "EditEntryActivity";
    static String itemId = "itemId";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.itemId)
    TextView itemIdTest;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.save)
    Button save;
    Bundle itemIdBundle;
    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.sharedPreferences.getBoolean("pref_dark_mode", false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_edit_entry);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.edit_entry_toolbar);
        realm = Realm.getDefaultInstance();
        if (savedInstanceState == null) {
            itemIdBundle = getIntent().getExtras();
        } else {
            String itemIdString = (String) savedInstanceState.getSerializable(itemId);
            itemIdTest.setText(itemIdString);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
