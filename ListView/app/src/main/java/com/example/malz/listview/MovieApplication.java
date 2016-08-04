package com.example.malz.listview;

import android.app.Application;
import com.firebase.client.Firebase;


/**
 * Created by malz on 7/14/16.
 */
public class MovieApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        // other setup code
    }

}
