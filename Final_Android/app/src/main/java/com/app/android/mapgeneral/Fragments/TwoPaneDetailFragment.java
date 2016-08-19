package com.app.android.mapgeneral.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.android.mapgeneral.R;

/**
 * Created by suzzett on 8/15/16.
 */
public class TwoPaneDetailFragment extends Fragment
{
  public static final String POSITION_TAG = "position";
  public static final int POSITION_SOCIAL = 0;
  public static final int POSITION_LOGIN = 1;
  public static final int POSITION_REGISTER = 2;
  public TwoPaneDetailFragment(){}

  public static TwoPaneDetailFragment newInstance(int position)
  {
    TwoPaneDetailFragment mFragment = new TwoPaneDetailFragment();
    Bundle args = new Bundle();
    args.putInt(POSITION_TAG, position);
    mFragment.setArguments(args);
    return mFragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle savedInstanceState)
  {
    View rootView = null;
    int position = getArguments().getInt(POSITION_TAG);
    switch(position)
    {
      case(POSITION_SOCIAL):
        rootView = inflater.inflate(R.layout.fragment_signin_social, vGroup, false);
        break;
      case(POSITION_LOGIN):
        rootView = inflater.inflate(R.layout.fragment_eatsignin, vGroup, false);
        break;
      case(POSITION_REGISTER):
        rootView = inflater.inflate(R.layout.fragment_register_me, vGroup, false);
        break;
    }
    return rootView;
  }

}
