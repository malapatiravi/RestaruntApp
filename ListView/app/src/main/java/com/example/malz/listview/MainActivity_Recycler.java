package com.example.malz.listview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.malz.listview.RecyOne.RecyclerViewFragment;

import java.util.HashMap;

/**
 * Created by malz on 6/9/16.
 */
public class MainActivity_Recycler extends AppCompatActivity implements RecyclerViewFragment.detailInter{

    Fragment mContent;
    DetailFragmentMain mContent1;
    boolean doubleBackToExitPressedOnce = false;
    //RecyclerViewFragment mContent;
    private void runFadeInAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fadein);
        a.reset();
        FrameLayout ll = (FrameLayout) findViewById(R.id.recycler_container);
        ll.clearAnimation();
        ll.startAnimation(a);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_recycler);
        Log.i("On create activity","View Pager");
        this.setTitle("Recycler Activity");
     /*   if(savedInstanceState!=null)
        {
            mContent = (RecyclerViewFragment) getSupportFragmentManager().getFragment(savedInstanceState,"mContent");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recycler_container, mContent).commit();
        }
        else
        {*/

        //}

        //runFadeInAnimation();
        if (savedInstanceState != null)
        {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }
        else
        {
            mContent = new RecyclerViewFragment();//.newInstance(R.id.activity_recycler);
        }

        //RecyclerViewFragment frag=new RecyclerViewFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recycler_container, mContent).commit();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mContent.isAdded())
        getSupportFragmentManager().putFragment(outState,"mContent",mContent);
        if(mContent1 !=null)
        if (mContent1.isAdded())
            getSupportFragmentManager().putFragment(outState,"mContent1",mContent1);

    }

    public void onDetailsSelect(int position, HashMap<String, ?> movie) {
        Log.i("In","On item selected");
        doubleBackToExitPressedOnce=false;
        //DetailFragmentMain mContent1 = DetailFragmentMain.newInstance(position);
        mContent = DetailFragmentMain.newInstance(position, movie);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recycler_container, mContent).addToBackStack(null).commit();
    }
  /*  public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_recycler, menu);
        return true;
    }*/
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();


       // mContent = (RecyclerViewFragment) getSupportFragmentManager().getFragment(savedInstanceState,"mContent");
        //getSupportFragmentManager().beginTransaction()
          //      .replace(R.id.recycler_container, mContent).commit();

        //RecyclerViewFragment frag=new RecyclerViewFragment();
        mContent=new RecyclerViewFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.recycler_container,mContent).commit();
        Log.i("BackPressed","back");
    }
}
