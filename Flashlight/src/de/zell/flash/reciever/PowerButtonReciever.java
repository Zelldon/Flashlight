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
package de.zell.flash.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import de.zell.flash.R;

/**
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class PowerButtonReciever extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent arg1) {
    Log.d(PowerButtonReciever.class.getName(), context.getString(R.string.log_recieve_button));

    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
      Camera cam = null;
      try {
        cam = Camera.open();
      } catch (Exception e) {
        Log.e(PowerButtonReciever.class.getName(), e.getMessage(), e);
      }
      if (cam != null) {
        Parameters p = cam.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();

      }
    }
  }
}
