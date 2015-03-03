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
package de.zell.camera.api;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import de.zell.camera.CameraApi;
import de.zell.flash.R;
import de.zell.flash.receiver.PowerButtonReceiver;

/**
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class CameraOldApi implements CameraApi {
  
  private static Camera cam = null;
  private static boolean camOn = false;

  public void changeCamState(Context context) {
    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
      if (!isCamOpen()) {
        openCam();
      }

      if (isCamOpen()) {
        Camera.Parameters p = cam.getParameters();
        cam.stopPreview();
        if (!camOn) {
          camOn = true;
          p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        } else {
          camOn = false;
          p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }
        cam.setParameters(p);
        cam.startPreview();

        if (!camOn) {
          releaseCam(context);
        }
      }
    }
  }

  private static void releaseCam(Context context) {
    if (cam != null) {
      Log.d(PowerButtonReceiver.class.getName(), context.getString(R.string.log_released_cam));
      cam.stopPreview();
      cam.release();
      cam = null;
    }
  }

  private static boolean isCamOpen() {
    return cam != null;
  }

  private static void openCam() {
    try {
      cam = Camera.open();
    } catch (Exception e) {
      Log.e(PowerButtonReceiver.class.getName(), e.getMessage(), e);
    }
  }
}
