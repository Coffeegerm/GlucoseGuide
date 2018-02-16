/*
 * Copyright 2017 Coffee and Cream Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.coffeegerm.glucoseguide.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import io.github.coffeegerm.glucoseguide.R;
import io.github.coffeegerm.glucoseguide.data.DatabaseManager;
import io.github.coffeegerm.glucoseguide.data.model.EntryItem;
import io.realm.Realm;
import io.realm.RealmResults;

import static io.github.coffeegerm.glucoseguide.GlucoseGuide.syringe;
import static io.github.coffeegerm.glucoseguide.utils.Constants.DATE_FORMAT;
import static io.github.coffeegerm.glucoseguide.utils.Constants.TWELVE_HOUR_TIME_FORMAT;
import static io.github.coffeegerm.glucoseguide.utils.Constants.TWENTY_FOUR_HOUR_TIME_FORMAT;

/**
 * Created by david_yarz on 11/7/17.
 * <p>
 * Class created to convert glucose readings and additional data
 * into a CSV that is saved for later use.
 */

public final class ConvertToCSV {
  
  @Inject
  public SharedPreferences sharedPreferences;
  @Inject
  public DatabaseManager databaseManager;
  
  // Storage Permissions
  private static final int REQUEST_EXTERNAL_STORAGE = 1;
  private static String[] PERMISSIONS_STORAGE = {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
  };
  private final Context context;
  private final Realm realm;
  private final SimpleDateFormat dateFormat = DATE_FORMAT;
  private final SimpleDateFormat twelveHourTimeFormat = TWELVE_HOUR_TIME_FORMAT;
  private final SimpleDateFormat twentyFourHourTimeFormat = TWENTY_FOUR_HOUR_TIME_FORMAT;
  private File EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
  
  public ConvertToCSV(Context context) {
    this.context = context;
    realm = Realm.getDefaultInstance();
    syringe.inject(this);
    Activity activity = (Activity) context;
    checkStoragePermissions(activity);
  }
  
  public String createCSVFile() {
    try {
      File file = null;
      final File externalStorageDirectory = Environment.getExternalStorageDirectory();
      if (externalStorageDirectory.canWrite()) {
        file = new File(EXPORT_REALM_PATH, "material_logbook_export_" + System.currentTimeMillis() / 1000 + ".csv");
        
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        RealmResults<EntryItem> realmResults = databaseManager.getAllSortedAscending();
        ArrayList<EntryItem> entryItems = new ArrayList<>(realmResults);
        
        try {
          fileOutputStream = new FileOutputStream(file);
          outputStreamWriter = new OutputStreamWriter(fileOutputStream);
          
          // CSV Structure
          // Date | Time | Blood Glucose | Carbohydrates | Insulin
          final Resources resources = this.context.getResources();
          writeLine(outputStreamWriter,
                resources.getString(R.string.date),
                resources.getString(R.string.time),
                resources.getString(R.string.blood_glucose),
                resources.getString(R.string.blood_glucose_measurement),
                resources.getString(R.string.carbohydrates),
                resources.getString(R.string.insulin)
          );
          
          // Data from EntryItem
          // | Date | Time | Blood Glucose | Carbohydrates | Insulin
          for (int i = 0; i < entryItems.size(); i++) {
            EntryItem entry = entryItems.get(i);
            String entryTime;
            if (sharedPreferences.getBoolean(Constants.MILITARY_TIME, false)) {
              entryTime = twentyFourHourTimeFormat.format(entry.getDate());
            } else {
              entryTime = twelveHourTimeFormat.format(entry.getDate());
            }
            
            writeLine(outputStreamWriter,
                  dateFormat.format(entry.getDate()),
                  entryTime,
                  String.valueOf(entry.getBloodGlucose()),
                  "mg/dL",
                  String.valueOf(entry.getCarbohydrates()),
                  String.valueOf(entry.getInsulin())
            );
            
          }
          
          outputStreamWriter.flush();
        } catch (Exception ignored) {
        } finally {
          if (outputStreamWriter != null) outputStreamWriter.close();
          if (fileOutputStream != null) fileOutputStream.close();
          String toastMessage = "CSV created at " + EXPORT_REALM_PATH.toString();
          Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }
      }
      realm.close();
      return file == null ? null : file.getPath();
    } catch (Exception e) {
      realm.close();
      e.printStackTrace();
      return null;
    }
    
  }
  
  private void writeLine(OutputStreamWriter outputStreamWriter, String... values) throws IOException {
    for (int i = 0; i < values.length; i++) {
      outputStreamWriter.append(values[i]);
      outputStreamWriter.append(i == values.length - 1 ? '\n' : ',');
    }
  }
  
  private void checkStoragePermissions(Activity activity) {
    // Check if we have write permission
    int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    
    if (permission != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
            activity,
            PERMISSIONS_STORAGE,
            REQUEST_EXTERNAL_STORAGE
      );
    }
  }
  
}
