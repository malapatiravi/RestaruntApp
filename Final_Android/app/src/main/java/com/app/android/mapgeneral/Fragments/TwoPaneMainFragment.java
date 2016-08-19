package com.app.android.mapgeneral.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.android.mapgeneral.R;

/**
 * Created by suzzett on 8/15/16.
 */
public class TwoPaneMainFragment extends Fragment
{
  private ButtonClickedListener mListener;
  public TwoPaneMainFragment(){}

  public static TwoPaneMainFragment newInstance()
  {
    TwoPaneMainFragment mFragment = new TwoPaneMainFragment();
    Bundle args = new Bundle();
    mFragment.setArguments(args);
    return mFragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle savedInstanceState)
  {
    View rootView = inflater.inflate(R.layout.fragment_two_pane_login_main, vGroup, false);
    try {mListener = (ButtonClickedListener) getActivity();}
    catch(ClassCastException e) {e.printStackTrace();}

    Button socialBtn = (Button) rootView.findViewById(R.id.two_pane_social_login);
    Button loginBtn = (Button) rootView.findViewById(R.id.two_pane_login);
    Button registerBtn = (Button) rootView.findViewById(R.id.two_pane_register_me_btn);
    socialBtn.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        mListener.onButtonClicked(TwoPaneDetailFragment.POSITION_SOCIAL);
      }
    });
    loginBtn.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        mListener.onButtonClicked(TwoPaneDetailFragment.POSITION_LOGIN);
      }
    });
    registerBtn.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        mListener.onButtonClicked(TwoPaneDetailFragment.POSITION_REGISTER);
      }
    });
    return rootView;
  }

  public interface ButtonClickedListener
  {
    void onButtonClicked(int position);
  }
}
