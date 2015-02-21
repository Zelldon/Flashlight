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
package de.zell.flash.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import de.zell.flash.ButtonPress;
import de.zell.flash.R;
import java.util.Date;

/**
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class PowerButtonReceiver extends BroadcastReceiver {

  private static ButtonPress press = null;
  private static Camera cam = null;
  private static boolean camOn = false;

  @Override
  public void onReceive(Context context, Intent arg1) {
    Log.d(PowerButtonReceiver.class.getName(), context.getString(R.string.log_recieve_button));

    calculateValidButtonPress();
    Log.d(PowerButtonReceiver.class.getName(), press.toString());
    Log.d(PowerButtonReceiver.class.getName(), new Date().toString());

    if (press.getCount() == 3) {
      press = null;
      changeCamState(context);
    }
  }
  
  private void calculateValidButtonPress() {
    if (press == null) {
      press = new ButtonPress();
    } else {
      long firstPress = press.getFirstButtonPressedTime().getTime();
      long now = new Date().getTime();
      long diff = now - firstPress;
      if (diff > 0 && diff < 2000) {
        press.incrementPress();
      } else {
        press = new ButtonPress();
      }
    }
  }
  
  private void changeCamState(Context context) {
      if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
        if (!isCamOpen()) {
          openCam();
        }

        if (isCamOpen()) {
          Parameters p = cam.getParameters();
          if (!camOn) {
            camOn = true;
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
          } else {
            camOn = false;
            p.setFlashMode(Parameters.FLASH_MODE_OFF);
          }
          cam.setParameters(p);
          cam.startPreview();
        }
      }
  }

  private boolean isCamOpen() {
    return cam != null;
  }

  private void openCam() {
    try {
      cam = Camera.open();
    } catch (Exception e) {
      Log.e(PowerButtonReceiver.class.getName(), e.getMessage(), e);
    }
  }
}
