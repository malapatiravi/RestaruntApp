package com.app.android.mapgeneral.Fragments;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.android.mapgeneral.Objects.LightService;
import com.app.android.mapgeneral.R;

import java.io.File;

/**
 * Created by malz on 8/14/16.
 */
public class LightSensor extends Fragment
{
  TextView textLIGHT_available, textLIGHT_reading;
  View rootView;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    super.onCreateView(inflater, container, savedInstanceState);
    rootView = inflater.inflate(R.layout.fragment_lightsensor, container, false);


    textLIGHT_available = (TextView) rootView.findViewById(R.id.LIGHT_available);
    textLIGHT_reading = (TextView) rootView.findViewById(R.id.LIGHT_reading);
    final Intent i = new Intent(getActivity(), LightService.class);
    //getActivity().startService(i);
    Log.i("TAG", "Starting service...");
    Button alertMe = (Button) rootView.findViewById(R.id.btnAlertMe);
    Button stopAlertMe = (Button) rootView.findViewById(R.id.btmStopAlert);

    alertMe.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        if (isMyServiceRunning1(LightService.class, getActivity()) != true) getActivity().startService(i);
        else Toast.makeText(getActivity().getBaseContext(), "Service is already running, Please stop", Toast.LENGTH_LONG).show();
      }
    });
    stopAlertMe.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        if (isMyServiceRunning1(LightService.class, getActivity()) == true) getActivity().stopService(i);
        else Toast.makeText(getActivity().getBaseContext(), "Service is already Stop", Toast.LENGTH_LONG).show();
      }
    });
    return rootView;
  }

  private boolean isMyServiceRunning(Class<?> serviceClass)
  {
    ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
    {
      if (serviceClass.getName().equals(service.service.getClassName()))
      {
        return true;
      }
    }
    return false;
  }

  private boolean isMyServiceRunning1(Class<?> serviceClass, Context context)
  {
    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
    {
      if (serviceClass.getName().equals(service.service.getClassName()))
      {
        Log.i("Service already", "running");
        return true;
      }
    }
    Log.i("Service not", "running");
    return false;
  }
}
