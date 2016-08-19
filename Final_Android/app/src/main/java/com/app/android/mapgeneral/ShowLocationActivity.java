package com.app.android.mapgeneral;

/**
 * Created by malz on 8/4/16.
 */

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.app.android.mapgeneral.Fragments.RestaurantListFragment;

public class ShowLocationActivity extends AppCompatActivity {//implements LocationListener {
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;
    Fragment mContent;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlocation);
        latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);
        GPSTracker objGps=new GPSTracker(this);
        latituteField.setText(String.valueOf(objGps.getLatitude()));
        longitudeField.setText(String.valueOf(objGps.getLongitude()));
        String restaurantList = "https://api.eatstreet.com/publicapi/v1/restaurant/search?latitude="
                + objGps.getLatitude() + "&longitude=" + objGps.getLongitude() + "&method=both";
        mContent = RestaurantListFragment.newInstance(restaurantList);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mContent).commit();

     /*   // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = LocationManager.GPS_PROVIDER;//locationManager.getBestProvider(criteria, false);
        Log.i("Provider is:","This is: "+provider);
        Location location=null;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
           // ActivityCompat.requestPermissions((this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LocationServices.FusedLocationApi.requestLocationUpdates()));

        }
        else
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);



        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latituteField.setText(String.valueOf(location.getLatitude()));
            longitudeField.setText("Location not available");
        }*/
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
    }

    /* Request updates at startup */
    /*
    @Override
    protected void onResume() {
        super.onResume();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        else
            locationManager.requestLocationUpdates(provider, 400, 1, this);
    }*/

    /* Remove the locationlistener updates when Activity is paused */
  /*  @Override
    protected void onPause() {
        super.onPause();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        locationManager.removeUpdates(this);
        else
            locationManager.removeUpdates(this);
    }*/

    /*@Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }*/



}