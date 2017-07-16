package io.github.coffeegerm.materiallogbook.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.github.coffeegerm.materiallogbook.utils.Utilities;
import io.realm.Realm;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Activity for a new Entry into the Database
 */

public class NewEntryActivity extends AppCompatActivity {

    private static final String TAG = "NewEntryActivity";

    @BindView(R.id.cancelBtn)
    Button cancelBtn;

    @BindView(R.id.saveBtn)
    Button saveBtn;

    @BindView(R.id.new_entry_date)
    EditText newEntryDate;

    @BindView(R.id.new_entry_time)
    EditText newEntryTime;

    @BindView(R.id.new_entry_blood_glucose_level)
    EditText newEntryBloodGlucose;

    @BindView(R.id.new_entry_carbohydrates_amount)
    EditText newEntryCarbohydrates;

    @BindView(R.id.new_entry_insulin_units)
    EditText newEntryInsulin;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: NewEntryActivity started");
        setContentView(R.layout.activity_new_entry);
        ButterKnife.bind(this);
        mRealm = Realm.getDefaultInstance();

        final Calendar cal = Calendar.getInstance();
        // Calendar for saving entered Date and Time
        final Calendar calendarForDb = Calendar.getInstance();

        // Set date and time to current date and time on initial create
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month++;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        newEntryDate.setText(month + "/" + day + "/" + year);
        newEntryTime.setText(Utilities.checkTimeString(hour, minute));

        newEntryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    DatePickerDialog dialog = new DatePickerDialog(NewEntryActivity.this,
                            android.R.style.Theme_Material_Dialog_Alert,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    month++;
                                    newEntryDate.setText(month + "/" + dayOfMonth + "/" + year);
                                    month--;
                                    calendarForDb.set(year, month, dayOfMonth);
                                }
                            },
                            year, month, day);
                    dialog.show();
                } else {
                    DatePickerDialog dialog = new DatePickerDialog(NewEntryActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    month++;
                                    newEntryDate.setText(month + "/" + dayOfMonth + "/" + year);
                                    month--;
                                    calendarForDb.set(year, month, dayOfMonth);
                                }
                            },
                            year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });

        newEntryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(NewEntryActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                newEntryTime.setText(Utilities.checkTimeString(hourOfDay, minute));
                                calendarForDb.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendarForDb.set(Calendar.MINUTE, minute);
                            }
                        },
                        hour, min, false);
                timePickerDialog.show();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.from_x_zero, R.anim.to_x_hundred);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        // Save Entry to database
                        EntryItem entryItem = mRealm.createObject(EntryItem.class);
                        // Creates Date object made from the DatePicker and TimePicker
                        Date date = calendarForDb.getTime();
                        entryItem.setDate(date);
                        // Prevention of NullPointerException
                        if (!newEntryBloodGlucose.getText().toString().equals("")) {
                            entryItem.setGlucose(Integer.parseInt(newEntryBloodGlucose.getText().toString()));
                        }
                        // Prevention of NullPointerException
                        if (!newEntryCarbohydrates.getText().toString().equals("")) {
                            entryItem.setCarbohydrates(Integer.parseInt(newEntryCarbohydrates.getText().toString()));
                        }
                        // Prevention of NullPointerException
                        if (!newEntryInsulin.getText().toString().equals("")) {
                            entryItem.setInsulin(Double.parseDouble(newEntryInsulin.getText().toString()));
                        }
                    }
                });

                // After save returns to MainActivity ListFragment
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.from_x_zero, R.anim.to_x_hundred);
            }
        });
    }
}
