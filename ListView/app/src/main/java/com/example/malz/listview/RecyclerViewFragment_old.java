package com.example.malz.listview;

/**
 * Created by malz on 7/9/16.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.example.malz.listview.RestaruntRecy.MyRecyclerViewAdapter;

import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


public class RecyclerViewFragment_old extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyRecyclerViewAdapter mRecyclerViewAdapter;
    MovieData movieData = new MovieData();
    //MovieDataJSon movieData=null;
    private detailInter mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        //movieData=new MovieDataJSon();
    }

    public interface detailInter{
        public void onDetailsSelect(int position, HashMap<String, ?> movie);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null)
        {

        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);


        if(menu.findItem(R.id.test1)==null)
        {
            inflater.inflate(R.menu.manu_recycler, menu);
            Log.i("Search inside","inflate was necessary");
        }
        SearchView search=(SearchView) menu.findItem(R.id.search).getActionView();
        if(search!=null)
        {

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
                @Override
                public boolean onQueryTextSubmit(String queryText) {
                    int position=movieData.findFirst(queryText);
                    if(position>=0)
                    {
                        mRecyclerView.smoothScrollToPosition(position);
                    }
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
    public void SelectAll()
    {
        for(int position=0;position<movieData.getSize();position++)
        {
            HashMap movie=movieData.getItem(position);
            movie.put("selection", true);
            Log.i("Selected All","Select All"+position);
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    public void ClearAll()
    {
        for(int position=0;position<movieData.getSize();position++)
        {
            HashMap movie=movieData.getItem(position);
            movie.put("selection", false);
            Log.i("Selected All","Select All");
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }


    public void delete()
    {
        mRecyclerViewAdapter.delete();
        //mRecyclerViewAdapter.

        /*for(int position=0;position<movieData.getSize();position++)
        {

            HashMap movie=movieData.getItem(position);
            if((boolean)movie.get("selection")!=false)
            {
                movie.put("selection",false);
                mRecyclerViewAdapter.removeItem(position,movie);
                //movieData.moviesList.remove(position);//(position);
                //mRecyclerViewAdapter.notifyDataSetChanged();
                mRecyclerViewAdapter.notifyItemRemoved(position);
                Log.i("delete","delete"+movie.get("name"));
            }


            //mRecyclerViewAdapter.notifyDataSetChanged();
        }*/

    }
    public void search(){

     /*   SearchView search=null;
        MenuItem searchmenu=menu.findItem(R.id.search);
        if(menu.findItem(R.id.test1)==null)
        {
            inflater.inflate(R.menu.manu_recycler, menu);
        }
        if(searchmenu!=null)
        {
            search=(SearchView)searchmenu.getActionView();


        }
        if(search!=null)
        {
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
                @Override
                public boolean onQueryTextSubmit(String queryText) {
                    int position=movieData.findFirst(queryText);
                    if(position>=0)
                    {
                        mRecyclerView.scrollToPosition(position);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        }*/
    }

 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.clear_all:
                Log.i("Clear", "Clear All"+id);
                ClearAll();
                return true;
            case R.id.select_all:
                SelectAll();

                Log.i("Select","Select All"+id);
                return true;
            case R.id.delete:
                Log.i("Delete","Delete"+id);
                delete();
                return true;
            case R.id.search:
                //searc();

                Log.i("Search","Search");
                return false;


        }
    return false;
    }*/

    public void defaultAnimation()
    {
        DefaultItemAnimator animator=new DefaultItemAnimator();
        animator.setAddDuration(200);
        animator.setRemoveDuration(50);
        mRecyclerView.setItemAnimator(animator);
    }

    private void itemAnimation()
    {
        SlideInLeftAnimator animator=new SlideInLeftAnimator();
        animator.setInterpolator(new OvershootInterpolator());
        animator.setAddDuration(200);
        animator.setRemoveDuration(150);
        mRecyclerView.setItemAnimator(animator);
    }

    private void adapterAnimation()
    {
        AlphaInAnimationAdapter alphaAdapter=new AlphaInAnimationAdapter(mRecyclerViewAdapter);
        SlideInBottomAnimationAdapter slide=new SlideInBottomAnimationAdapter(alphaAdapter);
        ScaleInAnimationAdapter scaleAplhaAdap=new ScaleInAnimationAdapter(alphaAdapter);
        mRecyclerView.setAdapter(slide);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.fragment_recylcerview,container, false);

        mRecyclerView=(RecyclerView) rootView.findViewById(R.id.cardList);


        mListener=(detailInter)getContext();

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(),movieData.getMoviesList());

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mRecyclerViewAdapter.setOnClickListenerAdapter(new MyRecyclerViewAdapter.OnItemClickListener1(){
            @Override
            public void onItemClick(View view, int position) {
                HashMap<String, ?> movie=(HashMap<String,?>) movieData.getItem(position);

                Log.i("Success","Success");
                mListener.onDetailsSelect(position, movie);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                getActivity().startActionMode(new ActionBarCallBack(position,movieData,mRecyclerViewAdapter));
                Log.i("Cloning","Clone");
                //movieData.addItem(position+1, (HashMap)((HashMap)movieData.getItem(position)).clone());
                //mRecyclerViewAdapter.notifyItemInserted(position);
                //mRecyclerViewAdapter.notifyDataSetChanged();
            }

            public void onOverflowMenuClick(View v,final int position)
            {
                final PopupMenu pop=new PopupMenu(getActivity(),v);
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId())
                        {
                            case R.id.copy_card:
                                movieData.getMoviesList().add(position+1, (HashMap) ((HashMap) movieData.getItem(position)).clone());
                                mRecyclerViewAdapter.notifyItemInserted(position+1);
                                return true;
                            case R.id.delete_card:
                                movieData.getMoviesList().remove(position);
                                mRecyclerViewAdapter.notifyItemRemoved(position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                MenuInflater inflat=pop.getMenuInflater();
                inflat.inflate(R.menu.popup_menu,pop.getMenu());
                pop.show();
            }

        });
        //defaultAnimation();;
        itemAnimation();
        adapterAnimation();

        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}



