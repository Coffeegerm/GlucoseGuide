package io.github.coffeegerm.materiallogbook.ui;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

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

    private static final String HYPERGLYCEMIC_INDEX = "hyperglycemicIndex";
    private static final String HYPOGLYCEMIC_INDEX = "hypoglycemicIndex";
    @BindView(R.id.btn_delete_all)
    Button deleteAllEntries;
    @BindView(R.id.hyperglycemic_edit_text)
    EditText hyperglycemicEditText;
    @BindView(R.id.hypoglycemic_edit_text)
    EditText hypoglycemicEditText;
    @BindView(R.id.toggle_dark_mode)
    Switch toggleDarkMode;
    @BindView(R.id.setting_toolbar)
    Toolbar settingsToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.sharedPreferences.getBoolean("pref_dark_mode", false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        final Realm realm = Realm.getDefaultInstance();
        setupToolbar();
        checkRangeStatus();
        setHints();

        toggleDarkMode.setChecked(MainActivity.sharedPreferences.getBoolean("pref_dark_mode", false));

        toggleDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.sharedPreferences.edit().putBoolean("pref_dark_mode", isChecked).apply();
                recreate();
            }
        });

        hypoglycemicEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "afterTextChanged: " + s.toString());
                if (!s.toString().equals(""))
                    MainActivity.sharedPreferences.edit()
                            .putInt(HYPOGLYCEMIC_INDEX, Integer.parseInt(s.toString())).apply();
            }
        });

        hyperglycemicEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "afterTextChanged: " + s.toString());
                if (!s.toString().equals("")) MainActivity.sharedPreferences.edit()
                        .putInt(HYPERGLYCEMIC_INDEX, Integer.parseInt(s.toString())).apply();
            }
        });

        deleteAllEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void checkRangeStatus() {
        int hyperglycemicIndex = MainActivity.sharedPreferences.getInt("hyperglycemicIndex", 0);
        int hypoglycemicIndex = MainActivity.sharedPreferences.getInt("hypoglycemicIndex", 0);
        if (hyperglycemicIndex == 0 && hypoglycemicIndex == 0) {
            MainActivity.sharedPreferences.edit().putInt("hypoglycemicIndex", 80).apply();
            MainActivity.sharedPreferences.edit().putInt("hyperglycemicIndex", 140).apply();
        }
    }

    public void setHints() {
        String hyperString = String.valueOf(MainActivity.sharedPreferences.getInt("hyperglycemicIndex", 0));
        hyperglycemicEditText.setHint(hyperString);

        String hypoString = String.valueOf(MainActivity.sharedPreferences.getInt("hypoglycemicIndex", 0));
        hypoglycemicEditText.setHint(hypoString);
    }

    public void setupToolbar() {
        setSupportActionBar(settingsToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
