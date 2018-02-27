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
import android.content.SharedPreferences
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
import io.github.coffeegerm.glucoseguide.utils.Constants.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import javax.inject.Inject

/**
 * TODO: Add class comment header
 */

class ConvertToCSV(private var context: Context) {
  
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  @Inject
  lateinit var databaseManager: DatabaseManager
  @Inject
  lateinit var resources: Resources
  
  // Storage Permissions
  private val requestExternalStorageCode = 1
  private val exportPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
  
  private val dateFormat = DATE_FORMAT
  private val twelveHourTimeFormat = TWELVE_HOUR_TIME_FORMAT
  private val twentyFourHourTimeFormat = TWENTY_FOUR_HOUR_TIME_FORMAT
  
  init {
    GlucoseGuide.syringe.inject(this)
    val activity = context as Activity
    checkStoragePermissions(activity)
  }
  
  fun createCSVFile(): String? {
    try {
      var file: File? = null
      val externalStorageDirectory = Environment.getExternalStorageDirectory()
      if (externalStorageDirectory.canWrite()) {
        file = File(exportPath, "material_logbook_export_" + System.currentTimeMillis() / 1000 + ".csv")
        
        var fileOutputStream: FileOutputStream? = null
        var outputStreamWriter: OutputStreamWriter? = null
        val realmResults = databaseManager.getAllSortedDescending()
        val entryItems = ArrayList(realmResults)
        
        try {
          fileOutputStream = FileOutputStream(file)
          outputStreamWriter = OutputStreamWriter(fileOutputStream)
          
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
            val entryTime: String = if (sharedPreferences.getBoolean(Constants.MILITARY_TIME, false)) {
              twentyFourHourTimeFormat.format(entry.date)
            } else {
              twelveHourTimeFormat.format(entry.date)
            }
            
            writeLine(outputStreamWriter,
                  dateFormat.format(entry.date),
                  entryTime,
                  entry.bloodGlucose.toString() + " mg/dL",
                  entry.carbohydrates.toString(),
                  entry.insulin.toString()
            )
            
          }
          
          outputStreamWriter.flush()
        } catch (ignored: Exception) {
        } finally {
          if (outputStreamWriter != null) outputStreamWriter.close()
          if (fileOutputStream != null) fileOutputStream.close()
          val toastMessage = "CSV created at " + exportPath.toString()
          Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
      }
      return if (file == null) null else file.path
    } catch (e: Exception) {
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
            requestExternalStorageCode
      )
    }
  }
}