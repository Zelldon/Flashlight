/*
 * Copyright (C) 2015 Christopher Zell <zelldon91@googlemail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.zell.flash;

import de.zell.flash.receiver.PowerButtonReceiver;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * Represents the flash service which will be started by the main 
 * activity or by the boot receiver. It registers the power button
 * receiver which should toggle the flash light.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class FlashService extends Service {

  /**
   * The power button receiver which will be registered by the service.
   */
  private PowerButtonReceiver reciever = null;

  @Override
  public void onCreate() {
    reciever = new PowerButtonReceiver();
    Log.d(FlashService.class.getName(), getString(R.string.log_service_create));
    super.onCreate();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    IntentFilter filter = new IntentFilter();
    filter.addAction(Intent.ACTION_SCREEN_OFF);
    filter.addAction(Intent.ACTION_SCREEN_ON);
    
    registerReceiver(new PowerButtonReceiver(), filter);
    Log.d(FlashService.class.getName(),  getString(R.string.log_activity_start));
    return super.onStartCommand(intent, flags, startId); 
  }

  @Override
  public IBinder onBind(Intent arg0) {
    return null;
  }

  @Override
  public void onDestroy() {
    unregisterReceiver(reciever);
    super.onDestroy();
  }
}
