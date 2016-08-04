package com.example.malz.listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.malz.listview.RestaruntRecy.MyRecyclerViewAdapter;

/**
 * Created by malz on 6/9/16.
 */
public class RecyclerSingleActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    //final LinearLayoutManager mLayoutManager=null;
    private MyRecyclerViewAdapter mRecyclerViewAdapter;
    MovieData movieData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclersingle);
        mRecyclerView=(RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager=new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerViewAdapter = new MyRecyclerViewAdapter(this,movieData.getMoviesList());



    }
}
