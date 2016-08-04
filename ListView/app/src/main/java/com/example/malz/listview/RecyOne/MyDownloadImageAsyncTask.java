package com.example.malz.listview.RecyOne;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.malz.listview.MyUtility;

import java.lang.ref.WeakReference;

/**
 * Created by malz on 7/8/16.
 */
public class MyDownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;

    public MyDownloadImageAsyncTask(ImageView imv)
    {
        imageViewReference= new WeakReference<ImageView>(imv);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap bitmap=null;
        for(String url:urls)
        {
            bitmap= MyUtility.downloadImageusingHTTPGetRequest(url);

        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        if(imageViewReference!=null && bitmap!=null)
        {
            final ImageView imageView=imageViewReference.get();
            if(imageView!=null)
            {
                imageView.setImageBitmap(bitmap);
            }
        }

    }

}
