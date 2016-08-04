package com.example.malz.listview.RecyOne;
import android.support.annotation.StringRes;
import android.util.Log;

import com.example.malz.listview.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by malz on 7/8/16.
 */
public class MovieDataJSon {

    public String FILE_SERVER;
    public String PHP_SERVER = "http://www.malz.com/index1.php/";
    List<Map<String,?>> moviesList = new ArrayList<Map<String,?>>();

    public void addItem(int position, Map<String, ?> item)
    {
        final JSONObject json;
        if(item!=null)
        {
            json=new JSONObject(item);
            try{
                Log.i("Info:",(String)json.get("length")+(String)json.get("year"));
            }
            catch(JSONException ex)
            {
                Log.i("Info","JSON Exception in addItem");
            }


        }
        else
            json=null;
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                String url = PHP_SERVER+"addMovie";
                MyUtility.sendHttPostRequest(url, json);
            }
        };
        new Thread(runnable).start();

        moviesList.add(position, item);
    }
    public void deleteItem(int position, Map<String, ?> item)
    {
        final JSONObject json;
        if(item!=null)
        {
            json=new JSONObject(item);
            try{
                Log.i("Info:delete",(String)json.get("length")+(String)json.get("year"));
            }
            catch(JSONException ex)
            {
                Log.i("Info","JSON Exception in delete");
            }


        }
        else
            json=null;
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                String url=PHP_SERVER+"delMovie";
                MyUtility.sendHttPostRequest(url, json);
                Log.d("delete",url);
            }
        };
        new Thread(runnable).start();

        moviesList.remove(position);

    }
    public HashMap getItem(int i)
    {
        if (i >=0 && i < moviesList.size())
        {
            return (HashMap) moviesList.get(i);
        }
            else return null;
    }
    public HashMap getItemByUrl(String url)
    {
        MyUtility testUtility = new MyUtility();
        String name=null;
        String imgURL=null;
        String primaryID=null;
        String id=null;
        String description=null;
        String stars=null;
        String length=null;
        String image=null;
        String year=null;
        double rating=0;
        String director=null;

        String movieJSonStr =  testUtility.downloadJSONusingHTTPGetRequest(url);

        try
        {
            if(movieJSonStr!=null)
            {
                JSONArray moviesJsonArray = new JSONArray(movieJSonStr);
                for(int i=0;i<moviesJsonArray.length();i++)
                {
                    //JSONObject movieJSONObj=new JSONObject(movieJSonStr);
                    JSONObject movieJSONObj = (JSONObject) moviesJsonArray.get(i);
                    if(movieJSONObj!=null){
                        //primaryID=(String) movieJSONObj.get("primaryID");
                        id=(String)movieJSONObj.get("id");
                        name = (String) movieJSONObj.get("name");
                        description = (String) movieJSONObj.get("description");
                        stars=(String)movieJSONObj.get("stars");
                        length=(String)movieJSONObj.get("length");
                        //Image=(String)movieJSONObj.get("Image");
                        int yr=(int)movieJSONObj.get("year");
                        year=Integer.toString(yr);
                        //year=(String) movieJSONObj.get("year");
                        rating=Double.parseDouble(movieJSONObj.get("rating").toString());
                        director=(String)movieJSONObj.get("director");

                        //rating = Double.parseDouble(movieJSONObj.get("rating").toString());
                        imgURL = (String) movieJSONObj.get("url");

                        //
                        //id = (String) movieJSONObj.get("id");
                        //Log.d("Info:",name+director+imgURL);
                    }
                }


                HashMap movie = new HashMap();
                movie.put("primaryID", primaryID);
                movie.put("id",id);
                movie.put("image",image);
                movie.put("name", name);
                movie.put("description", description);
                movie.put("year", year);
                movie.put("length",length);
                movie.put("rating",rating);
                movie.put("director",director);
                movie.put("stars",stars);
                movie.put("url",imgURL);
                movie.put("selection",false);
                return movie;
            }
            return null;
        }
        catch(JSONException ex)
        {
            Log.d("MyMsg", "JSONExceptionIn getItemByUrl "+url);
        }
        return null;

    }
    public void downloadMovieDataJson( String url)
    {
        moviesList.removeAll(moviesList);
        //Log.d("In doanloadJson",url);
        MyUtility testUtility = new MyUtility();
        url=url;
        String movieJSon=testUtility.downloadJSONusingHTTPGetRequest(url);
        //Log.d("In doanloadJson1",movieJSon);
        try
        {
            if(movieJSon!=null)
            {
                JSONArray moviesJsonArray = new JSONArray(movieJSon);
                //Log.d("In doanloadJson2",""+moviesJsonArray.length());
                for(int i=0;i<moviesJsonArray.length();i++)
                {

                    String name=null;
                    String imgURL=null;
                    String primaryID=null;
                    String id=null;
                    String description=null;
                    String stars=null;
                    String length=null;
                    String Image=null;
                    String year=null;
                    double rating=0;
                    String director=null;


                    JSONObject movieJSONObj = (JSONObject) moviesJsonArray.get(i);

                    if(movieJSONObj!=null){



                        id=(String)movieJSONObj.get("id");
                        name = (String) movieJSONObj.get("name");
                        description = (String) movieJSONObj.get("description");


                        Image=(String)movieJSONObj.get("Image");
                        Log.i("In Json6",""+movieJSONObj.toString());
                        year=(String) movieJSONObj.get("year");
                        rating=Double.parseDouble(movieJSONObj.get("rating").toString());
                        director=(String)movieJSONObj.get("director");
                        imgURL = (String) movieJSONObj.get("url");
                        primaryID=(String) movieJSONObj.get("primaryID");
                        stars=(String)movieJSONObj.get("stars");
                        length=(String)movieJSONObj.get("length");
                        Log.d("In doanloadJson2",""+moviesJsonArray.length()+name);
                    }

                    moviesList.add(createMovie(primaryID, id, name, Image, description, year,
                            length, rating, director, stars, imgURL));
                }
            }

        }
        catch(JSONException ex){
            Log.d("MyMsg", "JSONException");
        }

      /*  MyUtility testUtility = new MyUtility();
        //String description = null;
        //double rating = 0.0;
        String imgURL = null;
        String name = null;
        //String id = null;
        moviesList = new ArrayList<Map<String,?>>();
        String moviesArray = testUtility.downloadJSONusingHTTPGetRequest(url);
        if(moviesArray== null){
            Log.d("MyMsg", "Trouble loading URL" + url);
            return;
        }

        try{

            JSONArray moviesJsonArray = new JSONArray(moviesArray);
            for(int i=0;i<moviesJsonArray.length();i++){
                JSONObject movieJSONObj = (JSONObject) moviesJsonArray.get(i);
                if(movieJSONObj!=null){
                    name = (String) movieJSONObj.get("name");
                    //rating = Double.parseDouble(movieJSONObj.get("rating").toString());
                    imgURL = (String) movieJSONObj.get("url");
                    //description = (String) movieJSONObj.get("description");
                    //id = (String) movieJSONObj.get("id");
                }
                moviesList.add(createMovie(name, imgURL));
            }

        }
        catch(JSONException ex){
            Log.d("MyMsg", "JSONException");
        }*/

    }
    public int getSize()
    {
        return moviesList.size();
    }
    public List<Map<String, ?>> getMoviesList() {
        return moviesList;
    }

    private HashMap createMovie(String primaryID, String id, String name, String image, String description, String year,
                                String length, double rating, String director, String stars, String url) {
        HashMap movie = new HashMap();
        movie.put("primaryID", primaryID);
        movie.put("id",id);
        movie.put("image",image);
        movie.put("name", name);
        movie.put("description", description);
        movie.put("year", year);
        movie.put("length",length);
        movie.put("rating",rating);
        movie.put("director",director);
        movie.put("stars",stars);
        movie.put("url",url);
        movie.put("selection",false);
        return movie;
    }



}
