package com.app.android.mapgeneral;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YouTubeActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener, View.OnClickListener {

    public static String VIDEO_ID = "video id";
    private String mVideoId;
    private int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayer mPlayer;
    private Button pause;
    private Button play;
    private Button defaultsel;
    private Button minimalsel;
    private Button chromelesssel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);
        final Bundle arguments = getIntent().getExtras();
        if (arguments != null && arguments.containsKey(VIDEO_ID)) {
            mVideoId = arguments.getString(VIDEO_ID);
        }
        //button setting
        pause = (Button)findViewById(R.id.pause);
        pause.setOnClickListener(this);
        play = (Button)findViewById(R.id.play);
        play.setOnClickListener(this);
        defaultsel = (Button)findViewById(R.id.defaultstyle);
        defaultsel.setOnClickListener(this);
        minimalsel = (Button)findViewById(R.id.minimalstyle);
        minimalsel.setOnClickListener(this);
        chromelesssel = (Button)findViewById(R.id.chromelessstyle);
        chromelesssel.setOnClickListener(this);

        final YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.youTubePlayerView);
        playerView.initialize(getString(R.string.google_maps_key), this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restored) {

        mPlayer=youTubePlayer;

        //This flag tells the player to switch to landscape when in fullscreen, it will also return to portrait
        //when leaving fullscreen
        mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        //This flag tells the player to automatically enter fullscreen when in landscape. Since we don't have
        //landscape layout for this activity, this is a good way to allow the user rotate the video player.
        mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);

        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        if (mVideoId != null) {
            if (restored) {
                mPlayer.play();
            } else {
                mPlayer.loadVideo(mVideoId);
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            //Handle the failure
            Toast.makeText(this, "onInitializationFailure", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        //Null check the player
        if (mPlayer != null) {
            if (v == play) {
                mPlayer.play();
            } else if (v == pause) {
                mPlayer.pause();
            } else if (v == defaultsel){
                mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            } else if (v== chromelesssel){
                mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
            } else if (v==minimalsel){
                mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            }
        }
    }
}
