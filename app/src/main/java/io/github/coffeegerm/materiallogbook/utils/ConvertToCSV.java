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

package io.github.coffeegerm.materiallogbook.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import io.github.coffeegerm.materiallogbook.MaterialLogbookApplication;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static io.github.coffeegerm.materiallogbook.utils.Constants.DATE_FORMAT;
import static io.github.coffeegerm.materiallogbook.utils.Constants.TWELVE_HOUR_TIME_FORMAT;
import static io.github.coffeegerm.materiallogbook.utils.Constants.TWENTY_FOUR_HOUR_TIME_FORMAT;

/**
 * Created by david_yarz on 11/7/17.
 * <p>
 * Class created to convert glucose readings and additional data
 * into a CSV that is saved for later use.
 */

public final class ConvertToCSV {
  
  private final Context context;
  private final Realm realm;
  private final SimpleDateFormat dateFormat = DATE_FORMAT;
  private final SimpleDateFormat twelveHourTimeFormat = TWELVE_HOUR_TIME_FORMAT;
  private final SimpleDateFormat twentyFourHourTimeFormat = TWENTY_FOUR_HOUR_TIME_FORMAT;
  @Inject
  public SharedPreferences sharedPreferences;
  
  public ConvertToCSV(Context context) {
    this.context = context;
    realm = Realm.getDefaultInstance();
    MaterialLogbookApplication.syringe.inject(this);
  }
  
  public String createCSVFile() {
    try {
      File file = null;
      final File sd = Environment.getExternalStorageDirectory();
      if (sd.canWrite()) {
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/material_logbook", "material_logbook_export_" + System.currentTimeMillis() / 1000 + ".csv");
        
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        RealmResults<EntryItem> realmResults = realm.where(EntryItem.class).findAllSorted("date", Sort.ASCENDING);
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
  
  private void writeLine(OutputStreamWriter osw, String... values) throws IOException {
    for (int i = 0; i < values.length; i++) {
      osw.append(values[i]);
      osw.append(i == values.length - 1 ? '\n' : ',');
    }
  }
  
}
