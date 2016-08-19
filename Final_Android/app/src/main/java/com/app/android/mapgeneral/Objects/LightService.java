package com.app.android.mapgeneral.Objects;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.android.mapgeneral.R;

import java.io.File;

/**
 * Created by malz on 8/14/16.
 */
public class LightService extends Service
{
  private static final String TAG = "LightService";
  @Override
  public IBinder onBind(Intent intent) {return null;}

  @Override
  public void onCreate()
  {
    super.onCreate();
    Log.i(TAG, "Light service started as service...");
    final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext()).setSmallIcon(R.drawable.ic_lightbulb_outline_black_24dp).setContentTitle("Danger Notification").setContentText("Danger!").setAutoCancel(true);
    final SensorEventListener LightSensorListener = new SensorEventListener()
    {
      final NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(getBaseContext().NOTIFICATION_SERVICE);
      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) {}
      @Override
      public void onSensorChanged(SensorEvent event)
      {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT)
        {
          if (event.values[0] > 200)
          {
            Uri alarmSound = Uri.fromFile(new File("/sdcard/Ringtones/Police.mp3"));
            mBuilder.setSound(alarmSound);
            mBuilder.setColor(16711680);
            notificationManager.notify(0, mBuilder.build());
          }
          else if (event.values[0] > 150 && event.values[0] <= 200)
          {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setColor(32768);
            mBuilder.setSound(alarmSound);
            notificationManager.notify(0, mBuilder.build());
          }
        }
      }
    };
    SensorManager mySensorManager = (SensorManager) getBaseContext().getSystemService(Context.SENSOR_SERVICE);
    Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    if (LightSensor != null)
    {
      mySensorManager.registerListener(LightSensorListener, LightSensor, SensorManager.SENSOR_DELAY_NORMAL);
      logMessage("sensor is registered");
    }
    else {logMessage("Light sensor is not available");}
  }

  @Override
  public void onDestroy() {super.onDestroy();}
  public void logMessage(String msg) {if (Shared.DEBUG_MODE) Log.i(TAG, msg);}
}
