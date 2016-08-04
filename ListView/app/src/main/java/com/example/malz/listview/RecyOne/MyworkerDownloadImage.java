package com.example.malz.listview.RecyOne;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.malz.listview.MyUtility;

import java.lang.ref.WeakReference;

/**
 * Created by malz on 7/8/16.
 */
public class MyworkerDownloadImage extends AsyncTask<String, String, Bitmap> {
    private final WeakReference<Activity> parentReference;
    private final WeakReference<ImageView> imageViewWeakReference;
    private final WeakReference<TextView> textViewWeakReference;
    public MyworkerDownloadImage(final Activity parent, final ImageView imageView, final TextView textView)
    {
        parentReference = new WeakReference<Activity>(parent);
        imageViewWeakReference=new WeakReference<ImageView>(imageView);
        textViewWeakReference = new WeakReference<TextView>(textView);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... urls) {

        Bitmap bitmap = null;
        for (String url : urls) {
            bitmap = MyUtility.downloadImageusingHTTPGetRequest(url);
            if (bitmap != null) {

            }
        }
        return bitmap;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        if(textViewWeakReference!=null)
        {
            final TextView textView = textViewWeakReference.get();
            if(textView!=null)
            {
                textView.setText(values[0]);
            }
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        if(imageViewWeakReference!=null && bitmap!=null)
        {
            final ImageView imageView = imageViewWeakReference.get();
            if(imageView!=null)
            {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onCancelled(Bitmap bitmap) {
        if(textViewWeakReference!=null)
        {
            final TextView textView=textViewWeakReference.get();
            if(textView!=null)
            {
                textView.setText("Task Cancelled");
            }
        }
    }
}
