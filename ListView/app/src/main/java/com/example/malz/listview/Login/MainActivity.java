package com.example.malz.listview.Login;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.malz.listview.AboutUs.About_Me;
import com.example.malz.listview.Activity_ActionBar;
import com.example.malz.listview.First_Fragment;
import com.example.malz.listview.MainActivity_Recycler;
import com.example.malz.listview.R;
import com.example.malz.listview.Task3Activity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements First_Fragment.onButtonclickselectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, //LocationListener,
        NavigationView.OnNavigationItemSelectedListener{

    Toolbar mToolbar;
    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;
    Fragment mContent;
    Context cont;
    //For location services
    private GoogleApiClient mGoogleApiClient;
    private double mLatitude = 0;
    private double mLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cont=this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar_cover);
        setSupportActionBar(mToolbar);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this, mDrawerLayout,  mToolbar,R.string.open_drawer, R.string.close_drawer)
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if (savedInstanceState != null)
        {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }
        else{
            mContent=new First_Fragment();
            this.setTitle("Front Page");
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent).commit();



    }
    private void buildGoogleApiClient()
    {
        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    @Override
    public void onConnected(Bundle savedInstanceStat) {
        LocationRequest mLocationRequest = createLocationRequest();
       // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Location mLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        mLatitude = 43.0481;//mLocation.getLatitude();
        mLongitude = 76.1474;//mLocation.getLongitude();
        String restaurantList = "https://api.eatstreet.com/publicapi/v1/restaurant/search?latitude="
                + mLatitude + "&longitude=" + mLongitude + "&method=both";

        Log.i("At lat: ",""+restaurantList);
    }
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    public void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("onConnectionFailed ", "Connection failed: ConnectionResult.getErrorCode() = " +
                connectionResult.getErrorCode());
    }
    /*@Override
    public void onLocationChanged(Location location) {

    }*/


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mContent=new First_Fragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent).commit();
        Log.i("BackPressed","back");
    }

    @Override
    public void onButtonClickListener(int position) {

        if(position==0)
        {
            mContent=new About_Me();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, mContent).commit();
        }
        else if(position==1)
        {
            Log.i("Main Activity","View Pager: "+position);
            //About_Me first=new About_Me();
           // getSupportFragmentManager().beginTransaction()
             //     .replace(R.id.fragment_container, first).commit();
           Intent intent0=new Intent(cont,MainActivity_Recycler.class);
            startActivity(intent0);
            //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch(id){
            case R.id.aboutme_menu:
                mContent=new About_Me();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mContent).commit();
                mDrawerLayout.closeDrawers();

                break;
            case R.id.task2_menu:
                Intent intent0=new Intent(cont,Activity_ActionBar.class);
                startActivity(intent0);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.task3_menu:
                Intent intent1=new Intent(cont,Task3Activity.class);
                startActivity(intent1);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.home:
                mContent=new First_Fragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mContent).commit();
                mDrawerLayout.closeDrawers();

            default:
                First_Fragment myfrag=new First_Fragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, myfrag).commit();
        }

        return true;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mContent.isAdded())
            getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }


}
