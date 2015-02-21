package de.zell.flash;

import de.zell.flash.reciever.BootReciever;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class FlashMainActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(BootReciever.class.getName(), "Boot - intent sercice");
    Intent serviceIntent = new Intent(this, FlashService.class);
    this.startService(serviceIntent);
    finish();
  }
}
