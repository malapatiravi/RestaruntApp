package com.app.android.mapgeneral.Activities;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;

import com.app.android.mapgeneral.Fragments.OnePaneFragment;
import com.app.android.mapgeneral.Fragments.TwoPaneDetailFragment;
import com.app.android.mapgeneral.Fragments.TwoPaneMainFragment;
import com.app.android.mapgeneral.Objects.Shared;
import com.app.android.mapgeneral.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by malz on 8/12/16.
 */

public class MainLogin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, TwoPaneMainFragment.ButtonClickedListener
{
  private GoogleApiClient         mGoogleApiClient        = null;
  private final static String     TAG                     = "MainLogin";
  private boolean twoPane                                 = false;
  private Fragment currentFragmentMain;
  private Fragment secondaryFragment;
  private int mainFragmentContainer;
  private int secondaryFragmentContainer;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_login);
    FacebookSdk.sdkInitialize(this);
    connectGoogleAPI();
    signOut();
    setupWindowAnimations();

    if (savedInstanceState == null)
    {
      //MasterFragment mas = new MasterFragment();
      //getSupportFragmentManager().beginTransaction().add(R.id.main_container, mas).commit();
    }
    mainFragmentContainer = R.id.main_container;
    if (findViewById(R.id.tablet_container) != null)
    {
      twoPane = true;
      Log.i("TAG", "two pane is true");
      secondaryFragmentContainer = R.id.tablet_container;
      currentFragmentMain = TwoPaneMainFragment.newInstance();
      secondaryFragment = TwoPaneDetailFragment.newInstance(0);
      inflateMainFragment();
      inflateSecondaryFragment();
    }
    else
    {
      twoPane = false;
      Log.i("TAG", "two pane is false");
      secondaryFragment = null;
      secondaryFragmentContainer = 0;
      currentFragmentMain = OnePaneFragment.newInstance();
      inflateMainFragment();
    }
  }

  private void inflateMainFragment()
  {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
    transaction.replace(mainFragmentContainer, this.currentFragmentMain);
    transaction.addToBackStack(null);
    transaction.commit();
  }
  private void inflateSecondaryFragment()
  {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
    transaction.replace(secondaryFragmentContainer, this.secondaryFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  private void setupWindowAnimations()
  {
    Fade fade = new Fade();
    fade.setDuration(1000);
    getWindow().setExitTransition(fade);
  }

  @Override public void onConnectionFailed(ConnectionResult connectionResult) {logMessage("Google API connection failed: "+connectionResult.getErrorMessage());}

  public void signOut()
  {
    LoginManager.getInstance().logOut();
    googleSignOut();
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
    mGoogleApiClient.stopAutoManage(this);
    mGoogleApiClient.disconnect();
    mGoogleApiClient = null;
  }

  private void connectGoogleAPI()
  {
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    if ((mGoogleApiClient == null) || (!mGoogleApiClient.isConnected())) mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
  }

  public void logMessage(String msg) {if (Shared.DEBUG_MODE) Log.i(TAG, msg);}

  @Override
  public void onButtonClicked(int position)
  {
    secondaryFragment = TwoPaneDetailFragment.newInstance(position);
    inflateSecondaryFragment();
  }
}
