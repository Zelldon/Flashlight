package de.zell.flash;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;

public class FlashMainActivity extends Activity {

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    
    if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
      Camera cam = null;
      try {
        cam = Camera.open();
      } catch (Exception e) {
        Log.e(PowerButtonReciever.class.getName(), e.getMessage(), e);
      }
      if (cam != null) {
        Camera.Parameters p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();

      }
    }
    
    
    Log.d(BootReciever.class.getName(), "Boot - intent sercice");
    Intent serviceIntent = new Intent(this, FlashService.class);
    this.startService(serviceIntent);
  }
}
