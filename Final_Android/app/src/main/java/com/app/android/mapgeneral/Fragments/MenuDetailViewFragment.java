package com.app.android.mapgeneral.Fragments;

//import android.os.AsyncTask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.app.android.mapgeneral.Objects.Shared;
import com.app.android.mapgeneral.R;
import com.app.android.mapgeneral.RestaurantMenuAdapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class MenuDetailViewFragment extends Fragment implements RestaurantMenuAdapter.CheckChangedListener
{
  private static final String TAG = "MenuDetailViewFragment";
  private static final String ARG_MENU = "menu";
  private List<HashMap<String, String>> menu;
  private MenuDetailViewFragment context;
  private CheckChangedListener checkListener;
  RecyclerView mRecyclerView;
  LinearLayoutManager mLayoutManager;
  RestaurantMenuAdapter mRecyclerViewAdapter;

  public static MenuDetailViewFragment newInstance(List<HashMap<String, String>> value)
  {
    MenuDetailViewFragment fragment = new MenuDetailViewFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_MENU, (Serializable) value);
    fragment.setArguments(args);
    return fragment;
  }

  public MenuDetailViewFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    context = this;
    if (getArguments() != null)
    {
      menu = (List<HashMap<String, String>>) getArguments().getSerializable(ARG_MENU);
    }
    setRetainInstance(true);
  }

  private void itemAnimation()
  {
    SlideInLeftAnimator animator = new SlideInLeftAnimator();
    animator.setInterpolator(new OvershootInterpolator());
    animator.setAddDuration(1000);
    animator.setRemoveDuration(500);
    mRecyclerView.setItemAnimator(animator);
  }

  private void adapterAnimation()
  {
    AlphaInAnimationAdapter animator = new AlphaInAnimationAdapter(mRecyclerViewAdapter);
    ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(animator);
    mRecyclerView.setAdapter(scaleInAnimationAdapter);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    try {checkListener = (CheckChangedListener) getActivity();}
    catch (ClassCastException e)
    {
      logMessage("Parent is not implementing onCheckChanged in activity.");
      e.printStackTrace();
    }
    View rootView = inflater.inflate(R.layout.menu_fragment, container, false);
    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardListMenu);
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerViewAdapter = new RestaurantMenuAdapter(menu, context);
    mRecyclerView.setAdapter(mRecyclerViewAdapter);
    itemAnimation();
    adapterAnimation();
    return rootView;
  }
  public interface CheckChangedListener
  {
    void onCheckChanged(boolean isChecked, String apiKey);
  }

  @Override
  public void onCheckChanged(boolean isChecked, String apiKey)
  {
    checkListener.onCheckChanged(isChecked, apiKey);
  }

  public void logMessage(String msg) {if (Shared.DEBUG_MODE) Log.i(TAG, msg);}

}
