package com.app.android.mapgeneral.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.android.mapgeneral.Fragments.AddCardFragment;
import com.app.android.mapgeneral.Fragments.RestaurantDetailsFragment;
import com.app.android.mapgeneral.Fragments.UserProfile;
import com.app.android.mapgeneral.GPSTracker;
import com.app.android.mapgeneral.MyUtility;
import com.app.android.mapgeneral.R;
import com.app.android.mapgeneral.Fragments.RestaurantListFragment;
import com.app.android.mapgeneral.Objects.Shared;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RestaurantListFragment.RestaurantSelectListener, GoogleApiClient.OnConnectionFailedListener
{
  private static final String     TAG                     = "MainActivity";
  private String                  user_name;
  private String                  user_email;
  private String                  user_image_url;
  private String                  user_id;
  private String                  origin;
  private CircleImageView         imageView;
  private Context                 context;
  static final int                REQUEST_IMAGE_CAPTURE   = 1;
  private boolean                 image_status            = false;
  private GoogleApiClient         mGoogleApiClient        = null;
  private Fragment                currentFragment         = null;
  private int                     fragmentContainer       = 0;


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupWindowAnimations();
    context = this;
    FacebookSdk.sdkInitialize(this);
    connectGoogleAPI();
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    Intent i = getIntent();
    user_name       = i.getExtras().getString("user_name");
    user_email      = i.getExtras().getString("user_email");
    user_image_url  = i.getExtras().getString("image_url");
    user_id         = i.getExtras().getString("user_id");
    origin          = i.getExtras().getString("origin");
    logMessage("Extras: " + origin + "," + user_id + "," + i.getExtras().getString("image_url") + "," + i.getExtras().getString("user_name") + "," + i.getExtras().getString("user_email") + "," + i.getExtras().getString("user_id"));

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    {
      @Override
      public void onDrawerClosed(View drawerView)
      {
        super.onDrawerClosed(drawerView);
      }

      @Override
      public void onDrawerOpened(View drawerView)
      {
        super.onDrawerOpened(drawerView);
        imageView = (CircleImageView) drawerView.findViewById(R.id.profile_image);
        final TextView userNameTV = (TextView) drawerView.findViewById(R.id.header_name);
        final TextView userEmailTV = (TextView) drawerView.findViewById(R.id.header_email);
        userNameTV.setText(user_name);
        userEmailTV.setText(user_email);
        if (image_status == false && user_image_url != "null")
        {
          ImageDownloader myDownloadImageAsyncTask = new ImageDownloader(imageView);
          myDownloadImageAsyncTask.execute(user_image_url);
        }
      }
    };
    drawer.addDrawerListener(toggle);
    toggle.syncState();
    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    fragmentContainer = R.id.container;

    //Check for youtube service issues
    final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
    if (result != YouTubeInitializationResult.SUCCESS) result.getErrorDialog(this, 0).show();
    else if (savedInstanceState == null) showNearbyPlacesInFragment();
  }

  private void setupWindowAnimations()
  {
    Slide slide = new Slide();
    slide.setDuration(1000);
    getWindow().setEnterTransition(slide);
  }

  @Override
  public void onBackPressed()
  {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
    else
    {
      //TODO: Make sure to stop from starting previous activity without logout
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    switch(id)
    {
      case (R.id.action_settings):
        logMessage("Settings action clicked.");
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item)
  {
    // Handle navigation view item clicks here.
    int id = item.getItemId();
    switch (id)
    {
      case (R.id.nav_nearby_places):
        showNearbyPlacesInFragment();
        break;
      case (R.id.take_photo_main):
        dispatchTakePictureIntent();
        break;
      case (R.id.nav_add_creditcard):
        currentFragment = new AddCardFragment().newInstance(user_id, origin);
        inflateFragment(null);
        break;
      case (R.id.nav_user_profile):
        currentFragment = new UserProfile().newInstance(user_id, origin);
        inflateFragment(null);
        break;
      case (R.id.nav_logout):
        signOut();
        break;
    }
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
    {
      Bundle extras = data.getExtras();
      Bitmap imageBitmap = (Bitmap) extras.get("data");
      imageView.setImageBitmap(imageBitmap);
      image_status = true;
    }
  }

  private void dispatchTakePictureIntent()
  {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
  }

  @Override
  public void onRestaurantSelected(int id, HashMap<String, ?> restaurant, ImageView vIcon)
  {
    currentFragment = RestaurantDetailsFragment.newInstance(restaurant);
    currentFragment.setSharedElementEnterTransition(new RestaurantDetailTransition());
    currentFragment.setSharedElementReturnTransition(new RestaurantDetailTransition());
    inflateFragment(vIcon);
  }

  @Override public void onConnectionFailed(ConnectionResult connectionResult) {logMessage("Google API connection failed: "+connectionResult.getErrorMessage());}

  private class ImageDownloader extends AsyncTask<String, Void, Bitmap>
  {
    private final WeakReference<CircleImageView> imageViewReference;
    public ImageDownloader(CircleImageView imageView) {imageViewReference = new WeakReference<>(imageView);}
    @Override
    protected Bitmap doInBackground(String... urls)
    {
      Bitmap bitmap = null;
      for (String url : urls) bitmap = MyUtility.downloadImageusingHTTPGetRequest(url);
      return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
      if (bitmap != null)
      {
        final CircleImageView imageView = imageViewReference.get();
        if (imageView != null) imageView.setImageBitmap(bitmap);
      }
    }
  }

  public void showNearbyPlacesInFragment()
  {
    logMessage("showing nearby places");
    GPSTracker objGps = new GPSTracker(this);
    String restaurantList = "https://api.eatstreet.com/publicapi/v1/restaurant/search?latitude=" + objGps.getLatitude() + "&longitude=" + objGps.getLongitude() + "&method=both";
    currentFragment = RestaurantListFragment.newInstance(restaurantList);
    inflateFragment(null);
  }

  private void inflateFragment(ImageView sharedImage)
  {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    if (sharedImage == null)
    {
      transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
    }
    else
    {
      transaction.addSharedElement(sharedImage, sharedImage.getTransitionName());
    }
    transaction.replace(fragmentContainer, this.currentFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }
  private void connectGoogleAPI()
  {
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    if ((mGoogleApiClient == null) || (!mGoogleApiClient.isConnected())) mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
  }

  public void signOut()
  {
    LoginManager.getInstance().logOut();
    googleSignOut();
    Intent loginIntent = new Intent(context, MainLogin.class);
    finish();
    startActivity(loginIntent);
  }
  public void googleSignOut()
  {
    if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
    {
      Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>()
      {
        @Override public void onResult(Status status) {mGoogleApiClient = null;}
      });
    }
  }

  public class RestaurantDetailTransition extends TransitionSet
  {
    public RestaurantDetailTransition()
    {
      setOrdering(ORDERING_TOGETHER);
      addTransition(new ChangeBounds()).addTransition(new ChangeTransform()).addTransition(new ChangeImageTransform());
    }
  }

  public void logMessage(String msg) {if (Shared.DEBUG_MODE) Log.i(TAG, msg);}
}