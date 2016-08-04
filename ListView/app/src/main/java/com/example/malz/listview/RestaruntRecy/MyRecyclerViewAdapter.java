package com.example.malz.listview.RestaruntRecy;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malz.listview.R;
import com.example.malz.listview.RecyOne.MyDownloadImageAsyncTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by malz on 6/8/16.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>
{
    private List<Map<String,?>> mDataset;
    private Context mContext;

    OnItemClickListener1 mItemClickListener;

    @Override
    public int getItemViewType(int position) {
        return position%2;
    }

    //constructor
    public MyRecyclerViewAdapter(Context myContext, List<Map<String,?>> myDataset)
    {
        mContext=myContext;
        mDataset =myDataset;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Map<String,?> movie = mDataset.get(position);
        HashMap movie = (HashMap) mDataset.get(position);
        holder.bindMovieData(movie);
    }
    public void insertItem(int position, HashMap movie)
    {

    }
    public void delete()
    {
      /*  for(Map<String,?> map:mDataset)
        {
            int pos=mDataset.indexOf(map);
            mDataset.remove(pos);
            Log.i("Actual position is","pos is:"+map.get("name")+pos);
           HashMap movie=(HashMap)mDataset.get(pos);
            if((boolean)movie.get("selection")!=false)
            {
                Log.i("New Delete","Deleting "+pos+movie.get("name"));
                mDataset.remove(pos);
                notifyItemRemoved(pos);
            }

        }*/

        for(int position =getItemCount()-1;position>=0;position=position-1)
        {
            Log.i("Size of :","is"+position);
            HashMap<String,Boolean> item=(HashMap<String,Boolean>) mDataset.get(position);
            boolean b=item.get("selection");
            if(b==true)
            {
                mDataset.remove(position);
                notifyItemRemoved(position);
            }


            //HashMap movie=(HashMap)mDataset.get(position);
            //if((boolean)movie.get("selection")!=false)
            //{
                //Log.i("New Delete","Deleting "+position+movie.get("name"));
              //  mDataset.remove(position);
                //notifyItemRemoved(position);
            //}

        }

    }
    public void removeItem(int position, HashMap movie)
    {
        mDataset.remove(position);
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View v;
        ViewHolder vh=null;
       switch(viewType)
        {
            case 1:
            v=LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_add,parent,false);
                vh=new ViewHolder(v);
                return vh;
            case 0:
                v=LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_main,parent,false);
                vh=new ViewHolder(v);
                return vh;
        }
        return vh;
        /*
         View v;
        Log.i("View type is:","Type: "+viewType);
        v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_add,parent,false);

        ViewHolder vh=new ViewHolder(v);
        return vh;*/
    }

    public interface OnItemClickListener1{
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);

        void onOverflowMenuClick(View v, int position);
    }
    public void setOnClickListenerAdapter(final OnItemClickListener1 mItemClickListener1)
    {
        this.mItemClickListener=mItemClickListener1;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView vIcon;
        public TextView vTitle;
        public TextView vDescription;
        public CheckBox vCheckBox;
        public View vmain;
        public CardView vLay;
        public ImageView sMenu;
        public ViewHolder(View v)
        {


            super(v);
            vmain=(CardView)v.findViewById(R.id.card_view_inner);
            //v.setBackground();
            vLay=(CardView)v.findViewById(R.id.card_view);
            vIcon=(ImageView)v.findViewById(R.id.icon);
            vTitle=(TextView)v.findViewById(R.id.titLe);
            vDescription=(TextView)v.findViewById(R.id.description);
            vCheckBox=(CheckBox)v.findViewById(R.id.seLector);
            sMenu=(ImageView)v.findViewById(R.id.overflow_card);
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(mItemClickListener!=null)
                    {
                        int position=getPosition();
                        mItemClickListener.onItemClick(v,position);

                    }
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {

                    if(mItemClickListener!=null)
                    {
                        mItemClickListener.onItemLongClick(v,getPosition());
                    }
                    return true;
                }
            });
            if(sMenu!=null)
            {
                sMenu.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onOverflowMenuClick(v, getPosition());
                        }
                    }

            });
            }
        }
        public void bindMovieData( final HashMap movie)
        //public void bindMovieData( final Map<String,?> movie)
        {
            if(movie!=null)
            {
                final float rating1 = Float.parseFloat(movie.get("rating").toString());
                vTitle.setText((String) movie.get("name"));

                Log.d("Bind movie",(String)movie.get("stars"));
                //vIcon.setImageResource((Integer)movie.get("image"));
                MyDownloadImageAsyncTask task=new MyDownloadImageAsyncTask(vIcon);

                task.execute((String)movie.get("url"));
                vDescription.setText((String) movie.get("description"));
                vCheckBox.setChecked((Boolean)movie.get("selection"));
                vDescription.setTextColor(Color.BLACK);
                if(rating1>=8)
                    vmain.setBackgroundColor(Color.GRAY);
                else
                    vmain.setBackgroundColor(Color.LTGRAY);
                //vmain.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
                //vLay.(true);
                //vCheckBox.setTag(movieData.get(position))
                vCheckBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        // if(movie.get("Selection") !=true)
                        // {
                        if(cb.isChecked())
                        {
                            movie.put("selection",true);

                            Toast.makeText(
                                    v.getContext(),
                                    "Clicked on Checkbox: True "+movie.get("selection"), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            movie.put("selection", false);
                            Toast.makeText(
                                    v.getContext(),
                                    "Clicked on Checkbox: False ", Toast.LENGTH_SHORT).show();
                        }
                        // movie.put("Selection",true);
                        // Student contact = (Student) cb.getTag();

                        // contact.setSelected(cb.isChecked());
                        // stList.get(pos).setSelected(cb.isChecked());

                        // Toast.makeText(
                        //        v.getContext(),
                        //        "Clicked on Checkbox: True ", Toast.LENGTH_LONG).show();
                        // }
                        // else
                        // {
                        //    movie.put("Selection",false);
//
                        //    Toast.makeText(
                        //            v.getContext(),
                        //          "Clicked on Checkbox: False ", Toast.LENGTH_LONG).show();
                        //}

                    }
                });

          /*  vCheckBox.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Log.i("Clicked",(String) movie.get("name"));
                    //CheckBox cb=(CheckBox)v;
                    //movie.put("Selection",true);

                }
            });*/
            }

        }

    }


}