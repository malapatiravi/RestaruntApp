package com.app.android.mapgeneral;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements RecyclerViewFragment.OnListItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Check for youtube service issues
        final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);

        if (result != YouTubeInitializationResult.SUCCESS) {
            //If there are any issues we can show an error dialog.
            result.getErrorDialog(this, 0).show();
        } else if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new MainActivityFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new FragmentGoogleMap().newInstance())
                    .commit();
        } else if (id == R.id.nav_gallery) {
            //check if the youtube app is installed in the device
            if (YouTubeIntents.canResolvePlayVideoIntent(this)) {
                //Opens the video in the YouTube app
                startActivity(YouTubeIntents.createPlayVideoIntent(this, "68A_HPYGdlk"));
                //startActivity(YouTubeIntents.createPlayVideoIntentWithOptions(this, "68A_HPYGdlk", false, true));
            }
        } else if (id == R.id.nav_slideshow) {
            //Opens in the StandAlonePlayer, defaults to fullscreen
            if (YouTubeIntents.canResolvePlayVideoIntent(this)) {
                //Opens in the StandAlonePlayer, defaults to fullscreen
                startActivity(YouTubeStandalonePlayer.createVideoIntent(this,
                        getString(R.string.google_maps_key), "68A_HPYGdlk", 50000, true, true));
            }
        } else if (id == R.id.nav_manage) {
            final Intent actIntent = new Intent(this, YouTubeActivity.class);
            actIntent.putExtra(YouTubeActivity.VIDEO_ID, "68A_HPYGdlk");
            startActivity(actIntent);
        } else if (id == R.id.nav_share) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new RecyclerViewFragment().newInstance(0))
                    .commit();
        } else if (id == R.id.nav_send) {
            final Intent emptyact = new Intent(this, EmptyActivity.class);
            startActivity(emptyact);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListItemSelected(int position, HashMap<String, ?> movie, int option) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, DetailViewFragment.newInstance(position, movie, 0))
                .addToBackStack(null)
                .commit();

    }
}