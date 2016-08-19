package com.app.android.mapgeneral.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.android.mapgeneral.R;
import com.app.android.mapgeneral.RegisterMe;
import com.app.android.mapgeneral.SignInEat;

import github.chenupt.springindicator.SpringIndicator;

/**
 * Created by suzzett on 8/15/16.
 */
public class OnePaneFragment extends Fragment
{
  private MyFragmentPagerAdapter myPagerAdapter;
  private ViewPager mViewPager;
  public void OnePaneFragment() {}

  public static OnePaneFragment newInstance()
  {
    Bundle args = new Bundle();
    OnePaneFragment mFragment = new OnePaneFragment();
    mFragment.setArguments(args);
    return mFragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle bundle)
  {
    View rootView = inflater.inflate(R.layout.fragment_one_pane_layout, vGroup, false);
    SpringIndicator springIndicator = (SpringIndicator) rootView.findViewById(R.id.indicator);
    myPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), 3);
    mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
    mViewPager.setAdapter(myPagerAdapter);
    mViewPager.setCurrentItem(0);
    springIndicator.setViewPager(mViewPager);

    Log.i("at main view Pager", "At main view pager");
    mViewPager.setPageTransformer(false, new ViewPager.PageTransformer()
    {
      @Override
      public void transformPage(View page, float position)
      {
        final float normalized_position = Math.abs(Math.abs(position) - 1);
        page.setScaleX(normalized_position / 2 + 0.5f);
        page.setRotation(position * -30);
        page.setScaleY(normalized_position / 2 + 0.5f);

      }
    });
    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
    {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
      {

      }

      @Override
      public void onPageSelected(int position)
      {

      }

      @Override
      public void onPageScrollStateChanged(int state)
      {

      }
    });
    return rootView;
  }


}

class MyFragmentPagerAdapter extends FragmentPagerAdapter
{
  int count;

  public MyFragmentPagerAdapter(FragmentManager fm, int size)
  {
    super(fm);
    Log.i("TAG", "PagerAdapter initialized");
    count = size;
  }

  @Override
  public Fragment getItem(int position)
  {
    Log.i("TAG", "getting item at: "+position);
    switch (position)
    {
      case 0:
        return GoogleSignInFragment.newInstance(position, "Social SignIn1");
      case 1:
        return SignInEat.newInstance(position, "Social SignIn2");
      case 2:
        return RegisterMe.newInstance(position, "Social SignIn");
    }
    return null;
  }

  @Override
  public int getCount()
  {
    return count;
  }

  @Override
  public CharSequence getPageTitle(int position)
  {
    switch (position)
    {
      case 0:
        return "Social Login";
      case 1:
        return "Sign In";
      case 2:
        return "Register Me";
    }
    return "Sign In";
  }
}
