package com.app.android.mapgeneral;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Charles on 6/13/2015.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> implements YouTubeThumbnailView.OnInitializedListener {
    private List<Map<String,?>> mDataset;
    private Context mContext;
    /*******************************/
    private Map<View, YouTubeThumbnailLoader> mLoaders;
    private Map<String, YouTubeThumbnailView> keyLoaders;
    private YouTubeThumbnailView thumb;
    LruCache mImgMemoryCache;
    /*******************************/
    private int option;
    OnItemClickListener mItemClickListener;
    public MyRecyclerViewAdapter(Context myContext, List<Map<String, ?>> myDataset, LruCache mImgMemoryCache, int option){
        mContext = myContext;
        mDataset = myDataset;
        this.option=option;
        mLoaders = new HashMap<>();
        this.mImgMemoryCache=mImgMemoryCache;
        keyLoaders=new HashMap<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        YouTubeThumbnailView vIcon;
        TextView vTitle;
        TextView vRating;
        TextView vDescription;
        ImageView vMenu;

        public ViewHolder(View v){
            super(v);
            vIcon=(YouTubeThumbnailView)v.findViewById(R.id.icon);
            vTitle=(TextView)v.findViewById(R.id.title);
            vRating=(TextView)v.findViewById(R.id.rating);
            vDescription=(TextView)v.findViewById(R.id.description);
            vMenu=(ImageView)v.findViewById(R.id.overflow_button);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
            if(vMenu!=null){
                vMenu.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(mItemClickListener!=null){
                            mItemClickListener.onOverflowMenuClick(v,getAdapterPosition());
                        }
                    }
                });
            }
        }
        public void bindMovieData(Map<String, ?> movie){
            if(vTitle!=null)
                vTitle.setText(movie.get("name").toString());//((String)movie.get("name"));
            if(vRating!=null)
                vRating.setText(movie.get("rating") + "/10");
            if(vDescription!=null)
                vDescription.setText((String) movie.get("description"));
            if(vIcon!=null)
            {
                vIcon.setTag(movie.get("image").toString());
              //  vIcon.initialize(mContext.getString(R.string.google_maps_key),YouTubeThumbnailView.OnInitializedListener);
            }
        }
    }
    public void duplicateItem(int position){
        HashMap<String,?> movie = (HashMap<String,?>) mDataset.get(position);
        mDataset.add(position, (HashMap<String, ?>) movie.clone());
        HashMap<String, ?> itemMap =
                (HashMap<String, ?>) movie.get(position+1);
    }
    @Override
    public long getItemId(int position){return super.getItemId(position);}

    public void removeItem(int position){ mDataset.remove(position); }
    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v;
        v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public int getItemViewType(int position){
        return option;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Map<String, ?> movie=mDataset.get(position);
        holder.bindMovieData(movie);
        YouTubeThumbnailLoader loader = mLoaders.get(holder.itemView.findViewById(R.id.icon));
       // YouTubeThumbnailView thumbview = keyLoaders.get(movie.get("image"));
        if(loader==null)
            holder.vIcon.initialize(mContext.getString(R.string.google_maps_key),this);
        else
        {
            try {
                loader.setVideo(holder.vIcon.getTag().toString());
            } catch (IllegalStateException exception) {
                //If the Loader has been released then remove it from the map and re-init
                mLoaders.remove(holder.itemView.findViewById(R.id.icon));
                holder.vIcon.initialize(mContext.getString(R.string.google_maps_key), this);
            }
        }
    }
    @Override
    public int getItemCount(){return mDataset.size();}

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
        void onOverflowMenuClick(View view, final int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener=mItemClickListener;
    }

    @Override
    public void onInitializationSuccess(final YouTubeThumbnailView view, final YouTubeThumbnailLoader loader) {

        loader.setVideo((String) view.getTag());
        mLoaders.put(view, loader);
        loader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                //keyLoaders.put(s,youTubeThumbnailView);
                //loader.release();
                Log.d("Thumbnail ", "Loaded");
            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView,
                                         YouTubeThumbnailLoader.ErrorReason errorReason) {
                Log.d("Adapter Error ", "onThumbnailError");
            }
        });
    }
    @Override
    public void onInitializationFailure(YouTubeThumbnailView thumbnailView, YouTubeInitializationResult errorReason) {
        final String errorMessage = errorReason.toString();
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
    }

    public void releaseAllLoader(){
        for(YouTubeThumbnailLoader loader : mLoaders.values()){
            loader.release();
            Log.d("release","over");
        }
    }
}
