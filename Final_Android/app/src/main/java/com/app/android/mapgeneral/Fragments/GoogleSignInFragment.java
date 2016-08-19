package com.app.android.mapgeneral.Fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.android.mapgeneral.Activities.MainActivity;
import com.app.android.mapgeneral.R;
import com.app.android.mapgeneral.Objects.Shared;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;

import org.json.JSONObject;

import java.util.HashMap;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by malz on 8/12/16.
 */
public class GoogleSignInFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener
{
  private static final String     TAG                     = "SignInActivity";
  private static final int        RC_SIGN_IN              = 9001;
  private GoogleApiClient         mGoogleApiClient        = null;
  private TextView                statusTextView;
  private CallbackManager         mFacebookCallbackManager;
  private View                    rootView;

  public static GoogleSignInFragment newInstance(int position, String title)
  {
    GoogleSignInFragment frag = new GoogleSignInFragment();
    Bundle args = new Bundle();
    args.putInt("position", position);
    frag.setArguments(args);
    return frag;
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    super.onCreateView(inflater, container, savedInstanceState);
    FacebookSdk.sdkInitialize(getActivity());

    rootView = inflater.inflate(R.layout.fragment_signin_social, container, false);
    statusTextView                = (TextView) rootView.findViewById(R.id.googleSigninStatus);
    SignInButton signInButton     = (SignInButton) rootView.findViewById(R.id.sign_in_google);

    signInButton.setOnClickListener(this);

    initializeFacebookLogin();
    connectGoogleAPI();

    logMessage("OnCreate: Done");
    return rootView;
  }

  @Override
  public void onStop()
  {
    super.onStop();
    if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
    {
      mGoogleApiClient.stopAutoManage(getActivity());
      mGoogleApiClient.disconnect();
    }
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
    {
      mGoogleApiClient.stopAutoManage(getActivity());
      mGoogleApiClient.disconnect();
    }
  }

  @Override
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.sign_in_google:
        logMessage("Google sign in");
        googleSignIn();
        break;
    }
  }

  public void initializeFacebookLogin()
  {
    mFacebookCallbackManager = CallbackManager.Factory.create();
    LoginButton mFacebookLoginButton = (LoginButton) rootView.findViewById(R.id.login_with_facebook);
    mFacebookLoginButton.setFragment(this);
    mFacebookLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>()
    {
      @Override
      public void onSuccess(LoginResult loginResult)
      {
        logMessage("Facebook is success: "+loginResult.toString());
        Bundle params = new Bundle();
        params.putString("fields", "id,email,gender,name,cover,picture.type(large)");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET, new GraphRequest.Callback()
        {
          @Override
          public void onCompleted(GraphResponse response)
          {
           if (response != null)
           {
             try
             {
               JSONObject data = response.getJSONObject();
               logMessage("All data: " + data);
               if (data.has("picture"))
               {
                 String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                 HashMap<String, String> dataMap = new HashMap<>();
                 dataMap.put("user_name", data.getString("name"));
                 dataMap.put("user_email", data.getString("email"));
                 dataMap.put("user_id", data.getString("id"));
                 dataMap.put("image_url", profilePicUrl);
                 dataMap.put("origin", "social");
                 loggedInActivity(dataMap);
               }
             }
             catch (Exception e)
             {
               e.printStackTrace();
             }
           }
          }
        }).executeAsync();
      }

      @Override
      public void onCancel()
      {
        logMessage("Facebook cancelled.");
      }

      @Override
      public void onError(FacebookException error)
      {
        logMessage("Facebook error: "+error.getMessage());
      }
    });
  }

  public void googleSignIn()
  {
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    logMessage("OnActivity Result Requested code is:" + requestCode);
    if (requestCode == RC_SIGN_IN)
    {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      logMessage("Result is: " + result.getStatus());
      GoogleSignInAccount acct = null;
      if (result.isSuccess())   acct = result.getSignInAccount();
      else                      logMessage("There was some problem in signing in with google: " + result.getStatus());
      if (acct != null)
      {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("user_name",    (acct.getDisplayName() == null)   ? acct.getEmail()         : acct.getDisplayName());
        dataMap.put("user_email",   (acct.getEmail() == null)         ? "no_email@google.com"   : acct.getEmail());
        dataMap.put("user_id",      (acct.getId() == null)            ? "0"                     : acct.getId());
        dataMap.put("image_url",    (acct.getPhotoUrl() == null)      ? null                    : acct.getPhotoUrl().toString());
        dataMap.put("origin",       "social");
        loggedInActivity(dataMap);
      }
    }
    else
    {
      logMessage("OnActivityResult: Facebook");
      mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult)
  {
    logMessage("Connected failed function" + connectionResult);
  }

  public void loggedInActivity(HashMap<String, String> dataMap)
  {
    Intent loggedInActivity = new Intent(getActivity().getBaseContext(), MainActivity.class);
    loggedInActivity.putExtra("user_name",    dataMap.get("user_name"));
    loggedInActivity.putExtra("user_id",      dataMap.get("user_id"));
    loggedInActivity.putExtra("image_url",    dataMap.get("image_url"));
    loggedInActivity.putExtra("origin",       dataMap.get("origin"));
    loggedInActivity.putExtra("user_email",   dataMap.get("user_email"));
    this.getActivity().finish();
    getActivity().overridePendingTransition(R.anim.fadein, R.anim.enter_from_right);
    startActivity(loggedInActivity);
  }

  private void connectGoogleAPI()
  {
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    if ((mGoogleApiClient == null) || (!mGoogleApiClient.isConnected()))
      mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).enableAutoManage(getActivity(), this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
  }

  public void logMessage(String msg)
  {
    if (Shared.DEBUG_MODE) Log.i(TAG, msg);
  }
}