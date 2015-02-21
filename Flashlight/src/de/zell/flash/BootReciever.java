/*
 */
package de.zell.flash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class BootReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
      Log.d(BootReciever.class.getName(), "Boot - intent sercice");
      Intent serviceIntent = new Intent(arg0, FlashService.class);
      arg0.startService(serviceIntent);
    }
}