package com.app.android.mapgeneral;

/**
 * Created by Charles on 6/12/2015.
 */
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDataJsonLocal {
    List<Map<String,?>> moviesList;

    public List<Map<String, ?>> getMoviesList() {
        return moviesList;
    }
    public int getSize(){
        return moviesList.size();
    }
    public HashMap getItem(int i){
        if (i >=0 && i < moviesList.size()){
            return (HashMap) moviesList.get(i);
        } else
            return null;
    }

    public MovieDataJsonLocal() {
        moviesList = new ArrayList<Map<String,?>>();
    }

    public void loadLocalMovieDataJson(Context context) {
        moviesList.clear(); // clear the list

        // movie.json contains an array of movies
        String moviesArray = loadFileFromAsset(context, "movie");
        if (moviesArray == null){
            Log.d("MyDebugMsg", "Having trouble load movie.json");
            return;
        }

        try {
            // Parse the string to construct JSON array
            JSONArray moviesJsonArray = new JSONArray(moviesArray);
            for (int i = 0; i < moviesJsonArray.length(); i++) {
                JSONObject movieJsonObj = (JSONObject) moviesJsonArray.get(i);
                if (movieJsonObj != null) {
                    String name = (String) movieJsonObj.get("name");
                    double rating = Double.parseDouble(movieJsonObj.get("rating").toString());
                    String url = (String) movieJsonObj.get("url");
                    String description = (String) movieJsonObj.get("description");
                    String id = (String) movieJsonObj.get("id");
                    String image = (String) movieJsonObj.get("image");
                    String stars = (String) movieJsonObj.get("stars");
                    String year = (String) movieJsonObj.get("year");
                    String director = (String) movieJsonObj.get("director");
                    String length = (String) movieJsonObj.get("length");
                   // int resID = context.getResources().getIdentifier(image, "drawable", context.getPackageName());
                    moviesList.add(createMovie(name, image, description, year, length, rating, director, stars, url, id));
                }
            }
        } catch (JSONException ex){
            Log.d("MyDebugMsg", "JSONException in loadLocalMovieDataJson()");
            ex.printStackTrace();
        }
    }

    public String loadFileFromAsset(Context context, String fileName) {
        String contents = null, line;
        try {
            InputStream stream = context.getAssets().open(fileName);
            if (stream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder out = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                reader.close();
                contents = out.toString();
            }
        } catch (IOException ex) {
            Log.d("MyDebugMsg", "IOException in loadFileFromAsset()");
            ex.printStackTrace();
        }
        return contents;
    }

    private static HashMap createMovie(String name, String image, String description, String year,
                                       String length, double rating, String director, String stars, String url, String id) {
        HashMap movie = new HashMap();
        movie.put("image",image);
        movie.put("name", name);
        movie.put("description", description);
        movie.put("year", year);
        movie.put("length",length);
        movie.put("rating",rating);
        movie.put("director",director);
        movie.put("stars",stars);
        movie.put("url",url);
        movie.put("id",id);
        movie.put("selection",false);
        return movie;
    }

    private static HashMap createMovie_brief(String name, String description,
                                             double rating, String url, String id) {
        HashMap movie = new HashMap();

        movie.put("name", name);
        movie.put("description", description);
        movie.put("rating",rating);
        movie.put("url",url);
        movie.put("id",id);
        return movie;
    }

}
