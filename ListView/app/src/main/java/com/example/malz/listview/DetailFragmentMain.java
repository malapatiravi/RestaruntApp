package com.example.malz.listview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
//import android.widget.ShareActionProvider;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.malz.listview.RecyOne.MyDownloadImageAsyncTask;

import java.util.HashMap;

/**
 * Created by malz on 6/3/16.
 */
public class DetailFragmentMain extends Fragment {
    HashMap movie;
    HashMap movieNew;
    MovieData movieData = null;
    private ShareActionProvider mShare;
    public static final String ARG_MOVIE_ID="movieId";
    public static final String YEAR="year";
    public static final String DIRECTOR="director";
    public static final String STARS="stars";
    public static final String RATING="rating";
    public static final String DESC="description";
    public static final String IMGURL="imageUrl";
    public static final String NAME="name";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        movieData = new MovieData();

        View rootView;

        int position=getArguments().getInt(ARG_MOVIE_ID);
        movie=movieData.getItem(position);
        //movieNew=getArguments().get
        rootView = inflater.inflate(R.layout.fragment_detailmain, container, false);
        ImageView movieImage=(ImageView)rootView.findViewById(R.id.movieImage);
        MyDownloadImageAsyncTask imageAsyncTask=new MyDownloadImageAsyncTask(movieImage);
        imageAsyncTask.execute(getArguments().getString(IMGURL));

        //movieImage.setImageResource((Integer)movie.get("image"));

        TextView movieName=(TextView)rootView.findViewById(R.id.movieName);
        //movieName.setText((String)movie.get("name"));
        movieName.setText(getArguments().getString(NAME));

        TextView movieYear=(TextView)rootView.findViewById(R.id.movieYear);
        //movieYear.setText((String)movie.get("year"));
        movieYear.setText(getArguments().getString(YEAR));

        TextView movieDirector=(TextView)rootView.findViewById(R.id.movieDirector);
        //movieDirector.setText((String)movie.get("director"));
        movieDirector.setText(getArguments().getString(DIRECTOR));

        TextView movieCast=(TextView)rootView.findViewById(R.id.movieCast);
        //movieCast.setText((String)movie.get("stars"));
        movieCast.setText(getArguments().getString(STARS));
        float rate=(float)getArguments().getDouble("rating");
        RatingBar movieRating=(RatingBar) rootView.findViewById(R.id.movieRating);
        float rating1 = rate;
        movieRating.setStepSize(0.05f);

        movieRating.setRating(rating1);
        //Log.d("set detail","rating"+rating1);
        TextView movieDesc=(TextView) rootView.findViewById(R.id.movieDesc);
        //movieDesc.setText((String)movie.get("description"));
        movieDesc.setText(getArguments().getString(DESC));
        if(rating1>=8.5)
        rootView.setBackgroundColor(Color.GRAY);
        else
        rootView.setBackgroundColor(Color.LTGRAY);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu.findItem(R.id.share)==null)
        {
            menu.clear();
            inflater.inflate(R.menu.detail_menu, menu);

        }
        MenuItem shareItem=menu.findItem(R.id.share);
        mShare=(ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        Intent intentShare=new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT,(String)movie.get("name")+(String)movie.get("description"));
        mShare.setShareIntent(intentShare);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public static DetailFragmentMain newInstance(int movieId, HashMap movieData)
    {
        DetailFragmentMain frag=new DetailFragmentMain();
        Bundle args=new Bundle();
        args.putInt(ARG_MOVIE_ID,movieId);
        if(movieData!=null)
        {

            args.putString(IMGURL,(String)movieData.get("url"));
            args.putString(YEAR,(String) movieData.get("year"));
            args.putString(DIRECTOR,(String)movieData.get("director"));
            args.putString(STARS,(String)movieData.get("stars"));
            args.putDouble(RATING,(Double) movieData.get("rating"));
            args.putString(DESC,(String)movieData.get("description"));
            args.putString(NAME,(String)movieData.get("name"));
        }


        //movieNew=movieData;

        frag.setArguments(args);
        return frag;
    }


    public DetailFragmentMain()
    {

    }
}
