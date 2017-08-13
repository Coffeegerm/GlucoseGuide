package io.github.coffeegerm.materiallogbook.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.realm.Realm;

import static io.github.coffeegerm.materiallogbook.utils.Utilities.checkTimeString;

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
    @BindView(R.id.new_entry_date_time_label)
    TextView dateTimeLabel;
    @BindView(R.id.new_entry_glucose_label)
    TextView glucoseLabel;
    @BindView(R.id.new_entry_carbs_label)
    TextView carbsLabel;
    @BindView(R.id.new_entry_insulin_label)
    TextView insulinLabel;

    private Realm realm;
    private Calendar calendarForDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: NewEntryActivity started");
        if (MainActivity.sharedPreferences.getBoolean("pref_dark_mode", false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_new_entry);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        setFonts();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.create_entry);

        final Calendar cal = Calendar.getInstance();
        // Calendar for saving entered Date and Time
        calendarForDb = Calendar.getInstance();

        // Set date and time to current date and time on initial create
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month++;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        newEntryDate.setText(dateFix(month, day, year));
        newEntryTime.setText(checkTimeString(hour, minute));

        newEntryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(NewEntryActivity.this,
                        (Build.VERSION.SDK_INT >= 21 ? android.R.style.Theme_Material_Dialog_Alert
                                : android.R.style.Theme_Holo_Light_Dialog),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month++;
                                newEntryDate.setText(dateFix(month, dayOfMonth, year));
                                month--;
                                calendarForDb.set(year, month, dayOfMonth);
                            }
                        }, cal.get(Calendar.YEAR), // year
                        cal.get(Calendar.MONTH), // month
                        cal.get(Calendar.DAY_OF_MONTH)); // day

                if (Build.VERSION.SDK_INT < 21)
                    if (dialog.getWindow() != null)
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                dialog.show();
            }
        });

        newEntryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                TimePickerDialog timePickerDialog = new TimePickerDialog(NewEntryActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                newEntryTime.setText(checkTimeString(hourOfDay, minute));
                                calendarForDb.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendarForDb.set(Calendar.MINUTE, minute);
                            }
                        },
                        cal.get(Calendar.HOUR_OF_DAY), // current hour
                        cal.get(Calendar.MINUTE), // current minute
                        false); //no 24 hour view
                timePickerDialog.show();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry();
            }
        });
    }

    private void startMainActivity() {
//        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.from_x_zero, R.anim.to_x_hundred);
        finish();
    }

    private void saveEntry() {
        // Checks to make sure there is a blood glucose given.
        if (newEntryBloodGlucose.getText().toString().equals(""))
            Snackbar.make(getWindow().getDecorView().getRootView(),
                    R.string.no_glucose_toast, Snackbar.LENGTH_SHORT).show();
        else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Save Entry to database
                    EntryItem entryItem = NewEntryActivity.this.realm.createObject(EntryItem.class);
                    // Creates Date object made from the DatePicker and TimePicker
                    Date date = calendarForDb.getTime();
                    entryItem.setDate(date);
                    entryItem.setBloodGlucose(Integer.parseInt(newEntryBloodGlucose.getText().toString()));
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
            startMainActivity();
        }
    }

    private void setFonts() {
        // Fonts used in Activity
        Typeface avenirRegular = Typeface.createFromAsset(getAssets(), "fonts/AvenirNext-Regular.otf");
        Typeface avenirDemiBold = Typeface.createFromAsset(getAssets(), "fonts/AvenirNext-DemiBold.otf");
        Typeface avenirMedium = Typeface.createFromAsset(getAssets(), "fonts/AvenirNext-Medium.otf");
        cancelBtn.setTypeface(avenirMedium);
        saveBtn.setTypeface(avenirMedium);
        dateTimeLabel.setTypeface(avenirDemiBold);
        carbsLabel.setTypeface(avenirDemiBold);
        glucoseLabel.setTypeface(avenirDemiBold);
        insulinLabel.setTypeface(avenirDemiBold);
        newEntryDate.setTypeface(avenirRegular);
        newEntryTime.setTypeface(avenirRegular);
        newEntryBloodGlucose.setTypeface(avenirRegular);
        newEntryCarbohydrates.setTypeface(avenirRegular);
        newEntryInsulin.setTypeface(avenirRegular);
    }

    // dateFix
    StringBuilder dateFix(int month, int day, int year) {
        return new StringBuilder().append(month).append("/").append(day).append("/")
                .append(year);
    }
}