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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;

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

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry_activity);
        ButterKnife.bind(this);

        // Set date and time to current date and time on initial create
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        newEntryDate.setText(month + "/" + day + "/" + year);
        newEntryTime.setText(hour + ":" + min);

        newEntryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    DatePickerDialog dialog = new DatePickerDialog(NewEntryActivity.this,
                            android.R.style.Theme_Material_Dialog_Alert,
                            mDateSetListener,
                            year, month, day);
                    dialog.show();
                } else {
                    DatePickerDialog dialog = new DatePickerDialog(NewEntryActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog,
                            mDateSetListener,
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
                        mTimeSetListener,
                        hour, min, false);
                timePickerDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.i(TAG, "onDateSet: Date set");
                newEntryDate.setText(month + "/" + dayOfMonth + "/" + year);
            }
        };

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.i(TAG, "onTimeSet:" + hourOfDay + ":" + minute);
                newEntryTime.setText(hourOfDay + ":" + minute);
            }
        };

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
                Log.i(TAG, "onClick: + " + newEntryDate.getText().toString());
                Log.i(TAG, "onClick: + " + newEntryTime.getText().toString());
                Log.i(TAG, "onClick: + " + newEntryBloodGlucose.getText().toString());
                Log.i(TAG, "onClick: + " + newEntryCarbohydrates.getText().toString());
                Log.i(TAG, "onClick: + " + newEntryInsulin.getText().toString());
            }
        });
    }


    // TODO Create Realm database

    // TODO saveBtn onClickListener saves data given to database

}
