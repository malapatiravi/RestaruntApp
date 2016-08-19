package com.app.android.mapgeneral;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MenuData
{
    HashMap<String, List<HashMap<String, String>>> menuData;

    public HashMap<String, List<HashMap<String, String>>> getMenu() {
        return menuData;
    }

    public int getSize()
    {
        return menuData.size();
    }

    public String getMenuItem(int position)
    {
        String key = null;
        Set<String> keySet = menuData.keySet();
        Iterator<String> iter = keySet.iterator();
        for (int i=0; i<position; i++)
        {
            if (iter.hasNext())
                iter.next();
            else
                break;
        }
        if (iter.hasNext())
            key = iter.next();
        return key;
    }

    public MenuData() {
        menuData = new HashMap<>();
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

    public void getMenuDetails(String urlString)
    {
        String jsonString;
        HashMap detailMap = null;
        try {
            Log.i("Getting Menu details","in MenuData.javaFile");
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("X-Access-Token","5d26f33f30a7418c");
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream stream = httpURLConnection.getInputStream();
                jsonString = getStringFromStream(stream);
                Log.i("in getMenuDetails","got jsonString"+jsonString);
//               /* detailMap =*/
                    createMenuJSON(jsonString);
            }
        }
        catch (Exception e) {
            Log.d("MyDebugMsg", "Exception in downloadMovieDataJson");
            e.printStackTrace();
        }
        //menuData = detailMap;
    }

    private void createMenuJSON(String jsonString) {
        int i;
        try
        {
            JSONArray array = new JSONArray(jsonString);
            for (i=0; i<array.length(); i++)
            {
                Log.i("in createMenuJSON","recevied"+jsonString);
                JSONObject jsonObject = array.getJSONObject(i);
                String name = (String) jsonObject.get("name");
                JSONArray menuItems = jsonObject.getJSONArray("items");
                menuData.put(name, createMenuItemMap(menuItems));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private List<HashMap<String, String>> createMenuItemMap(JSONArray menuItems)
    {
        List<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> items;
        int i;
        try
        {
            for (i=0; i<menuItems.length(); i++)
            {
                items = new HashMap<>();
                JSONObject obj = menuItems.getJSONObject(i);
                String apiKey = (String) obj.get("apiKey");
                String name = (String) obj.get("name");
                double price = (double) obj.get("basePrice");
                String basePrice = "Price: $"+String.valueOf(price);
                String description = null;
                if (obj.has("description"))
                {
                    description = (String) obj.get("description");
                }
                items.put("apikey", apiKey);
                items.put("name", name);
                items.put("price", basePrice);
                items.put("description", description);
                items.put("selection", "false");
                list.add(items);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            list = null;
        }
        return list;
    }
}
