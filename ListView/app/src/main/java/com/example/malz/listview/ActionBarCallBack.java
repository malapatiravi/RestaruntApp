package com.example.malz.listview;

import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.example.malz.listview.RestaruntRecy.MyRecyclerViewAdapter;

import java.util.HashMap;

/**
 * Created by malz on 6/16/16.
 */
public class ActionBarCallBack implements ActionMode.Callback {
    int position;
    MovieData movieData;
    MyRecyclerViewAdapter mAdapter;
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.popup_menu,menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int id=item.getItemId();
        int newPos=position+1;
        Log.i("Registered Long","Click");
        switch(id){
            case R.id.delete_card:
                movieData.getMoviesList().remove(position);
                mAdapter.notifyItemRemoved(position);
                mode.finish();
                break;
            case R.id.copy_card:
                movieData.getMoviesList().add(newPos,(HashMap)((HashMap)movieData.getItem(position)).clone());
                mAdapter.notifyItemInserted(newPos);
                mode.finish();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    public ActionBarCallBack(int position, MovieData movieData,MyRecyclerViewAdapter recyclerviewAdapter)
    {
        this.position=position;
        this.movieData=movieData;
        this.mAdapter=recyclerviewAdapter;
    }
}
