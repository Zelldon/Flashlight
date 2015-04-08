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
import android.util.Log;
import de.zell.camera.CameraApiFactory;
import de.zell.flash.ButtonPress;
import de.zell.flash.R;
import java.util.Date;

/**
 * The power button receiver which will be called if the power button was pressed.
 * After 3 times of pressing the power button the flash light should be toggled
 * but only if the button was pressed in a valid pressing interval.
 * The interval is defined by the BUTTON_PRESS_INTERVAL field.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class PowerButtonReceiver extends BroadcastReceiver {

  /**
   * The button press object which counts the button press.
   */
  private static ButtonPress press = null;
  
  /**
   * Defines the valid interval for the button pressing.
   */
  private static final Long BUTTON_PRESS_INTERVAL = 2000l;
  
  /**
   * Defines the button press count on which the camera state should be changed.
   */
  private static final int BUTTON_PRESS_COUNT = 3;
  
  @Override
  public void onReceive(Context context, Intent arg1) {
    Log.d(PowerButtonReceiver.class.getName(), context.getString(R.string.log_receive_button));

    calculateValidButtonPress();
    Log.d(PowerButtonReceiver.class.getName(), press.toString());
    Log.d(PowerButtonReceiver.class.getName(), new Date().toString());

    if (press.getCount() == BUTTON_PRESS_COUNT) {
      press = null;
      CameraApiFactory.produceCameraApi().changeCamState(context);
    }
  }

  /**
   * Calculates the valid button press. 
   * If the button press intent was inside the defined interval the
   * count will be increment if not the button press object will be 
   * reseted. 
   * 
   * The firstButtonPressedTime and BUTTON_PRESS_INTERVAL will be used 
   * to determine if the button press was in the defined interval.
   * @see ButtonPress
   */
  private void calculateValidButtonPress() {
    if (press == null) {
      press = new ButtonPress();
    } else {
      long firstPress = press.getFirstButtonPressedTime().getTime();
      long now = new Date().getTime();
      long diff = now - firstPress;
      if (diff > 0 && diff < BUTTON_PRESS_INTERVAL) {
        press.incrementPress();
      } else {
        press = new ButtonPress();
      }
    }
  }

}
