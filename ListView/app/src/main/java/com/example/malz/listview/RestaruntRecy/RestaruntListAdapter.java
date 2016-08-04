package com.example.malz.listview.RestaruntRecy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malz.listview.MyUtility;
import com.example.malz.listview.R;
import com.example.malz.listview.RecyOne.MyDownloadImageAsyncTask;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by malz on 8/2/16.
 */
public class RestaruntListAdapter extends RecyclerView.Adapter<RestaruntListAdapter.ViewHolder> {
    private List<Map<String, ?>> mDataset;
    private Context mContext;
    private AdapterView.OnItemClickListener mItemClickListener;
    private LruCache<String, Bitmap> mImgCache;


    public RestaruntListAdapter(Context myContext, List<Map<String,?>> myDataset, LruCache<String, Bitmap> myImgCache)
    {
        mDataset = myDataset;
        mContext = myContext;
        mImgCache = myImgCache;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_rest_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, ?> restarunt_map = mDataset.get(position);
        holder.bindMovieData(restarunt_map);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView vIcon;
        public TextView vTitle;
        //public TextView vDispAddr;
        public TextView vMinDel;
        public TextView vDelPrice;
        public TextView vCategories;
        public ViewHolder(View v)
        {
            super(v);
            vIcon = (ImageView) v.findViewById(R.id.movieIcon);
            vTitle = (TextView) v.findViewById(R.id.titleMovie);
            vMinDel = (TextView) v.findViewById(R.id.minDelivery);
            //vDispAddr = (TextView) v.findViewById(R.id.dispAddress);
            vDelPrice = (TextView) v.findViewById(R.id.deliveryPrice);
            vCategories = (TextView) v.findViewById(R.id.categories);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                       ;// mItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemClickListener != null) {
                       ;// mItemClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
        public void bindMovieData( Map<String, ?> movie)
        //public void bindMovieData( final Map<String,?> movie)
        {
            String url = (String) movie.get("url");
            final Bitmap bitmap = mImgCache.get(url);
            vTitle.setText((String) movie.get("name"));
            //vDispAddr.setText((String) movie.get("displayaddress"));
            vMinDel.setText((String) movie.get("mindelivery"));
            vDelPrice.setText((String) movie.get("deliveryprice"));
            vCategories.setText((String)  movie.get("categories"));
            if (bitmap != null)
            {
                vIcon.setImageBitmap((bitmap));
            }
            else
            {
                MyDownloadImageAsyncTask1 myDownloadImageAsyncTask = new MyDownloadImageAsyncTask1(vIcon);
                myDownloadImageAsyncTask.execute(url);
            }
        }
        }


    public void SetOnItemClickListener(final AdapterView.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }

    private class MyDownloadImageAsyncTask1 extends AsyncTask<String, Void, Bitmap>
    {
        private final WeakReference<ImageView> imageViewReference;
        public MyDownloadImageAsyncTask1(ImageView imageView)
        {
            imageViewReference = new WeakReference<>(imageView);
        }
        @Override
        protected Bitmap doInBackground(String... urls)
        {
            Bitmap bitmap = null;
            for (String url : urls)
            {
                bitmap = MyUtility.downloadImageusingHTTPGetRequest(url);
                if (bitmap != null)
                {
                    mImgCache.put(url, bitmap);
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null)
            {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null)
                {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }


}
