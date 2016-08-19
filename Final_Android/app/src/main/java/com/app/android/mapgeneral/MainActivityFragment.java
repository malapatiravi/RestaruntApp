package com.app.android.mapgeneral;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 * only to show how to implement youtubethumbnailview in a fragment
 */
public class MainActivityFragment extends Fragment implements YouTubeThumbnailView.OnInitializedListener {

    public MainActivityFragment() {
    }
/*******************************/
    private Map<View, YouTubeThumbnailLoader> mLoaders = new HashMap<>();
    private YouTubeThumbnailView thumb;
/*******************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        thumb = (YouTubeThumbnailView) rootview.findViewById(R.id.imageView_thumbnail);
        thumb.setTag("68A_HPYGdlk");
        thumb.initialize(getContext().getString(R.string.google_maps_key), this);
        return rootview;
    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView view, final YouTubeThumbnailLoader loader) {
        loader.setVideo((String) view.getTag());
        loader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                loader.release();
            }
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView,
                                         YouTubeThumbnailLoader.ErrorReason errorReason) {
                Log.d("onInitialization Error ", "onThumbnailError");
            }
        });
    }
    @Override
    public void onInitializationFailure(YouTubeThumbnailView thumbnailView, YouTubeInitializationResult errorReason) {
        final String errorMessage = errorReason.toString();
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onStop(){
        super.onStop();
    }
}
