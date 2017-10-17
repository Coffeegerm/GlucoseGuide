package io.github.coffeegerm.materiallogbook.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.realm.Realm;
import io.realm.RealmResults;

import static io.github.coffeegerm.materiallogbook.utils.Constants.PREF_DARK_MODE;
import static io.github.coffeegerm.materiallogbook.utils.Utilities.checkTimeString;

/**
 * Created by dyarz on 8/17/2017.
 * <p>
 * Activity created to allow user to edit their entries
 * already present in database
 */

public class EditEntryActivity extends AppCompatActivity {

    public static final String ITEM_ID = "itemId";
    private static final String TAG = "EditEntryActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.update)
    Button update;
    @BindView(R.id.new_entry_date)
    EditText date;
    @BindView(R.id.new_entry_time)
    EditText time;
    @BindView(R.id.new_entry_blood_glucose_level)
    EditText bloodGlucose;
    @BindView(R.id.new_entry_carbohydrates_amount)
    EditText carbohydrates;
    @BindView(R.id.new_entry_insulin_units)
    EditText insulin;
    @BindView(R.id.delete_entry)
    TextView deleteEntry;
    @BindView(R.id.breakfast_status)
    ImageButton breakfast;
    @BindView(R.id.lunch_status)
    ImageButton lunch;
    @BindView(R.id.dinner_status)
    ImageButton dinner;
    @BindView(R.id.sick_status)
    ImageButton sick;
    @BindView(R.id.exercise_status)
    ImageButton exercise;
    @BindView(R.id.sweets_status)
    ImageButton sweets;
    Handler handler;
    /* Original values from item. Compare to possible updated values to find what needs to be updated in database */
    Date originalDate;
    int originalStatus;
    int originalBloodGlucose;
    int originalCarbohydrates;
    double originalInsulin;
    /* items to be used to altered to show that the item has been updated */
    int updatedStatus;
    Date updatedDate;
    int updatedBloodGlucose;
    int updatedCarbohydrates;
    double updatedInsulin;
    private Calendar originalCalendar;
    private Calendar updatedCalendar;
    private Realm realm;
    private String itemIdString;
    private EntryItem item;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa", Locale.US);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.sharedPreferences.getBoolean(PREF_DARK_MODE, false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_edit_entry);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        handler = new Handler();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.edit_entry_toolbar);
        realm = Realm.getDefaultInstance();
        itemIdString = getIntent().getStringExtra(ITEM_ID);
        item = getItem();
        updatedCalendar = Calendar.getInstance();
        getOriginalValues(); // must call before hints are set
        setHints();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(EditEntryActivity.this,
                        (Build.VERSION.SDK_INT >= 21 ? android.R.style.Theme_Material_Dialog_Alert
                                : android.R.style.Theme_Holo_Light_Dialog),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month++;
                                date.setText(dateFix(month, dayOfMonth, year));
                                month--;
                                updatedCalendar.set(year, month, dayOfMonth);
                            }
                        }, originalCalendar.get(Calendar.YEAR), // year
                        originalCalendar.get(Calendar.MONTH), // month
                        originalCalendar.get(Calendar.DAY_OF_MONTH)); // day

                if (Build.VERSION.SDK_INT < 21)
                    if (dialog.getWindow() != null)
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                dialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditEntryActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                time.setText(checkTimeString(hourOfDay, minute));
                                updatedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                updatedCalendar.set(Calendar.MINUTE, minute);
                            }
                        },
                        originalCalendar.get(Calendar.HOUR_OF_DAY), // current hour
                        originalCalendar.get(Calendar.MINUTE), // current minute
                        false); //no 24 hour view
                timePickerDialog.show();
            }
        });

        bloodGlucose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updatedBloodGlucose = Integer.parseInt(editable.toString());
                Log.i(TAG, "afterTextChanged bloodGlucose: " + editable);
            }
        });

        carbohydrates.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updatedCarbohydrates = Integer.parseInt(editable.toString());
                Log.i(TAG, "afterTextChanged carbohydrates: " + editable);
            }
        });

        insulin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updatedInsulin = Double.parseDouble(editable.toString());
                Log.i(TAG, "afterTextChanged insulin: " + editable);
            }
        });

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedStatus = 1;
                setStatus(updatedStatus);
                final Toast toast = Toast.makeText(getApplicationContext(), "Breakfast", Toast.LENGTH_SHORT);
                toast.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 700);
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedStatus = 2;
                Log.i(TAG, "status: " + updatedStatus);
                setStatus(updatedStatus);
                final Toast toast = Toast.makeText(getApplicationContext(), "Lunch", Toast.LENGTH_SHORT);
                toast.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 700);
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedStatus = 3;
                Log.i(TAG, "status: " + updatedStatus);
                setStatus(updatedStatus);
                final Toast toast = Toast.makeText(getApplicationContext(), "Dinner", Toast.LENGTH_SHORT);
                toast.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 700);
            }
        });

        sick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedStatus = 4;
                Log.i(TAG, "status: " + updatedStatus);
                setStatus(updatedStatus);
                final Toast toast = Toast.makeText(getApplicationContext(), "Sick", Toast.LENGTH_SHORT);
                toast.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 700);
            }
        });

        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedStatus = 5;
                Log.i(TAG, "status: " + updatedStatus);
                setStatus(updatedStatus);
                final Toast toast = Toast.makeText(getApplicationContext(), "Exercise", Toast.LENGTH_SHORT);
                toast.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 700);
            }
        });

        sweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedStatus = 6;
                Log.i(TAG, "status: " + updatedStatus);
                setStatus(updatedStatus);
                final Toast toast = Toast.makeText(getApplicationContext(), "Sweets", Toast.LENGTH_SHORT);
                toast.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 700);
            }
        });

        deleteEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder;

                // Sets theme based on VERSION_CODE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    builder = new AlertDialog.Builder(EditEntryActivity.this, android.R.style.Theme_Material_Dialog_NoActionBar);
                else builder = new AlertDialog.Builder(EditEntryActivity.this);

                builder.setTitle(R.string.delete_single_entry)
                        .setMessage(R.string.delete_single_entry_message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        item.deleteFromRealm();
                                        Toast.makeText(EditEntryActivity.this, R.string.entry_deleted, Toast.LENGTH_SHORT).show();
                                        finish();
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEntry();
            }
        });
    }

    private EntryItem getItem() {
        RealmResults<EntryItem> itemRealmResults = realm.where(EntryItem.class).equalTo("id", itemIdString).findAll();
        return itemRealmResults.get(0);
    }

    private void getOriginalValues() {
        originalStatus = item.getStatus();
        originalCalendar = Calendar.getInstance();
        originalCalendar.setTime(item.getDate());
        originalDate = originalCalendar.getTime();
        originalBloodGlucose = item.getBloodGlucose();
        originalCarbohydrates = item.getCarbohydrates();
        originalInsulin = item.getInsulin();
    }

    private void setHints() {
        String formattedDate = dateFormat.format(originalDate);
        String formattedTime = timeFormat.format(originalDate);
        date.setHint(formattedDate);
        time.setHint(formattedTime);
        bloodGlucose.setHint(String.valueOf(originalBloodGlucose));
        if (originalCarbohydrates != 0)
            carbohydrates.setHint(String.valueOf(originalCarbohydrates));
        else carbohydrates.setHint(R.string.dash);
        if (originalInsulin != 0.0)
            insulin.setHint(String.valueOf(originalInsulin));
        else insulin.setHint(R.string.dash);
        setStatus(originalStatus);
    }

    private void setStatus(int status) {
        Drawable status_checked = getResources().getDrawable(R.drawable.status_checked);
        Drawable status_unchecked = getResources().getDrawable(R.drawable.status_unchecked);
        switch (status) {
            case 1:
                breakfast.setBackground(status_checked);
                lunch.setBackground(status_unchecked);
                dinner.setBackground(status_unchecked);
                sick.setBackground(status_unchecked);
                exercise.setBackground(status_unchecked);
                sweets.setBackground(status_unchecked);
                break;
            case 2:
                breakfast.setBackground(status_unchecked);
                lunch.setBackground(status_checked);
                dinner.setBackground(status_unchecked);
                sick.setBackground(status_unchecked);
                exercise.setBackground(status_unchecked);
                sweets.setBackground(status_unchecked);
                break;
            case 3:
                breakfast.setBackground(status_unchecked);
                lunch.setBackground(status_unchecked);
                dinner.setBackground(status_checked);
                sick.setBackground(status_unchecked);
                exercise.setBackground(status_unchecked);
                sweets.setBackground(status_unchecked);
                break;
            case 4:
                breakfast.setBackground(status_unchecked);
                lunch.setBackground(status_unchecked);
                dinner.setBackground(status_unchecked);
                sick.setBackground(status_checked);
                exercise.setBackground(status_unchecked);
                sweets.setBackground(status_unchecked);
                break;
            case 5:
                breakfast.setBackground(status_unchecked);
                lunch.setBackground(status_unchecked);
                dinner.setBackground(status_unchecked);
                sick.setBackground(status_unchecked);
                exercise.setBackground(status_checked);
                sweets.setBackground(status_unchecked);
                break;
            case 6:
                breakfast.setBackground(status_unchecked);
                lunch.setBackground(status_unchecked);
                dinner.setBackground(status_unchecked);
                sick.setBackground(status_unchecked);
                exercise.setBackground(status_unchecked);
                sweets.setBackground(status_checked);
                break;
            default:
                break;
        }
    }

    StringBuilder dateFix(int month, int day, int year) {
        return new StringBuilder().append(month).append("/").append(day).append("/").append(year);
    }

    private void updateEntry() {
        Date dateToSave = new Date();
        realm.beginTransaction();
        item.deleteFromRealm();
        EntryItem itemToSave = new EntryItem();
        itemToSave.setStatus(updatedStatus);
        if (updatedCalendar.getTimeInMillis() != originalCalendar.getTimeInMillis()) {
            dateToSave.setTime(updatedCalendar.getTimeInMillis());
            itemToSave.setDate(dateToSave);
        } else {
            dateToSave.setTime(originalCalendar.getTimeInMillis());
            itemToSave.setDate(dateToSave);
        }
        if (!bloodGlucose.getText().toString().equals("")) {
            itemToSave.setBloodGlucose(Integer.parseInt(bloodGlucose.getText().toString()));
        }
        if (!carbohydrates.getText().toString().equals("")) {
            itemToSave.setCarbohydrates(Integer.parseInt(carbohydrates.getText().toString()));
        }
        if (!insulin.getText().toString().equals("")) {
            itemToSave.setInsulin(Double.parseDouble(insulin.getText().toString()));
        }
        realm.copyToRealm(itemToSave);
        realm.commitTransaction();
        finish();
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}