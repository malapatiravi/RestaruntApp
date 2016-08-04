package com.example.malz.listview.RecyOne;

import android.os.AsyncTask;

import com.example.malz.listview.RestaruntRecy.MyRecyclerViewAdapter;

import java.lang.ref.WeakReference;

/**
 * Created by malz on 7/8/16.
 */
public class MyDownloadJsonAsyncTask extends AsyncTask<String, Void, MovieDataJSon>{
    private final WeakReference<MyRecyclerViewAdapter> adapterReference;
    public MyDownloadJsonAsyncTask(MyRecyclerViewAdapter adapter)
    {
        adapterReference = new WeakReference<MyRecyclerViewAdapter>(adapter);
    }

    @Override
    protected MovieDataJSon doInBackground(String... urls) {
    MovieDataJSon threadMovieData=new MovieDataJSon();
        for(String url:urls)
        {
            threadMovieData.downloadMovieDataJson(url);
        }
        return threadMovieData;
    }

    @Override
    protected void onPostExecute(MovieDataJSon threadMovieData) {
        for(int i=0;i<threadMovieData.getSize();i++)
        {
            //threadMovieData.addItem();
        }
        if(adapterReference != null)
        {
            final MyRecyclerViewAdapter adapter=adapterReference.get();
            if(adapter!=null)
            {
                adapter.notifyDataSetChanged();
            }
        }

    }
}
