package com.example.malz.listview.RecyOne;

import android.os.AsyncTask;
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

import com.example.malz.listview.Movie;
import com.example.malz.listview.MovieData;
import com.example.malz.listview.RestaruntRecy.MyRecyclerViewAdapter;
import com.example.malz.listview.R;
import com.firebase.client.Firebase;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by malz on 6/8/16.
 */

public class RecyclerViewFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyRecyclerViewAdapter mRecyclerViewAdapter;
    MyFirebaseRecylerAdapter mFirebaseRecylerAdapter;
    MovieData movieData = new MovieData();
    MovieDataJSon movieDataJSon=null;
    private detailInter mListener;
    Firebase ref;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Firebase.setAndroidContext(getContext());
        ref = new Firebase("https://zolhi-90cea.firebaseio.com/moviedata/");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        movieDataJSon=new MovieDataJSon();
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
                    MyDownloadJsonAsyncTask downloadJSon=new MyDownloadJsonAsyncTask(mRecyclerViewAdapter);
                    String url=movieDataJSon.PHP_SERVER+"movies/rating/"+queryText;
                    //Log.d("www.malz.com",url);
                    downloadJSon.execute(url);
                  /*  int position=movieData.findFirst(queryText);
                    if(position>=0)
                    {
                        mRecyclerView.smoothScrollToPosition(position);
                    }*/
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
        AlphaInAnimationAdapter alphaAdapter=new AlphaInAnimationAdapter(mFirebaseRecylerAdapter);
        SlideInBottomAnimationAdapter slide=new SlideInBottomAnimationAdapter(alphaAdapter);
        ScaleInAnimationAdapter scaleAplhaAdap=new ScaleInAnimationAdapter(alphaAdapter);
        mRecyclerView.setAdapter(slide);
    }
    private HashMap createMap(Movie movieObj)
    {
        HashMap movie = new HashMap();
        movie.put("image",movieObj.getImage());
        movie.put("name", movieObj.getName());
        movie.put("description", movieObj.getDescription());
        movie.put("year", movieObj.getYear());
        movie.put("length",movieObj.getLength());
        movie.put("rating",movieObj.getRating());
        movie.put("director",movieObj.getDirector());
        movie.put("stars",movieObj.getStars());
        movie.put("url",movieObj.getUrl());
        return movie;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.fragment_recylcerview,container, false);

        mRecyclerView=(RecyclerView) rootView.findViewById(R.id.cardList);


        mListener=(detailInter)getContext();

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        //mRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), movieDataJSon.getMoviesList());
        mFirebaseRecylerAdapter = new MyFirebaseRecylerAdapter(Movie.class, R.layout.cardview_main,
                MyFirebaseRecylerAdapter.MovieViewHolder.class, ref, getActivity());

        mRecyclerView.setAdapter(mFirebaseRecylerAdapter);

        //MyDownloadJsonAsyncTask downloadJSon=new MyDownloadJsonAsyncTask(mRecyclerViewAdapter);
        //String url=movieDataJSon.PHP_SERVER+"movies/";
        //Log.d("www.malz.com",url);
        //downloadJSon.execute(url);
        //mRecyclerView.setAdapter(mRecyclerViewAdapter);


        //downloadJSon.execute(url);

     /*   mRecyclerViewAdapter.setOnClickListenerAdapter(new MyRecyclerViewAdapter.OnItemClickListener1(){
            @Override
            public void onItemClick(View view, int position) {
                //HashMap<String, ?> movie=(HashMap<String,?>) movieDataJSon.getItem(position);
                Movie movie = mFirebaseRecylerAdapter.getItem(position);
                HashMap movieMap = createMap(movie);

                //String url = movieDataJSon.PHP_SERVER+"movies/id/"+(String)movie.get("id");
                //Log.i("Success",url);
                mListener.onDetailsSelect(position, movieMap);
                //MyDownloadDetailAsyncTask download=new MyDownloadDetailAsyncTask(mListener);
                //download.execute(url);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                getActivity().startActionMode(new ActionBarCallBack(position,movieData,mRecyclerViewAdapter));
                //Log.i("Cloning","Clone");
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
                                //movieData.getMoviesList().add(position+1, (HashMap) ((HashMap) movieData.getItem(position)).clone());
                                //movieDataJSon.addItem(position,movieDataJSon.getItem(position));
                                //mRecyclerViewAdapter.notifyItemInserted(position+1);
                                //return true;
                                Movie cloud = mFirebaseRecylerAdapter.getItem(position);
                                cloud.setName(cloud.getName() + "_new");
                                cloud.setId(cloud.getId() + "_new");
                                ref.child(cloud.getId()).setValue(cloud);
                                return true;
                            case R.id.delete_card:
                                //movieData.getMoviesList().remove(position);
                                //movieDataJSon.deleteItem(position,movieDataJSon.getItem(position));
                                //mRecyclerViewAdapter.notifyItemRemoved(position);
                                Movie cloudDelete = mFirebaseRecylerAdapter.getItem(position);
                                ref.child(cloudDelete.getId()).removeValue();
                                //return true;
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
        }); */
        mFirebaseRecylerAdapter.SetOnItemClickListener(new MyFirebaseRecylerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                Movie movie = mFirebaseRecylerAdapter.getItem(position);
                HashMap movieMap = createMap(movie);
                mListener.onDetailsSelect(position, movieMap);
            }

            @Override
            public void onOverflowMenuClick(View v, final int position) {
                final PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.copy_card:
                                Movie cloud = mFirebaseRecylerAdapter.getItem(position);
                                cloud.setName(cloud.getName() + "_new");
                                cloud.setId(cloud.getId() + "_new");
                                ref.child(cloud.getId()).setValue(cloud);
                                return true;
                            case R.id.delete_card:
                                Movie cloudDelete = mFirebaseRecylerAdapter.getItem(position);
                                ref.child(cloudDelete.getId()).removeValue();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_menu, popup.getMenu());
                popup.show();
            }
        });
        //defaultAnimation();;
        itemAnimation();
        adapterAnimation();

        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
    private class MyDownloadJsonAsyncTask extends AsyncTask<String, Void, MovieDataJSon> {
        private final WeakReference<MyRecyclerViewAdapter> adapterReference;
        public MyDownloadJsonAsyncTask(MyRecyclerViewAdapter adapter)
        {
            adapterReference = new WeakReference<MyRecyclerViewAdapter>(adapter);
        }
        @Override
        protected MovieDataJSon doInBackground(String... urls) {
            MovieDataJSon threadMovieData=new MovieDataJSon();
            for(String url:urls)
            {
                //Log.d("Inside do in back", url);
                threadMovieData.downloadMovieDataJson(url);
            }
            return threadMovieData;
        }

        @Override
        protected void onPostExecute(MovieDataJSon threadMovieData) {
            movieDataJSon.moviesList.clear();
            for(int i=0;i<threadMovieData.getSize();i++)
            {
                Integer x=i;
               // Log.d("On post Execute",x.toString());
                movieDataJSon.moviesList.add(threadMovieData.moviesList.get(i));
            }
            if(adapterReference != null)
            {
                final MyRecyclerViewAdapter adapter=adapterReference.get();
                if(adapter!=null)
                {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
    private class MyDownloadDetailAsyncTask extends AsyncTask<String, Void, HashMap>{

        private final WeakReference<detailInter> weakListener;
        public MyDownloadDetailAsyncTask(detailInter listener)
        {
            weakListener=new WeakReference<detailInter>(listener);

        }

        @Override
        protected HashMap doInBackground(String... urls) {
            HashMap movie=new HashMap();
            for(String url:urls)
            {
                movie = movieDataJSon.getItemByUrl(url);
                //Log.d("Inside Do in back",(String)movie.get("name"));
            }

            return movie;
        }

        @Override
        protected void onPostExecute(HashMap movie) {

            if(weakListener!=null)
            {
                final detailInter detailsList=weakListener.get();
                if(detailsList!=null)
                {
                    detailsList.onDetailsSelect(2, movie);
                    Log.d("Inside detailList","Here"+(String)movie.get("name"));
                }
            }

            //super.onPostExecute(movie);
        }
    }

}



