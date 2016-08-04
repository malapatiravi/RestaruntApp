package com.example.malz.listview.Login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malz.listview.AboutUs.About_Me;
import com.example.malz.listview.Activity_ActionBar;
import com.example.malz.listview.First_Fragment;
import com.example.malz.listview.MyUtility;
import com.example.malz.listview.R;
import com.example.malz.listview.Task3Activity;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by malz on 8/3/16.
 */

public class MainProject extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar mToolbar;
    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;
    Fragment mContent;
    Context cont;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mContent=new First_Fragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent).commit();
        Log.i("BackPressed","back");
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

  /*
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private GoogleApiClient mGoogleApiClient;
    private final String FIREBASEREF = "https://foodera.firebaseio.com/";
    private Firebase firebaseRef;

    private double mLatitude = 0;
    private double mLongitude = 0;
    private String userName = null, profileImage = null, mUID = null;
    boolean img = true;

    Fragment mContent;
    Toolbar mToolbar;
    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        firebaseRef = new Firebase(FIREBASEREF);
        getUserDetails();
        buildGoogleApiClient();
    }

    private void checkUserExists()
    {
        final String uid = mUID;
        firebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap users = (HashMap) dataSnapshot.getValue();
                Log.d("users", users.toString());
                String image = null;
                boolean exists = false;
                for (String key : (Set<String>)users.keySet()) {
                    if (key.equals(uid)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    HashMap<String, String> val = new HashMap<>();
                    val.put("default", "value");
                    firebaseRef.child("users/" + uid).setValue(val);
                }
                else
                {
                    HashMap user = (HashMap) users.get(mUID);
                    if (user != null)
                    {
                        image = (String) user.get("profileImage");
                    }
                }
                if (image != null)
                {
                    if (image.contains("http"))
                    {
                        profileImage = image;
                    }
                    else
                    {
                        img = false;
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void getUserDetails()
    {
        AuthData authData = firebaseRef.getAuth();
        mUID = authData.getUid();
        checkUserExists();
        switch (authData.getProvider()) {
            case "password":
                String email = (String) authData.getProviderData().get("email");
                //setProfileImage();
                if (profileImage == null)
                {
                    profileImage = (String) authData.getProviderData().get("profileImageURL");
                }
                userName = getUsername(email);
                break;
            case "facebook":
                //setProfileImage();
                userName = (String) authData.getProviderData().get("displayName");
                if (profileImage == null)
                {
                    profileImage = (String) authData.getProviderData().get("profileImageURL");
                }
                break;
            default:
                userName = (String) authData.getProviderData().get("displayName");
                break;
        }
    }

    private String getUsername(String email)
    {
        int ind = email.indexOf("@");
        if( ind >= 0)
        {
            String[] userDet = email.split("@");
            return userDet[0];
        }
        return null;
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
    public void onConnected(Bundle savedInstanceState) {
        LocationRequest mLocationRequest = createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Location mLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLocation != null)
        {
            mLatitude = mLocation.getLatitude();
            mLongitude = mLocation.getLongitude();
            String restaurantList = "https://api.eatstreet.com/publicapi/v1/restaurant/search?latitude="
                    + mLatitude + "&longitude=" + mLongitude + "&method=both";
            mToolbar = (Toolbar) findViewById(R.id.toolbar_cover);
            setSupportActionBar(mToolbar);

            mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
            mNavigationView.setNavigationItemSelectedListener(this);

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
            ActionBarDrawerToggle actionBarDrawerToggle =
                    new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer)
                    {
                        @Override
                        public void onDrawerClosed(View drawerView) {
                            super.onDrawerClosed(drawerView);
                        }

                        @Override
                        public void onDrawerOpened(View drawerView) {
                            super.onDrawerOpened(drawerView);
                            final CircleImageView imageView = (CircleImageView) drawerView.findViewById(R.id.profile_image);
                            final TextView userNameTV = (TextView) drawerView.findViewById(R.id.header_name);
                            userNameTV.setText(userName);
                            if (imageView != null)
                            {
                                Drawable image = imageView.getDrawable();
                                if (image != null)
                                {
                                    Log.d("profile image", image.toString());
                                }
                                else
                                {
                                    Log.d("profile image", "null");
                                    if (img)
                                    {
                                        MyDownloadImageAsyncTask myDownloadImageAsyncTask = new MyDownloadImageAsyncTask(imageView);
                                        myDownloadImageAsyncTask.execute(profileImage);
                                    }
                                    else
                                    {
                                        firebaseRef.child("users/"+mUID+"/profileImage")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot snapshot) {
                                                        String base64Image = (String) snapshot.getValue();
                                                        if (base64Image != null)
                                                        {
                                                            byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
                                                            imageView.setImageBitmap(
                                                                    BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(FirebaseError error) {
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    };

            mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            if (savedInstanceState != null)
            {
                mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
            }
            else
            {
                mContent = RestaurantListFragment.newInstance(restaurantList);
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mContent).commit();
        }
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

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.aboutus:
                Toast.makeText(this, "About us clicked", Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                firebaseRef.unauth();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recycler_menu, menu);
        return  true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.homeScreen:
                String restaurantList = "https://api.eatstreet.com/publicapi/v1/restaurant/search?latitude="
                        + mLatitude + "&longitude=" + mLongitude + "&method=both";
                mContent = RestaurantListFragment.newInstance(restaurantList);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, mContent).commit();
                break;
            case R.id.changepic:
                Intent intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
                break;
            case R.id.mycart:
                Intent cartIntent = new Intent(this, MyCartActivity.class);
                startActivity(cartIntent);
                break;
            case R.id.aboutus:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new AboutUs_Fragment())
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new RestaurantListFragment())
                        .commit();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListItemSelected(int id) {

    }

    @Override
    public void onListItemSelected(int id, HashMap<String, ?> movie)
    {
        Intent intent = new Intent(this, Restaurant_DetailView.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (mContent.isAdded())
            getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private class MyDownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap>
    {
        private final WeakReference<CircleImageView> imageViewReference;
        public MyDownloadImageAsyncTask(CircleImageView imageView)
        {
            imageViewReference = new WeakReference<>(imageView);
        }
        @Override
        protected Bitmap doInBackground(String... urls)
        {
            Bitmap bitmap = null;
            for (String url : urls)
            {
                bitmap = MyUtility.downloadImageusingHTTPGetRequest(url);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null)
            {
                final CircleImageView imageView = imageViewReference.get();
                if (imageView != null)
                {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }*/
}
