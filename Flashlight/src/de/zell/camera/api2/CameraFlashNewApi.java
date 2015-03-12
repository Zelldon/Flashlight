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
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import de.zell.camera.CameraApi;
import de.zell.flash.receiver.PowerButtonReceiver;
import java.util.ArrayList;

/**
 * Represents the CameraFlashNewApi which corresponds to the new
 * android camera api and sets the flash of the camera.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class CameraFlashNewApi implements CameraApi {

  /**
   * The error string which will be shown if the camera contains no output sizes for the surface texture.
   */
  private static final String CAMERA_OUTPUT_SIZE_ERROR_STR = "Camera %1$s doesn't support any outputSize.";
  
  /**
   * The background thread name which identifies the thread.
   */
  private static final String BACKGROUND_THREAD_NAME = "CameraThread";
  
  /**
   * The background thread which will be used from the camera api.
   */
  private static HandlerThread backgroundThread = null;
  
  /**
   * The background handler which is also used by the camera api.
   */
  private static Handler backgroundHandler = null;

  /**
   * Starts the background thread and creates a new handler.
   */
  public static void startBackground() {
    backgroundThread = new HandlerThread(BACKGROUND_THREAD_NAME);
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
  /**
   * The surface texture which will be used to create a surface for the camera api.
   */
  private static SurfaceTexture surfaceTexture = null;
  
  /**
   * The surface which will be used by the camera api to create a session request.
   */
  private static Surface surface = null;
  
  /**
   * The camera manager which will be used to open the camera device.
   */
  private static CameraManager manager = null;
  
  /**
   * The camera device object which represents the camera hardware.
   */
  private static CameraDevice cam = null;
  
  /**
   * The current camera session which will be used to toggle the flashlight.
   */
  private static CameraCaptureSession camSession = null;
  
  /**
   * The callback to configure asynchronously the camera after creation.
   */
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

  /**
   * The method toggles the camera state to on or off.
   * 
   * @param context the application context.
   */
  public void changeCamState(Context context) {
    if (cam == null) {
      startBackground();
      openCam(context);
    } else {
      closeCam();
      stopBackground();
    }
  }

  /**
   * Opens the camera device with the new android api.
   * 
   * @param context the application context.
   */
  private static void openCam(Context context) {
    manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    try {
      String cams[] = manager.getCameraIdList();
      if (cams.length > 0 && backgroundHandler != null) {
        manager.openCamera(cams[0], callBack, backgroundHandler);
      }
    } catch (CameraAccessException ex) {
      Log.e(PowerButtonReceiver.class.getName(), ex.getMessage(), ex);
    }
  }

  /**
   * Closes the camera device and all other resource which was used with the camera.
   */
  private static void closeCam() {
    if (camSession != null) {
      camSession.close();
      camSession = null;
    }
    
    if (surface != null) {
      surface.release();
      surfaceTexture.release();
      surface = null;
      surfaceTexture = null;
    }
    
    if (cam != null) {
      cam.close();
      cam = null;
    }
    manager = null;
  }

  /**
   * Returns the smallest surface size which is possible with the given camera.
   * To save memory because the surface is not really used for the flashlight.
   * 
   * @param cameraId the id of the current camera device
   * @return the smallest surface size
   * @throws CameraAccessException the camera access exception which was thrown 
   *                               if the id is not correct or something else is not
   *                               acceptable
   */
  private static Size getSmallestSize(String cameraId) throws CameraAccessException {
    if (manager == null)
      return null;
    
    Size[] outputSizes = manager.getCameraCharacteristics(cameraId)
            .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            .getOutputSizes(SurfaceTexture.class);
    if (outputSizes == null || outputSizes.length == 0) {
      throw new IllegalStateException(String.format(CAMERA_OUTPUT_SIZE_ERROR_STR, cameraId));
    }
    Size chosen = outputSizes[0];
    for (Size s : outputSizes) {
      if (chosen.getWidth() >= s.getWidth() && chosen.getHeight() >= s.getHeight()) {
        chosen = s;
      }
    }
    return chosen;
  }

  /**
   * Creates a surface list which contains only a single small surface to 
   * create a camera session request.
   * 
   * @return the list with the single surface
   * @throws CameraAccessException if something goes wrong with the camera access
   *                               after calling getId
   */
  private static ArrayList<Surface> createSurfaces() throws CameraAccessException {
    if (surface == null && cam != null) {
      surfaceTexture = new SurfaceTexture(0);
      Size size = getSmallestSize(cam.getId());
      surfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());
      surface = new Surface(surfaceTexture);
    }
    ArrayList<Surface> outputs = new ArrayList<Surface>(1);
    outputs.add(surface);
    return outputs;
  }

  /**
   * Configures the camera device and creates a capture session which
   * should toggle the flashlight.
   */
  private static void configureCam() {
    if (cam != null) {
      try {
        cam.createCaptureSession(createSurfaces(), new CameraCaptureSession.StateCallback() {

          @Override
          public void onConfigured(CameraCaptureSession session) {
            try {
              CaptureRequest.Builder builder = cam.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
              builder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
              builder.addTarget(surface);
              CaptureRequest req = builder.build();
              session.setRepeatingRequest(req, null, backgroundHandler);
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
