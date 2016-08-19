package com.app.android.mapgeneral;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.app.android.mapgeneral.Objects.Shared;

import java.util.HashMap;

public class RestaurantDetails extends Fragment
{
  private final static String TAG = "RestaurantDetails";
  private final String BUNDLE_FRAGMENT_TAG = "currentFragment";
  public static final String RESTAURANT_TAG = "restaurant";
  private Fragment currentFragment;
  private int fragmentContainer;

  public RestaurantDetails() {}
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }

  public static RestaurantDetails newInstance(HashMap<String, ?> restaurant)
  {
    RestaurantDetails currentInstance = new RestaurantDetails();
    Bundle args = new Bundle();
    args.putSerializable(RESTAURANT_TAG, restaurant);
    currentInstance.setArguments(args);
    return currentInstance;
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup v, Bundle savedInstanceState)
  {
    View rootView = inflater.inflate(R.layout.fragment_restaurant_detail, v);
    //setContentView(R.layout.fragment_restaurant_detail);
    //Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_rest_detail);
    //setSupportActionBar(mToolbar);

    fragmentContainer = R.id.container_rest_detail;
    HashMap<String, ?> restaurant = (HashMap) getArguments().getSerializable(RESTAURANT_TAG);
    logMessage("Passing Restarunt Detail View Image " + restaurant.get("name"));
    //currentFragment = ((savedInstanceState == null))? RestaurantDetailsFragment.newInstance(restaurant):getSupportFragmentManager().getFragment(savedInstanceState, BUNDLE_FRAGMENT_TAG);
//    CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
//    collapsingToolbar.setTitle((String) restaurant.get("name"));
    return rootView;
  }

  public void logMessage(String msg) {if (Shared.DEBUG_MODE) Log.i(TAG, msg);}
}
