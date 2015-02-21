/*
 */
package de.zell.flash;

import de.zell.flash.reciever.PowerButtonReciever;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class FlashService extends Service {

  private PowerButtonReciever reciever = null;

  @Override
  public void onCreate() {
    reciever = new PowerButtonReciever();
    Log.d(FlashService.class.getName(), "Service - onStartCommand");
    super.onCreate();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    IntentFilter filter = new IntentFilter();
    filter.addAction(Intent.ACTION_SCREEN_OFF);
    filter.addAction(Intent.ACTION_SCREEN_ON);
    filter.addAction(Intent.ACTION_POWER_CONNECTED);
    filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
    
    registerReceiver(new PowerButtonReciever(), filter);
    Log.d(FlashService.class.getName(), "Service - onStartCommand");
    return super.onStartCommand(intent, flags, startId); //To change body of generated methods, choose Tools | Templates.
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
