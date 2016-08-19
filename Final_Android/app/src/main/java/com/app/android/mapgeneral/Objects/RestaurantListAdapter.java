package com.app.android.mapgeneral.Objects;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.app.android.mapgeneral.MyUtility;
import com.app.android.mapgeneral.R;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by malz on 8/2/16.
 */

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>
{
  private List<Map<String, ?>> mDataset;
  private Context mContext;
  private OnItemClickListener mItemClickListener;
  private LruCache<String, Bitmap> mImgCache;


  public RestaurantListAdapter(Context myContext, List<Map<String, ?>> myDataset, LruCache<String, Bitmap> myImgCache)
  {
    mDataset = myDataset;
    mContext = myContext;
    mImgCache = myImgCache;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_rest_card, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position)
  {
    Map<String, ?> restarunt_map = mDataset.get(position);
    holder.bindMovieData(restarunt_map);
  }

  @Override
  public int getItemCount()
  {
    return mDataset.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder
  {

    public TextView vCategories;
    public TextView vOpen;
    public ImageView vlocation;
    public ImageView vIcon;
    public TextView vTitle;
    public TextView vMinDel;
    public TextView vDelPrice;

    public ViewHolder(View v)
    {
      super(v);
      vIcon = (ImageView) v.findViewById(R.id.movieIcon);
      vTitle = (TextView) v.findViewById(R.id.titleMovie);
      vMinDel = (TextView) v.findViewById(R.id.minDelivery);
      vDelPrice = (TextView) v.findViewById(R.id.deliveryPrice);
      vCategories = (TextView) v.findViewById(R.id.categories);
      vOpen=(TextView)v.findViewById(R.id.status);
      vlocation=(ImageView)v.findViewById(R.id.location);

      v.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
        if (mItemClickListener != null) mItemClickListener.onItemClick(v, getAdapterPosition(), vIcon);
        }
      });

      v.setOnLongClickListener(new View.OnLongClickListener()
      {
        @Override
        public boolean onLongClick(View v)
        {
        if (mItemClickListener != null)
        {
          ;// mItemClickListener.onItemLongClick(v, getAdapterPosition());
        }
        return true;
        }
      });
    }

    public void bindMovieData(Map<String, ?> restaurant)
    {
      String url = (String) restaurant.get("url");
      final Bitmap bitmap = mImgCache.get(url);
      vTitle.setText((String) restaurant.get("name"));
      vMinDel.setText((String) restaurant.get("mindelivery"));
      //vDelPrice.setText((String) restaurant.get("deliveryprice"));
      vCategories.setText("Address : "+(String) restaurant.get("address"));
      final String name = (String) restaurant.get("name");
      final Map<String, ?> rest1=restaurant;
      final Double latitude = (Double) restaurant.get("latitude");
      final Double longitude = (Double) restaurant.get("longitude");
      vlocation.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          String address = name + (String) rest1.get("address");
          String geoLocation = String.format(Locale.ENGLISH, "geo:%f,%f?q=%s", latitude, longitude, address);
          Intent intent = new Intent(Intent.ACTION_VIEW);
          intent.setData(Uri.parse(geoLocation));

          mContext.startActivity(intent);
        }
      });
      if(restaurant.get("open").toString()=="true")
      {
        vOpen.setText("Opened Now");
      }
      else
        vOpen.setText("Closed Now");

      if (bitmap != null) vIcon.setImageBitmap((bitmap));
      else
      {
        ImageDownloader myDownloadImageAsyncTask = new ImageDownloader(vIcon);
        myDownloadImageAsyncTask.execute(url);
      }
      vIcon.setTransitionName(restaurant.get("id").toString()+"_transition_image");
    }
  }


  public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {this.mItemClickListener = mItemClickListener;}

  public interface OnItemClickListener
  {
    void onItemClick(View v, int position, ImageView vIcon);
    void onItemLongClick(View v, int position);
  }

  private class ImageDownloader extends AsyncTask<String, Void, Bitmap>
  {
    private final WeakReference<ImageView> imageViewReference;
    public ImageDownloader(ImageView imageView) {imageViewReference = new WeakReference<>(imageView);}
    @Override
    protected Bitmap doInBackground(String... urls)
    {
      Bitmap bitmap = null;
      for (String url : urls)
      {
        bitmap = MyUtility.downloadImageusingHTTPGetRequest(url);
        if (bitmap != null) mImgCache.put(url, bitmap);
      }
      return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
      if (bitmap != null)
      {
        final ImageView imageView = imageViewReference.get();
        if (imageView != null) imageView.setImageBitmap(bitmap);
      }
    }
  }


}
