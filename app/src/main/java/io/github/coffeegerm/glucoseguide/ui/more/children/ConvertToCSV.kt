/*
 * Copyright 2018 Coffee and Cream Studios
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

package io.github.coffeegerm.glucoseguide.ui.more.children

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.data.DatabaseManager
import io.github.coffeegerm.glucoseguide.utils.Constants
import io.github.coffeegerm.glucoseguide.utils.DateFormatter
import io.github.coffeegerm.glucoseguide.utils.SharedPreferencesManager
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import javax.inject.Inject

/**
 * Class dedicated to converting all entries to a CSV for printing
 * and viewing with Endocrinologist
 */

class ConvertToCSV(private var context: Context) {
  
  @Inject
  lateinit var sharedPreferencesManager: SharedPreferencesManager
  @Inject
  lateinit var databaseManager: DatabaseManager
  @Inject
  lateinit var resources: Resources
  @Inject
  lateinit var dateFormatter: DateFormatter
  
  init {
    GlucoseGuide.syringe.inject(this)
    checkStoragePermissions(context as Activity)
  }
  
  fun createCSVFile(): String? {
    try {
      val exportPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
      var file: File? = null
      val externalStorageDirectory = Environment.getExternalStorageDirectory()
      if (externalStorageDirectory.canWrite()) {
        file = File(exportPath, "material_logbook_export_" + System.currentTimeMillis() / 1000 + ".csv")
        
        val fileOutputStream = FileOutputStream(file)
        val outputStreamWriter = OutputStreamWriter(fileOutputStream)
        val realmResults = databaseManager.getAllSortedDescending()
        val entryItems = ArrayList(realmResults)
        
        try {
          
          // CSV Structure
          // Date | Time | Blood Glucose | Carbohydrates | Insulin
          writeLine(outputStreamWriter,
                resources.getString(R.string.date),
                resources.getString(R.string.time),
                resources.getString(R.string.blood_glucose),
                resources.getString(R.string.carbohydrates),
                resources.getString(R.string.insulin)
          )
          
          // Data from EntryItem
          // | Date | Time | Blood Glucose | Carbohydrates | Insulin
          for (currentEntry in entryItems.indices) {
            val entry = entryItems[currentEntry]
            val date = entry.date
            val entryTime: String = if (sharedPreferencesManager.getBoolean(Constants.MILITARY_TIME)) {
              dateFormatter.twentyFourHourFormat(date)
            } else {
              dateFormatter.twelveHourFormat(date)
            }
            
            writeLine(outputStreamWriter,
                  dateFormatter.formatDate(date),
                  entryTime,
                  entry.bloodGlucose.toString() + " mg/dL",
                  entry.carbohydrates.toString(),
                  entry.insulin.toString()
            )
            
          }
          
          outputStreamWriter.flush()
        } catch (ignored: Exception) {
        } finally {
          outputStreamWriter.close()
          fileOutputStream.close()
          val toastMessage = "CSV created at " + exportPath.toString()
          Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
      }
      return if (file == null) null else file.path
    } catch (e: Exception) {
      Timber.i("Unable to process")
      e.printStackTrace()
      return null
    }
    
  }
  
  @Throws(IOException::class)
  private fun writeLine(outputStreamWriter: OutputStreamWriter, vararg values: String) {
    for (i in values.indices) {
      outputStreamWriter.append(values[i])
      outputStreamWriter.append(if (i == values.size - 1) '\n' else ',')
    }
  }
  
  private fun checkStoragePermissions(activity: Activity) {
    // Check if we have write permission
    val permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    if (permission != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
      )
    }
  }
}