package com.example.malz.listview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.malz.listview.RecyOne.RecyclerViewFragment;

import java.util.HashMap;

/**
 * Created by malz on 6/16/16.
 */
public class Task3Activity extends AppCompatActivity implements RecyclerViewFragment.detailInter {

    Fragment mContent;
    DetailFragmentMain mContent1;
    boolean doubleBackToExitPressedOnce = false;
    Toolbar mToolbarMain, mToolbarSub;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity__action_bar);
        Log.i("Action Bar","Task2");
        this.setTitle("Recycler Activity");

        actionBar=getSupportActionBar();
        mToolbarMain=(Toolbar)findViewById(R.id.toolbar_top);
        setSupportActionBar(mToolbarMain);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarSub=(Toolbar)findViewById(R.id.toolbar_bottom);

        mToolbarSub.inflateMenu(R.menu.popup_menu1);
        seUpToolbarItemSelected();
        //this.actionBar.setDisplayHomeAsUpEnabled(true);


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
            if(mContent instanceof  DetailFragmentMain)
            {
                mToolbarSub.setVisibility(View.GONE);
                Log.i("DeatialFragment", "Reloaded");
            }
           /* if(mContent.getArguments()!=null)
            {
                Bundle args=mContent.getArguments();
                if(args.get("details")=="details")
                {
                    mToolbarSub.setVisibility(View.GONE);
                    Log.i("DeatialFragment", "Reloaded");
                }


            }*/
        }
        else
        {
            mContent = new RecyclerViewFragment();//.newInstance(R.id.activity_recycler);
        }

        //RecyclerViewFragment frag=new RecyclerViewFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recycler_container, mContent).commit();




    }

    void seUpToolbarItemSelected()
    {
        mToolbarSub.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
               switch(id)
               {
                   case R.id.refresh:
                       Toast.makeText(getApplicationContext(),"Refreshing",Toast.LENGTH_SHORT).show();
                       
                       return true;
                   case R.id.shop:
                       Toast.makeText(getApplicationContext(),"Added to Cart",Toast.LENGTH_SHORT).show();
                       return true;
                   default:
                       break;


               }
                return false;
            }
        });
        mToolbarSub.setNavigationIcon(R.drawable.ic_open_in_new_black_24dp);
        mToolbarSub.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mToolbarSub.setVisibility(View.GONE);
            }
        });
    }

    public void onDetailsSelect(int position, HashMap<String, ?> movie) {
        Log.i("In","On item selected");
        doubleBackToExitPressedOnce=false;
        //DetailFragmentMain mContent1 = DetailFragmentMain.newInstance(position);
        Bundle args=new Bundle();
        args.putString("details","details");
        mContent = DetailFragmentMain.newInstance(position, movie);
        mContent.setArguments(args);
        mToolbarSub.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recycler_container, mContent).addToBackStack(null).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch(id)
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_edit:
                mToolbarSub.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

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
        mToolbarSub.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.recycler_container,mContent).commit();
        Log.i("BackPressed","back");
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


    /*Animations are as below*/
    private void runFadeInAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fadein);
        a.reset();
        FrameLayout ll = (FrameLayout) findViewById(R.id.recycler_container);
        ll.clearAnimation();
        ll.startAnimation(a);
    }

}
