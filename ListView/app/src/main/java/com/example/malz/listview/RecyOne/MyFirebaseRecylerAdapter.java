package com.example.malz.listview.RecyOne;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.malz.listview.Movie;
import com.example.malz.listview.R;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

public class MyFirebaseRecylerAdapter extends FirebaseRecyclerAdapter<Movie,MyFirebaseRecylerAdapter.MovieViewHolder> {

    private Context mContext ;
    static OnItemClickListener myItemClickListener;


    public MyFirebaseRecylerAdapter(Class<Movie> modelClass, int modelLayout,
                                    Class<MovieViewHolder> holder, Query ref, Context context) {
        super(modelClass,modelLayout,holder,ref);
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(MovieViewHolder movieViewHolder, Movie movie, int i)
    {
        Log.i("Here :","Here"+movie.getUrl()+i);
        movieViewHolder.vTitle.setText(movie.getName());
        movieViewHolder.vDesc.setText(movie.getDescription());
        //Float rating = movie.getRating()/2;
        //movieViewHolder.vRate.setRating(rating);
        //String ratingText = String.valueOf(movie.getRating());
//        movieViewHolder.vRatingText.setText(ratingText);
        Picasso.with(mContext).load(movie.getUrl()).into(movieViewHolder.vIcon);
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView vIcon;
        public TextView vTitle;
        public TextView vDesc;
        public RatingBar vRate;
        public  TextView vRatingText;
        public  ImageView vMenu;

        public MovieViewHolder(View v)
        {
            super(v);
            vIcon = (ImageView) v.findViewById(R.id.icon);
            vTitle = (TextView) v.findViewById(R.id.titLe);
            //vRate = (RatingBar) v.findViewById(R.id.movieRating);
            vDesc = (TextView) v.findViewById(R.id.description);
            //vRatingText = (TextView) v.findViewById(R.id.ratingMovie);
            vMenu = (ImageView) v.findViewById(R.id.overflow_card);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener != null) {
                        myItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });

            if (vMenu != null) {
                vMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myItemClickListener != null) {
                            myItemClickListener.onOverflowMenuClick(v, getAdapterPosition());
                        }
                    }
                });
            }
        }
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.myItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
        public void onOverflowMenuClick(View v, final int position);
    }

}
