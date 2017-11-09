package io.github.coffeegerm.materiallogbook.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.github.coffeegerm.materiallogbook.utils.Constants;
import io.realm.Realm;

/**
 * Created by david_yarz on 11/8/17.
 * <p>
 * Activity which handles data within the app.
 * <p>
 * Responsible for deleting and exporting data as well.
 */

public class SettingsDataActivity extends AppCompatActivity {

    private static final String TAG = "SettingsDataActivity";
    private Realm realm;

    @BindView(R.id.data_toolbar)
    Toolbar toolbar;
    @BindView(R.id.delete_all)
    TextView deleteAllEntries;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.sharedPreferences.getBoolean(Constants.PREF_DARK_MODE, false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_settings_data);
        Log.i(TAG, "View set");
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        setupToolbar();
        realm = Realm.getDefaultInstance();
        deleteAllEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllEntries();
            }
        });
    }

    public void deleteAllEntries() {
        final AlertDialog.Builder builder;

        // Sets theme based on VERSION_CODE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new AlertDialog.Builder(SettingsDataActivity.this, android.R.style.Theme_Material_Dialog_NoActionBar);
        else builder = new AlertDialog.Builder(SettingsDataActivity.this);

        builder.setTitle("Delete all entries")
                .setMessage("Are you sure you want to delete all entries?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.delete(EntryItem.class);
                                Log.i(TAG, "All Entry items deleted");
                            }
                        });
                        Toast.makeText(SettingsDataActivity.this, R.string.all_deleted, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_trash)
                .show();
    }

    public void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.data);
            Log.i(TAG, "Toolbar set");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
