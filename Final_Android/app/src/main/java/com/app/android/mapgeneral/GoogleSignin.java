package com.app.android.mapgeneral;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.util.ThreadUtil;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.android.mapgeneral.Activities.MainActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


import org.json.JSONObject;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by malz on 8/6/16.
 */
public class GoogleSignin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    SignInButton signInButton;
    Button signOutButton;
    TextView statusTextView;
    ImageView imageSignIn;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG="SignInActivity";
    private static final int RC_SIGN_IN=9001;
    GoogleSignInAccount acct;

    /* The login button for Facebook */
    private LoginButton mFacebookLoginButton;
    /* The callback manager for Facebook */
    private CallbackManager mFacebookCallbackManager;
    /* Used to track user logging in/out off Facebook */
    private AccessTokenTracker mFacebookAccessTokenTracker;

    /* A reference to the Firebase */
    private Firebase mFirebaseRef;
    /* Data from the authenticated user */
    private AuthData mAuthData;
    private ProgressDialog mAuthProgressDialog;

    Context cont;
    String firebase_url="https://zolhi-90cea.firebaseio.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                Firebase.setAndroidContext(this);
        mFirebaseRef=new Firebase(firebase_url);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.fragment_signin_social);
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestEmail().build();
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        statusTextView=(TextView)findViewById(R.id.googleSigninStatus);
        signInButton=(SignInButton)findViewById(R.id.sign_in_google);
        signOutButton=(Button)findViewById(R.id.signOutGoogle);
        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        Log.i(TAG,"OnCreate: Done");

         /* *************************************
         *              FACEBOOK               *
         ***************************************/
        /* Load the Facebook login button and set up the tracker to monitor access token changes */
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginButton = (LoginButton) findViewById(R.id.login_with_facebook);
        mFacebookLoginButton.setReadPermissions("email");
        mFacebookLoginButton.setReadPermissions("user_friends");

        mFacebookLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            Log.i("Facebook","Facebook is success");
                Bundle params = new Bundle();
                params.putString("fields", "id,email,gender,cover,picture.type(large)");
                new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                if (response != null) {
                                    try {
                                        JSONObject data = response.getJSONObject();
                                        if (data.has("picture")) {
                                            String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                            Log.i("Facebok Image","Image URL is"+data);
                                            // set profile image to imageview using Picasso or Native methods
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).executeAsync();
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                            /* handle the result */
                                if(response!=null)
                                {
                                    try
                                    {
                                        JSONObject data = response.getJSONObject();
                                        Log.i("Facebok Image","Image URL is"+data);

                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                ).executeAsync();


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }


        });
        new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        AccessToken accessToken = AccessToken.getCurrentAccessToken();


      /*  mFacebookAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.i(TAG, "Facebook.AccessTokenTracker.OnCurrentAccessTokenChanged");
                GoogleSignin.this.onFacebookAccessTokenChange(currentAccessToken);
            }

        };*/



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFacebookAccessTokenTracker.stopTracking();
    }
    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.hide();
            Log.i(TAG, provider + " auth successful");
            Map<String,Object> userData=authData.getProviderData();
            Log.i("Facebook User:",authData.getProviderData().get("id").toString());
            //userData.
            //setAuthenticatedUser(authData);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            //mAuthProgressDialog.hide();
            Log.i(TAG, provider + " auth Error"+firebaseError.toString());
            //showErrorDialog(firebaseError.toString());
        }
    }
    /* ************************************
     *             FACEBOOK               *
     **************************************
     */
   /* private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            Log.i("FACEBOOK:","onFacebookAccessTokenChange: Success");
            //mAuthProgressDialog.show();
            mFirebaseRef.authWithOAuthToken("facebook", token.getToken(), new AuthResultHandler("facebook"));

        } else {
            // Logged out of Facebook and currently authenticated with Firebase using Facebook, so do a logout
            if (this.mAuthData != null && this.mAuthData.getProvider().equals("facebook")) {
                Log.i("FACEBOOK:","onFacebookAccessTokenChange:Not success");
                mFirebaseRef.unauth();
               // setAuthenticatedUser(null);
            }
        }
    }*/

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.sign_in_google:
                Log.i(TAG,"You clicked sign in");
                signIn();
                break;
            case R.id.signOutGoogle:
                Log.i(TAG,"You clicked sign in");
                signOut();

                break;


        }
    }
    public void signIn()
    {
        Intent signInIntnet=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        Log.i(TAG,"Sign in called, intenet is:"+signInIntnet.getType());
        startActivityForResult(signInIntnet,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"OnActivity Result Requested code is:"+requestCode);
        if(requestCode==RC_SIGN_IN)
        {
            Log.i(TAG,"Requested code is RCSIGNIN");
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.i(TAG,"Result is:"+result.isSuccess());
            handleSigninResult(result);
            Intent main_act=new Intent(getBaseContext(), MainActivity.class);
            main_act.putExtra("user_name",acct.getDisplayName());
            if(acct.getPhotoUrl()!=null)
            main_act.putExtra("image_url",acct.getPhotoUrl().toString());
            main_act.putExtra("user_email",acct.getEmail());
            main_act.putExtra("user_id",acct.getId());
            startActivity(main_act);
        }
        else
        {
            Log.i("OnActivityResult", "OnActivity REsult Faceook");
  //          Intent main_act=new Intent(getBaseContext(), MainActivity.class);
//            main_act.putExtra("user_name",acct.getDisplayName());

            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }
    public void handleSigninResult(GoogleSignInResult result)
    {
        Log.d("TAG","handlesignInResult:"+result.isSuccess());
        if(result.isSuccess())
        {
            acct=result.getSignInAccount();

            statusTextView.setText("Hello, "+acct.getDisplayName());
            if(acct.getPhotoUrl()!=null)
            {
                Log.i(TAG,"Photo Url is: "+acct.getPhotoUrl());
                new DownloadImageTask((ImageView) findViewById(R.id.signinImage))
                        .execute(acct.getPhotoUrl().toString());

            }
            else
                Log.i(TAG,"No photo url");

        }
        else
        {

        }

    }

    public void signOut()
    {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                statusTextView.setText("Sign Out -:");
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG,"Connected failed function"+connectionResult);
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}

/*{"id":"10153829420708108",
"email":"malapatiravi@gmail.com",
"gender":"male",
"cover":{"id":"10153245973053108","offset_y":37,"source":"https:\/\/scontent.xx.fbcdn.net\/v\/t1.0-9\/12043039_10153245973053108_1081656944199513810_n.jpg?oh=1fdc5388017ef08e6f4e191c49c4d80c&oe=581DF8BE"},
"picture":{"data":{"is_silhouette":false,"url":"https:\/\/scontent.xx.fbcdn.net\/v\/t1.0-1\/p200x200\/12963649_10153630271898108_2001461956329561346_n.jpg?oh=f8c18bb4dfa11062e7d7763a2d87646d&oe=5823C675"}}
}*/