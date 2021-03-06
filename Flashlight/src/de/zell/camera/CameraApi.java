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

import android.content.Context;

/**
 * Represents the camera api which will be used of the application.
 * The changeCamState method should toggle the camera state (on/off)
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public interface CameraApi {
  
  /**
   * The method toggles the camera state to on or off.
   * 
   * @param context the application context.
   */
  public void changeCamState(Context context);
}
