package com.app.android.mapgeneral.Fragments;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.app.android.mapgeneral.Objects.RestaurantDataJSON;
import com.app.android.mapgeneral.Objects.RestaurantListDownloader;
import com.app.android.mapgeneral.R;
import com.app.android.mapgeneral.Objects.RestaurantListAdapter;
import com.app.android.mapgeneral.Objects.Shared;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class RestaurantListFragment extends Fragment
{
  private static final String TAG = "RestaurantListFrag";
  private String restaurantDetail = "https://api.eatstreet.com/publicapi/v1/restaurant/";
  private String mURL = null;
  private RecyclerView mRecyclerView;
  private LinearLayoutManager mLayoutManager;
  private RestaurantListAdapter mRecyclerViewAdapter;
  static RestaurantDataJSON restaurantData;
  private LruCache<String, Bitmap> mImgCache;
  private static final String ARG_URL = "url";
  public static RestaurantListFragment newInstance(String url)
  {
    RestaurantListFragment fragment = new RestaurantListFragment();
    Bundle args = new Bundle();
    restaurantData = new RestaurantDataJSON();
    args.putString(ARG_URL, url);
    fragment.setArguments(args);
    return fragment;
  }

  public RestaurantListFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) mURL = getArguments().getString(ARG_URL);
    if (mImgCache == null)
    {
      final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
      final int cacheSize = maxMemory / 8;
      mImgCache = new LruCache<String, Bitmap>(cacheSize)
      {
        @Override
        protected int sizeOf(String key, Bitmap bitmap)
        {
          return bitmap.getByteCount() / 1024;
        }
      };
    }
    setRetainInstance(true);
    setHasOptionsMenu(true);
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
    final View rootView = inflater.inflate(R.layout.activity_recyclerview, container, false);
    final RestaurantSelectListener mListener;
    try                             {mListener = (RestaurantSelectListener) getContext();}
    catch (ClassCastException e)    {throw new ClassCastException("The hosting activity of the Fragment forgot to implement OnFragmentInteractionListener");}

    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerViewAdapter = new RestaurantListAdapter(getActivity(), restaurantData.getRestaurantList(), mImgCache);

    RestaurantListDownloader restaurantList = new RestaurantListDownloader(mRecyclerViewAdapter, restaurantData);
    restaurantList.execute(mURL);

    mRecyclerView.setAdapter(mRecyclerViewAdapter);

    mRecyclerViewAdapter.SetOnItemClickListener(new RestaurantListAdapter.OnItemClickListener()
    {
      @Override
      public void onItemClick(View v, int position, ImageView vIcon)
      {
        HashMap<String, ?> movie = (HashMap<String, ?>) restaurantData.getItem(position);
        String id = (String) movie.get("id");
        String url = restaurantDetail + id;
        logMessage("URl is:" + url);
        RestaurantInfoDownloader task = new RestaurantInfoDownloader(mListener, position, vIcon);
        task.execute(url);
      }
      @Override
      public void onItemLongClick(View v, int position)
      {
      }
    });
    //defaultAnimation();
    itemAnimation();
    adapterAnimation();
    return rootView;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
  {
    SearchView search = null;
    inflater.inflate(R.menu.recycler_fragment_menu, menu);
    MenuItem menuItem = menu.findItem(R.id.action_search);
    if (menuItem != null) search = (SearchView) menuItem.getActionView();
    if (search != null)
    {
      search.setOnQueryTextListener(new SearchView.OnQueryTextListener()
      {
        @Override
        public boolean onQueryTextSubmit(String query)
        {
          int position = restaurantData.findFirst(query);
          if (position >= 0) mRecyclerView.scrollToPosition(position);
          return true;
        }

        @Override
        public boolean onQueryTextChange(String newText)
        {
          return false;
        }
      });
    }
    super.onCreateOptionsMenu(menu, inflater);
  }


  private class RestaurantInfoDownloader extends AsyncTask<String, Void, HashMap>
  {
    private final int position;
    private final WeakReference<RestaurantSelectListener> mAsyncListener;
    private final WeakReference<ImageView> vIconImage;
    public RestaurantInfoDownloader(RestaurantSelectListener myListener, int pos, ImageView vIcon)
    {
      position = pos;
      mAsyncListener = new WeakReference<>(myListener);
      vIconImage = new WeakReference<>(vIcon);
    }

    @Override
    protected HashMap doInBackground(String... urls)
    {
      HashMap map = new HashMap();
      RestaurantDataJSON movieDataJson = new RestaurantDataJSON();
      for (String url : urls) map = movieDataJson.downloadRestaurantDetailJSON(url);
      return map;
    }

    @Override
    protected void onPostExecute(HashMap map)
    {
      final RestaurantSelectListener listener = mAsyncListener.get();
      final ImageView vIcon = vIconImage.get();
      if (listener != null) listener.onRestaurantSelected(position, map, vIcon);
    }
  }

  public interface RestaurantSelectListener
  {
    void onRestaurantSelected(int id, HashMap<String, ?> movie, ImageView vIcon);
  }

  public void logMessage(String msg)
  {
    if (Shared.DEBUG_MODE) Log.i(TAG, msg);
  }
}
