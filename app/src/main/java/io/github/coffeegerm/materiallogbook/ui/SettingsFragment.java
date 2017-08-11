package io.github.coffeegerm.materiallogbook.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Fragment for settings to change the way the app looks and others minor things
 */

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    private SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;

    @BindView(R.id.btn_delete_all)
    Button deleteAllEntries;
    @BindView(R.id.hyperglycemic_edit_text)
    EditText hyperglycemicEditText;
    @BindView(R.id.hypoglycemic_edit_text)
    EditText hypoglycemicEditText;
    @BindView(R.id.toggle_dark_mode)
    Switch toggleDarkMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View settingsView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, settingsView);
        initView();
        return settingsView;
    }

    public void initView() {
        final Realm realm = Realm.getDefaultInstance();
        settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        settingsEditor = settings.edit();
        settingsEditor = settings.edit();
        checkRangeStatus();
        setHints();
        setDarkModeToggle(settings.getBoolean("darkModeStatus", false));

        toggleDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settingsEditor.putBoolean("darkModeStatus", true);
                    settingsEditor.apply();

                } else {
                    settingsEditor.putBoolean("darkModeStatus", true);
                    settingsEditor.apply();
                }

            }
        });

        hypoglycemicEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "afterTextChanged: " + s.toString());
                if (!s.toString().equals("")) {
                    settingsEditor.putInt("hypoglycemicIndex", Integer.parseInt(s.toString()));
                    settingsEditor.apply();
                }
            }
        });

        hyperglycemicEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing.
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "afterTextChanged: " + s.toString());
                if (!s.toString().equals("")) {
                    settingsEditor.putInt("hyperglycemicIndex", Integer.parseInt(s.toString()));
                    settingsEditor.apply();
                }
            }
        });

        deleteAllEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder;

                // Sets theme based on VERSION_CODE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }

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

    public void setDarkModeToggle(boolean status) {
        if (status) {
            toggleDarkMode.setChecked(true);
        } else {
            toggleDarkMode.setChecked(false);
        }
    }

    public void checkRangeStatus() {
        int hyperglycemicIndex = settings.getInt("hyperglycemicIndex", 0);
        int hypoglycemicIndex = settings.getInt("hypoglycemicIndex", 0);
        if (hyperglycemicIndex == 0 && hypoglycemicIndex == 0) {
            settingsEditor.putInt("hypoglycemicIndex", 80);
            settingsEditor.putInt("hyperglycemicIndex", 140);
            settingsEditor.apply();
        }
    }

    public void setHints() {
        String hyperString = String.valueOf(settings.getInt("hyperglycemicIndex", 0));
        hyperglycemicEditText.setHint(hyperString);

        String hypoString = String.valueOf(settings.getInt("hypoglycemicIndex", 0));
        hypoglycemicEditText.setHint(hypoString);
    }
}
