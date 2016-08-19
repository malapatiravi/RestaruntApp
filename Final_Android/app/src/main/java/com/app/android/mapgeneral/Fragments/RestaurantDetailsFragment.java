package com.app.android.mapgeneral.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.android.mapgeneral.RestaurantDetails;
import com.app.android.mapgeneral.MenuData;
import com.app.android.mapgeneral.Activities.MenuPagerActivity;
import com.app.android.mapgeneral.MyUtility;
import com.app.android.mapgeneral.Objects.Shared;
import com.app.android.mapgeneral.R;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RestaurantDetailsFragment extends Fragment
{
  private static final String TAG = "DetailsMainFragment";
  private HashMap<String, ?> restaurant;
  private String googleMapURL = "https://maps.googleapis.com/maps/api/staticmap?scale=2&zoom=18&maptype=roadmap";
  private String menuDetail = "https://api.eatstreet.com/publicapi/v1/restaurant/";

  public static RestaurantDetailsFragment newInstance(HashMap<String, ?> map)
  {
    RestaurantDetailsFragment fragment = new RestaurantDetailsFragment();
    Bundle args = new Bundle();
    args.putSerializable(RestaurantDetails.RESTAURANT_TAG, map);
    fragment.setArguments(args);
    return fragment;
  }

  public RestaurantDetailsFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) restaurant = (HashMap<String, ?>) getArguments().getSerializable(RestaurantDetails.RESTAURANT_TAG);
    setRetainInstance(true);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View rootView = inflater.inflate(R.layout.fragment_restaurants_detail, container, false);
    //Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar_rest_detail);
    TextView text = (TextView) rootView.findViewById(R.id.restaurant_toolbar_title);
    text.setText(restaurant.get("name").toString());
    final TextView nameTV = (TextView) rootView.findViewById(R.id.restaurant_name);
    final TextView isopenTV = (TextView) rootView.findViewById(R.id.isopen);
    final TextView deliveryTV = (TextView) rootView.findViewById(R.id.delivery);
    final TextView minTV = (TextView) rootView.findViewById(R.id.minprice);
    final TextView waitTV = (TextView) rootView.findViewById(R.id.waitingtime);
    final ImageView googleMaps = (ImageView) rootView.findViewById(R.id.googlemaps);
    final TextView addressTV = (TextView) rootView.findViewById(R.id.address);

    final TextView mondayTV = (TextView) rootView.findViewById(R.id.monday);
    final TextView tuesdayTV = (TextView) rootView.findViewById(R.id.tuesday);
    final TextView wednesdayTV = (TextView) rootView.findViewById(R.id.wednesday);
    final TextView thursdayTV = (TextView) rootView.findViewById(R.id.thursday);
    final TextView fridayTV = (TextView) rootView.findViewById(R.id.friday);
    final TextView satTV = (TextView) rootView.findViewById(R.id.saturday);
    final TextView sundayTV = (TextView) rootView.findViewById(R.id.sunday);

    ImageView backDrop = (ImageView) rootView.findViewById(R.id.backdrop);
    backDrop.setImageResource(R.drawable.food);
    backDrop.setTransitionName(restaurant.get("id").toString()+"_transition_image");

    final String apiKey = (String) restaurant.get("apikey");
    final String name = (String) restaurant.get("name");
    String isOpen = (String) restaurant.get("open");
    String delivery = (String) restaurant.get("delivery");
    String minprice = (String) restaurant.get("min");
    final Double latitude = (Double) restaurant.get("latitude");
    final Double longitude = (Double) restaurant.get("longitude");
    Integer minTime = (Integer) restaurant.get("mintime");
    Integer maxTime = (Integer) restaurant.get("maxtime");
    String waitTime = minTime + " - " + maxTime + " mins";
    final String address = (String) restaurant.get("address");
    CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
    //collapsingToolbar.setTitle((String) restaurant.get("name"));
    setRefreshToolbarEnable(collapsingToolbar, true);
    //mToolbar.setTitle((String) restaurant.get("name"));


    FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.share);
    fab.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = name + address;
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Restaurant details");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Sharevia"));
      }
    });

    nameTV.setText(name);
    if (isOpen.equals("true")) isopenTV.setText("Open Now");
    else
    {
      isopenTV.setTextColor(getResources().getColor(R.color.red));
      isopenTV.setText("Closed Now");
    }
    deliveryTV.setText("Address"+address+"\\r\\n "+"Delivery: $" + delivery);
    minTV.setText("Minimum: $" + minprice);
    waitTV.setText("Delivery Time: " + waitTime);
    addressTV.setText(address);

    HashMap days = (HashMap) restaurant.get("hours");
    if(days.get("Monday")!="NA")
          mondayTV.setText((String) days.get("Monday"));
    else
      mondayTV.setText("Not Opened");
    tuesdayTV.setText((String) days.get("Tuesday"));
    wednesdayTV.setText((String) days.get("Wednesday"));
    thursdayTV.setText((String) days.get("Thursday"));
    fridayTV.setText((String) days.get("Friday"));
    if(days.get("Sunday")!="NA")
    satTV.setText((String) days.get("Saturday"));
    else
      satTV.setText("Not Opened");
    if(days.get("Sunday")!="NA")
    sundayTV.setText((String) days.get("Sunday"));
    else
      sundayTV.setText("Not Opened");

    RelativeLayout menu_button = (RelativeLayout) rootView.findViewById(R.id.menu_button);
    menu_button.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        menuDetail += apiKey + "/menu?includeCustomizations=false";
        MenuDownloader task = new MenuDownloader();
        logMessage("Menu download Task called now");
        task.execute(menuDetail);
      }
    });

    formGoogleMapURL(String.valueOf(latitude), String.valueOf(longitude));

    ImageDownloader task = new ImageDownloader(googleMaps);
    task.execute(googleMapURL);

    googleMaps.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        String address = name + (String) restaurant.get("address");
        String geoLocation = String.format(Locale.ENGLISH, "geo:%f,%f?q=%s", latitude, longitude, address);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(geoLocation));
        getContext().startActivity(intent);
      }
    });
    ImageView you_video = (ImageView) rootView.findViewById(R.id.youtube_video);
    you_video.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        if (YouTubeIntents.canResolvePlayVideoIntent(getActivity()))
        {
          //Opens in the StandAlonePlayer, defaults to fullscreen
          startActivity(YouTubeStandalonePlayer.createVideoIntent(getActivity(), getString(R.string.google_maps_key), "68A_HPYGdlk", 50000, true, true));
        }
      }
    });
    return rootView;
  }

  private void formGoogleMapURL(String latitude, String longitude)
  {
    int height = 340;
    googleMapURL += "&size=" + 500 + "x" + height;
    googleMapURL += "&markers=color:green%7C" + latitude + "," + longitude;
    googleMapURL += "&key=" + getResources().getString(R.string.googlemap_id);
  }

  private class ImageDownloader extends AsyncTask<String, Void, Bitmap>
  {
    private final WeakReference<ImageView> imageViewReference;
    public ImageDownloader(ImageView imageView)
    {
      imageViewReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... urls)
    {
      Bitmap bitmap = null;
      for (String url : urls) bitmap = MyUtility.downloadImageusingHTTPGetRequest(url);
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

  private class MenuDownloader extends AsyncTask<String, Void, MenuData>
  {
    private MenuData mMenuData;
    public MenuDownloader()
    {
      mMenuData = new MenuData();
    }
    @Override
    protected MenuData doInBackground(String... params)
    {
      MenuData menuData = new MenuData();
      logMessage("DownLoading:" + params[0]);
      menuData.getMenuDetails(params[0]);
      return menuData;
    }

    @Override
    protected void onPostExecute(MenuData menu)
    {
      HashMap<String, List<HashMap<String, String>>> mMap = mMenuData.getMenu();
      mMap.clear();
      if (menu != null)
      {
        HashMap<String, List<HashMap<String, String>>> map = menu.getMenu();
        for (String key : map.keySet()) mMap.put(key, map.get(key));
        Intent intent = new Intent(getContext(), MenuPagerActivity.class);
        intent.putExtra("menu", mMap);
        startActivity(intent);
      }
    }
  }

  public static void setRefreshToolbarEnable(CollapsingToolbarLayout collapsingToolbarLayout, boolean refreshToolbarEnable) {
    try
    {
      Field field = CollapsingToolbarLayout.class.getDeclaredField("mRefreshToolbar");
      field.setAccessible(true);
      field.setBoolean(collapsingToolbarLayout, refreshToolbarEnable);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
  public void logMessage(String msg) {if (Shared.DEBUG_MODE) Log.i(TAG, msg);}
}
