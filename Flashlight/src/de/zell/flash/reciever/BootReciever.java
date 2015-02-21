/*
 */
package de.zell.flash.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.zell.flash.FlashService;
import de.zell.flash.R;

/**
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class BootReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
      Log.d(BootReciever.class.getName(), arg0.getString(R.string.log_recieve_boot));
      Intent serviceIntent = new Intent(arg0, FlashService.class);
      arg0.startService(serviceIntent);
    }
}