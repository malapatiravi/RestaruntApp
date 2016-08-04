package com.example.malz.listview.RestaruntRecy;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by malz on 8/2/16.
 */
public class RestaruntDataJSon {
    List<Map<String,?>> restaruntList;

    public int getSize(){ return restaruntList.size();}

    public HashMap getItem(int i)
    {
        if(i>=0&&i<restaruntList.size())
        {
            return (HashMap)restaruntList.get(i);
        }
        else
            return null;

    }

    public RestaruntDataJSon()
    {
        restaruntList =new ArrayList<Map<String, ?>>();
    }

    private HashMap createRestarunt(String name, String address, String dispAddress, String categories,
                                    double rating, String reviews, String distance, String url, String id)
    {
        HashMap restaurant = new HashMap();
        restaurant.put("name", name);
        restaurant.put("address", address);
        restaurant.put("displayaddress", dispAddress);
        restaurant.put("categories", categories);
        restaurant.put("rating",rating);
        restaurant.put("reviews",reviews);
        restaurant.put("distance", distance);
        restaurant.put("url",url);
        restaurant.put("id",id);
        return restaurant;
    }

    private HashMap createESRestaurant(String name, String address, String dispAddress, String categories,
                                       String minDel, String delPrice, boolean isOpen, String url, String id) {
        HashMap restaurant = new HashMap();
        restaurant.put("name", name);
        restaurant.put("address", address);
        restaurant.put("displayaddress", dispAddress);
        restaurant.put("categories", categories);
        restaurant.put("mindelivery",minDel);
        restaurant.put("deliveryprice",delPrice);
        restaurant.put("open", isOpen);
        restaurant.put("url",url);
        restaurant.put("id",id);
        return restaurant;
    }


