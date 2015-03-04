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
package de.zell.camera.api2;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;
import de.zell.camera.CameraApi;
import de.zell.flash.receiver.PowerButtonReceiver;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class CameraNewApi implements CameraApi {

  private static HandlerThread backgroundThread = null;
  private static Handler backgroundHandler = null;

  public static void startBackground() {
    backgroundThread = new HandlerThread("CameraThread");
    backgroundThread.start();
    backgroundHandler = new Handler(backgroundThread.getLooper());
  }

  public static void stopBackground() {
    backgroundThread.quitSafely();
    try {
      backgroundThread.join();
      backgroundThread = null;
      backgroundHandler = null;
    } catch (InterruptedException ex) {
      Log.e(PowerButtonReceiver.class.getName(), ex.getMessage(), ex);
    }
  }

  //====================================
  //==============API 21================
  //====================================
  private static CameraDevice cam = null;
  private static CameraCaptureSession camSession = null;
  private static final CameraDevice.StateCallback callBack = new CameraDevice.StateCallback() {

    @Override
    public void onOpened(CameraDevice camera) {
      cam = camera;
      configureCam();
    }

    @Override
    public void onDisconnected(CameraDevice camera) {
      cam.close();
      cam = null;
    }

    @Override
    public void onError(CameraDevice camera, int error) {
      cam.close();
      cam = null;
      Log.e(PowerButtonReceiver.class.getName(), "Error: " + error);
    }
  };

  public void changeCamState(Context context) {
    if (cam == null) {
      startBackground();
      openCam(context);
    } else {
      closeCam();
      stopBackground();
    }
  }

  private static void openCam(Context context) {
    CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    try {
      String cams[] = manager.getCameraIdList();
      if (cams.length > 0 && backgroundHandler != null) {
        manager.openCamera(cams[0], callBack, backgroundHandler);
      }
    } catch (CameraAccessException ex) {
      Log.e(PowerButtonReceiver.class.getName(), ex.getMessage(), ex);
    }

  }

  private static void closeCam() {
    if (camSession != null) {
      camSession.close();
      camSession = null;
    }

    if (cam != null) {
      cam.close();
      cam = null;
    }

  }

  private static void configureCam() {
    if (cam != null) {
      try {
        CaptureRequest.Builder builder = cam.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        builder.setTag(CaptureRequest.FLASH_MODE_TORCH);
        final CaptureRequest req = builder.build();
        Surface s = new Surface(new SurfaceTexture(0));
        cam.createCaptureSession(Arrays.asList(s), new CameraCaptureSession.StateCallback() {

          @Override
          public void onConfigured(CameraCaptureSession session) {
            try {
              session.setRepeatingRequest(req, null, backgroundHandler);
              //session.capture(req, null, null);
            } catch (CameraAccessException ex) {
              Log.e(PowerButtonReceiver.class.getName(), ex.getMessage(), ex);
            }
          }

          @Override
          public void onConfigureFailed(CameraCaptureSession session) {
            session.close();
            Log.e(PowerButtonReceiver.class.getName(), session.getDevice().getId());
          }
        }, null);
      } catch (CameraAccessException ex) {
        Log.e(PowerButtonReceiver.class.getName(), ex.getMessage(), ex);
      }
    }
  }
}
