package io.github.coffeegerm.materiallogbook.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.realm.Realm;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Activity for changing and showing chosen settings of Material Logbook.
 */

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    @BindView(R.id.btn_delete_all)
    TextView deleteAllEntries;
    @BindView(R.id.toggle_dark_mode)
    Switch toggleDarkMode;
    @BindView(R.id.setting_toolbar)
    Toolbar settingsToolbar;
    @BindView(R.id.treatment_section)
    LinearLayout treatmentSection;
    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.sharedPreferences.getBoolean(MainActivity.PREF_DARK_MODE, false)) {
            setTheme(R.style.AppTheme_Dark);
        }
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        realm = Realm.getDefaultInstance();
        setupToolbar();

        toggleDarkMode.setChecked(MainActivity.sharedPreferences.getBoolean(MainActivity.PREF_DARK_MODE, false));
        toggleDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.sharedPreferences.edit().putBoolean(MainActivity.PREF_DARK_MODE, isChecked).apply();
                recreate();
            }
        });

        treatmentSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsTreatmentActivity.class));
            }
        });

        deleteAllEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder;

                // Sets theme based on VERSION_CODE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    builder = new AlertDialog.Builder(SettingsActivity.this, android.R.style.Theme_Material_Dialog_NoActionBar);
                else builder = new AlertDialog.Builder(SettingsActivity.this);

                builder.setTitle("Delete all entries")
                        .setMessage("Are you sure you want to delete all entries?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.delete(EntryItem.class);
                                    }
                                });
                                Toast.makeText(SettingsActivity.this, R.string.all_deleted, Toast.LENGTH_SHORT).show();
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
        });
    }

    public void setupToolbar() {
        setSupportActionBar(settingsToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.settings);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
