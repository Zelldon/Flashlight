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

import de.zell.flash.receiver.BootReceiver;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Represents the main activity of the application to create the flash service
 * which also starts the power button receiver.
 * 
 * The application contains no content and finishes after starting the service.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class FlashMainActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(BootReceiver.class.getName(), getString(R.string.log_activity_start));
    Toast.makeText(this, getString(R.string.service_started), Toast.LENGTH_LONG).show();
    Intent serviceIntent = new Intent(this, FlashService.class);
    this.startService(serviceIntent);
    finish();
  }
}
