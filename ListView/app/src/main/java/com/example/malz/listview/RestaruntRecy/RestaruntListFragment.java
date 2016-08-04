package com.example.malz.listview.RestaruntRecy;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.example.malz.listview.OnListItemSelectedListener;
import com.example.malz.listview.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by malz on 8/2/16.
 */
public class RestaruntListFragment extends Fragment {

    private String restaruntDetail="https://api.eatstreet.com/publicapi/v1/restaurant/";
    private String mURL = null;

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MyRecyclerViewAdapter mRecyclerViewAdapter;
    static RestaruntDataJSon movieData;
    private LruCache<String, Bitmap> mImgCache;
    private static final String ARG_URL = "url";

    public static RestaruntListFragment newInstance(String url)
    {
        RestaruntListFragment fragment = new RestaruntListFragment();
        Bundle args = new Bundle();
        movieData = new RestaruntDataJSon();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }
    public RestaruntListFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mURL = getArguments().getString(ARG_URL);
        }
        if (mImgCache == null)
        {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            mImgCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    private void itemAnimation() {
        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        animator.setInterpolator(new OvershootInterpolator());
        animator.setAddDuration(1000);
        animator.setRemoveDuration(500);
        mRecyclerView.setItemAnimator(animator);
    }
    private void adapterAnimation() {
        AlphaInAnimationAdapter animator = new AlphaInAnimationAdapter(mRecyclerViewAdapter);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(animator);
        mRecyclerView.setAdapter(scaleInAnimationAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        final View rootView=inflater.inflate(R.layout.fragment_recylcerview,container, false);
        //final onL
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        SearchView search = null;
        inflater.inflate(R.menu.manu_recycler, menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        if (menuItem != null)
            search = (SearchView) menuItem.getActionView();
        if (search != null) {
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    int position = movieData.findFirst(query);
                    if (position >= 0)
                        mRecyclerView.scrollToPosition(position);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);

    }
    private class MyDownloadRestaurant extends AsyncTask<String, Void, HashMap>
    {
        private final int position;
        private final WeakReference<OnListItemSelectedListener> mAsyncListener;
        public MyDownloadRestaurant(OnListItemSelectedListener myListener, int pos)
        {
            position = pos;
            mAsyncListener = new WeakReference<>(myListener);
        }

        @Override
        protected HashMap doInBackground(String... urls) {
            HashMap map = new HashMap();
            RestaruntDataJSon movieDataJson = new RestaruntDataJSon();
            for (String url : urls)
            {
                map = movieDataJson.downloadRestaurantDetailJSON(url);
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap map)
        {
            final OnListItemSelectedListener listener = mAsyncListener.get();
            if (listener != null)
            {
                listener.onListItemSelected(position, map);
            }
        }
    }


}
