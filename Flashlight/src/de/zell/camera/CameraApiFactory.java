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
package de.zell.camera;

import android.os.Build;
import de.zell.camera.api.CameraFlashOldApi;
import de.zell.camera.api2.CameraNewApi;

/**
 * Represents the CamaeraApiFactory which gives for the corresponding
 * build version the correct camera api object.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class CameraApiFactory {

  /**
   * The correct camera api for the corresponding build version.
   * 
   * @return the camera api object
   */
  public static CameraApi produceCameraApi() {
    if (Build.VERSION.SDK_INT < 21) {
      return new CameraFlashOldApi();
    } else {
      return new CameraNewApi();
    }
  }
}
