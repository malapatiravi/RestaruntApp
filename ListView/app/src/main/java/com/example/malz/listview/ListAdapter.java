package com.example.malz.listview;

/**
 * Created by malz on 6/9/16.
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder> {

    private List<String> mDatas;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public ListAdapter(Context context, List<String> mDatas) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int i) {
        itemViewHolder.mTextView.setText(mDatas.get(i));
        if(mOnItemClickListener != null) {

            if(!itemViewHolder.itemView.hasOnClickListeners()) {
                Log.e("ListAdapter", "setOnClickListener");
                itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = itemViewHolder.getPosition();
                        mOnItemClickListener.onItemClick(v, pos);
                    }
                });
                itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = itemViewHolder.getPosition();
                        mOnItemClickListener.onItemLongClick(v, pos);
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        Log.e("ListAdapter", "onCreateViewHolder");
        ItemViewHolder holder = new ItemViewHolder(mInflater.inflate(
                R.layout.item_layout, viewGroup, false));
        return holder;
    }


    public void add(int position, String value) {
        if(position > mDatas.size()) {
            position = mDatas.size();
        }
        if(position < 0) {
            position = 0;
        }
        mDatas.add(position, value);

        notifyItemInserted(position);
    }


    public String remove(int position) {
        if(position > mDatas.size()-1) {
            return null;
        }
        String value = mDatas.remove(position);
        notifyItemRemoved(position);
        return value;
    }


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textview);
        }
    }

}
