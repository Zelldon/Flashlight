package de.zell.flash;

import de.zell.flash.reciever.BootReciever;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class FlashMainActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(BootReciever.class.getName(), getString(R.string.log_activity_start));
    Toast.makeText(this, getString(R.string.service_started), Toast.LENGTH_LONG).show();
    Intent serviceIntent = new Intent(this, FlashService.class);
    this.startService(serviceIntent);
    finish();
  }
}
