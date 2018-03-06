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

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class NotificationPublisher extends BroadcastReceiver {
  
  /*
  * The only reason that this class isn't in Kotlin is because
  * for some reason whenever you try to do a Build.VERSION check in Kotlin
  * the IDE will scream at you
  * */
  
  @Override
  public void onReceive(Context context, Intent intent) {
    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    if (Build.VERSION.SDK_INT >= 26) {
      NotificationChannel channel = new NotificationChannel("Reminders", "Reminders", NotificationManager.IMPORTANCE_DEFAULT);
      assert notificationManager != null;
      notificationManager.createNotificationChannel(channel);
    }
    Notification notification = intent.getParcelableExtra(Constants.NOTIFICATION);
    int id = intent.getIntExtra(Constants.NOTIFICATION_ID, 0);
    assert notificationManager != null;
    notificationManager.notify(id, notification);
  }
}
