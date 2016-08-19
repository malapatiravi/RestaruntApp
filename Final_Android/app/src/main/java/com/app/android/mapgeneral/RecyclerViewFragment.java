package com.app.android.mapgeneral;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.app.android.mapgeneral.Objects.RestaurantDataLocalJSON;

import java.util.HashMap;

//import android.support.v7.view.ActionMode;

/**
 * Created by Charles on 6/13/2015.
 */
public class RecyclerViewFragment extends Fragment
{
  private static final String ARG_OPTION = "option";
  public static RecyclerViewFragment newInstance(int sectionNumber)
  {
    RecyclerViewFragment fragment = new RecyclerViewFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_OPTION, sectionNumber);
    fragment.setArguments(args);
    return fragment;
  }

  public RecyclerViewFragment() {}

  RestaurantDataLocalJSON movieData;
  private LruCache mImgMemoryCache;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    setRetainInstance(true);
    movieData = new RestaurantDataLocalJSON();
    movieData.loadLocalMovieDataJson(getActivity());
  }
  private RecyclerView recyclerView;
  private MyRecyclerViewAdapter mRecyclerViewAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
    getActivity().setTitle("Movie list");
    recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
    recyclerView.setHasFixedSize(true);
    int option = getArguments().getInt(ARG_OPTION);
    RecyclerView.LayoutManager mLayoutManager;
    switch (option)
    {
      case 0:
        mLayoutManager = new LinearLayoutManager(getActivity());
        break;
      case 1:
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        break;
      default:
        mLayoutManager = new LinearLayoutManager(getActivity());
        break;
    }

    recyclerView.setLayoutManager(mLayoutManager);
    mRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), movieData.getMoviesList(), mImgMemoryCache, option);
    recyclerView.setAdapter(mRecyclerViewAdapter);
    //set listener
    mRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener()
    {
      @Override
      public void onItemClick(View v, int position)
      {
        HashMap<String, ?> movie = (HashMap<String, ?>) movieData.getItem(position);
        if (getActivity().findViewById(R.id.container) != null)
        {
          mListener.onListItemSelected(position, movie, 0);//option=0(framelayout:container),1(item_container)
        }
      }

      @Override
      public void onItemLongClick(View v, int position)
      {
        getActivity().startActionMode(new ActionBarCallBack(position));
      }

      @Override
      public void onOverflowMenuClick(View v, final int position)
      {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
          @Override
          public boolean onMenuItemClick(MenuItem item)
          {
            switch (item.getItemId())
            {
              case R.id.item_duplicate:
                mRecyclerViewAdapter.duplicateItem(position);
                mRecyclerViewAdapter.notifyItemInserted(position + 1);
                return true;
              case R.id.item_delete:
                mRecyclerViewAdapter.removeItem(position);
                mRecyclerViewAdapter.notifyItemRemoved(position);
                return true;
              default:
                return false;
            }
          }
        });
        inflater.inflate(R.menu.menu_cab, popup.getMenu());
        popup.show();
      }
    });

    return rootView;
  }

  // ActionMode mActionMode;
  class ActionBarCallBack implements ActionMode.Callback
  {
    int position;

    public ActionBarCallBack(int position) {this.position = position;}

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
    {
      int id = item.getItemId();
      switch (id)
      {
        case R.id.item_delete:
          mRecyclerViewAdapter.removeItem(position);
          mRecyclerViewAdapter.notifyItemRemoved(position);
          mode.finish();
          break;
        case R.id.item_duplicate:
          mRecyclerViewAdapter.duplicateItem(position);
          mRecyclerViewAdapter.notifyItemInserted(position + 1);
          mode.finish();
          break;
        default:
          break;
      }
      return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu)
    {
      mode.getMenuInflater().inflate(R.menu.menu_cab, menu);
      return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {}

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
    {
      HashMap hm = (HashMap) movieData.getItem(position);
      mode.setTitle(hm.get("name").toString());
      return false;
    }
  }

  @Override
  public void onStop()
  {
    super.onStop();
    mRecyclerViewAdapter.releaseAllLoader();
  }

  private OnListItemSelectedListener mListener;

  public interface OnListItemSelectedListener
  {
    void onListItemSelected(int position, HashMap<String, ?> movie, int option);
  }

  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);
    try
    {
      mListener = (OnListItemSelectedListener) context;
    }
    catch (ClassCastException e)
    {
      throw new ClassCastException(context.toString()
                                           + " must implement OnFragmentInteractionListener.");
    }
  }
}