    public HashMap downloadRestaurantDetailJSON(String urlString)
    {
        String jsonString = null;
        HashMap detailMap = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("X-Access-Token","5d26f33f30a7418c");
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream stream = httpURLConnection.getInputStream();
                jsonString = getStringFromStream(stream);
                detailMap = createRestaurantDetailJSON(jsonString);
            }
        }
        catch (Exception e) {
            Log.d("MyDebugMsg", "Exception in downloadMovieDataJson");
            e.printStackTrace();
        }
        return detailMap;
    }

    private HashMap createRestaurantDetailJSON(String jsonString)
    {
        HashMap map = null;
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            if (jsonObj != null)
            {
                JSONObject obj = jsonObj.getJSONObject("restaurant");
                if (obj != null)
                {
                    String apiKey = (String) obj.get("apiKey");
                    String name = (String) obj.get("name");
                    String street = (String) obj.get("streetAddress");
                    String city = (String) obj.get("city");
                    String dispAddress = street + ", " + city;
                    String postal_code = (String) obj.get("zip");
                    String state_code = (String) obj.get("state");
                    String address = street + ", " + city + ", " + state_code + " " + postal_code;
                    JSONArray cat_list = obj.getJSONArray("foodTypes");
                    String categories = createCategories(cat_list);
                    String imgUrl = (String) obj.get("logoUrl");
                    boolean isOpen = (boolean) obj.get("open");
                    double latitude = (double) obj.get("latitude");
                    double longitude = (double) obj.get("longitude");
                    int minWaitTime = (int) obj.get("minWaitTime");
                    int maxWaitTime = (int) obj.get("maxWaitTime");
                    String id = (String) obj.get("apiKey");
                    JSONObject days = obj.getJSONObject("hours");
                    HashMap daysMap = createDaysMap(days);
                    JSONArray zones = obj.getJSONArray("zones");
                    JSONObject zoneObj = (JSONObject) zones.get(0);
                    double deliv = (double) zoneObj.get("cost");
                    String delivery = String.valueOf(deliv);
                    double minD = (double) zoneObj.get("minimum");
                    String min = String.valueOf(minD);
                    map = buildDetailJSON(apiKey, name, dispAddress, address, categories, imgUrl, isOpen,
                            latitude, longitude, minWaitTime, maxWaitTime, id, daysMap, delivery, min);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    private String createCategories(JSONArray cat_list)
    {
        int i;
        String categories = "";
        try
        {
            for (i = 0; i < cat_list.length(); i++)
            {
                categories += cat_list.get(i).toString() + ", ";
            }
            int ind = categories.lastIndexOf(", ");
            if( ind>=0 )
                categories = new StringBuilder(categories).replace(ind, ind+1,"").toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return categories;
    }
    private HashMap createDaysMap(JSONObject days)
    {
        HashMap daysMap = null;
        try {
            daysMap = new HashMap();
            JSONArray day = new JSONArray();
            String dayStr = "Monday";
            if (days.has(dayStr))
            {
                day = days.getJSONArray(dayStr);
                daysMap.put(dayStr, day.getString(0));
            }
            else
            {
                daysMap.put(dayStr, "NA");
            }
            dayStr = "Saturday";
            if (days.has(dayStr))
            {
                day = days.getJSONArray(dayStr);
                daysMap.put(dayStr, day.getString(0));
            }
            else
            {
                daysMap.put(dayStr, "NA");
            }
            dayStr = "Sunday";
            if (days.has(dayStr))
            {
                day = days.getJSONArray(dayStr);
                daysMap.put(dayStr, day.getString(0));
            }
            else
            {
                daysMap.put(dayStr, "NA");
            }
            dayStr = "Tuesday";
            if (days.has(dayStr))
            {
                day = days.getJSONArray(dayStr);
                daysMap.put(dayStr, day.getString(0));
            }
            else
            {
                daysMap.put(dayStr, "NA");
            }
            dayStr = "Wednesday";
            if (days.has(dayStr))
            {
                day = days.getJSONArray(dayStr);
                daysMap.put(dayStr, day.getString(0));
            }
            else
            {
                daysMap.put(dayStr, "NA");
            }
            dayStr = "Thursday";
            if (days.has(dayStr))
            {
                day = days.getJSONArray(dayStr);
                daysMap.put(dayStr, day.getString(0));
            }
            else
            {
                daysMap.put(dayStr, "NA");
            }
            dayStr = "Friday";
            if (days.has(dayStr))
            {
                day = days.getJSONArray(dayStr);
                daysMap.put(dayStr, day.getString(0));
            }
            else
            {
                daysMap.put(dayStr, "NA");
            }
        } catch (Exception e) {
            e.printStackTrace();
            daysMap = null;
        }
        return daysMap;
    }

    private HashMap buildDetailJSON(String apiKey, String name, String dispAddress, String address,
                                    String categories, String imgUrl, boolean isOpen,
                                    double latitude, double longitude, int minWaitTime,
                                    int maxWaitTime, String id, HashMap daysMap,
                                    String delivery, String min)
    {
        HashMap restaurant = new HashMap();
        restaurant.put("apikey", apiKey);
        restaurant.put("name", name);
        restaurant.put("address", address);
        restaurant.put("displayaddress", dispAddress);
        restaurant.put("categories", categories);
        restaurant.put("open", String.valueOf(isOpen));
        restaurant.put("url",imgUrl);
        restaurant.put("id",id);
        restaurant.put("latitude", latitude);
        restaurant.put("longitude", longitude);
        restaurant.put("mintime", minWaitTime);
        restaurant.put("maxtime", maxWaitTime);
        restaurant.put("hours", daysMap);
        restaurant.put("delivery", delivery);
        restaurant.put("min", min);
        return restaurant;
    }


    private String getStringFromStream(InputStream stream)
    {
        String line, jsonString = null;
        if (stream != null)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder out = new StringBuilder();
            try
            {
                while ((line = reader.readLine()) != null)
                {
                    out.append(line);
                }
                reader.close();
                jsonString = out.toString();
            }
            catch (Exception ex1)
            {
                ex1.printStackTrace();
            }
        }
        return jsonString;
    }

    public int findFirst(String query) {
        int i, movieSize = restaruntList.size();
        for (i=0; i< movieSize; i++)
            if (restaruntList.get(i).get("name").toString().toLowerCase().contains(query.toLowerCase()))
                return i;
        return movieSize;
    }

}
