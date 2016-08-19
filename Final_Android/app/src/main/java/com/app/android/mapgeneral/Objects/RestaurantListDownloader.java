package com.app.android.mapgeneral.Objects;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

public class RestaurantListDownloader extends AsyncTask<String, Void, RestaurantDataJSON>
{
  private static final String TAG = "RestaurantsDownloader";
  private final RestaurantDataJSON restaurantData;
  private final WeakReference<RestaurantListAdapter> adapterReference;

  public RestaurantListDownloader(RestaurantListAdapter myRecyclerViewAdapter, RestaurantDataJSON map)
  {
    restaurantData = map;
    adapterReference = new WeakReference<>(myRecyclerViewAdapter);
  }

  @Override
  protected RestaurantDataJSON doInBackground(String... urls)
  {
    RestaurantDataJSON movieDataJson = new RestaurantDataJSON();
    String url = urls[0];
    logMessage("Url is: " + url);
    movieDataJson.downloadMovieDetailJSON(url);
    return movieDataJson;
  }

  @Override
  protected void onPostExecute(RestaurantDataJSON movieDataJson)
  {
    int i;
    restaurantData.restaurantList.clear();
    if (movieDataJson != null)
    {
      for (i = 0; i < movieDataJson.getSize(); i++)
      {
        restaurantData.restaurantList.add(movieDataJson.getItem(i));
      }
      final RestaurantListAdapter adapter = adapterReference.get();
      if (adapter != null)
      {
        adapter.notifyDataSetChanged();
      }
    }
  }

  public void logMessage(String msg)
  {
    if (Shared.DEBUG_MODE) Log.i(TAG, msg);
  }
}
