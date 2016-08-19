package com.app.android.mapgeneral;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.app.android.mapgeneral.Fragments.MenuDetailViewFragment;
import com.app.android.mapgeneral.Objects.Shared;

import java.util.HashMap;
import java.util.List;

public class RestaurantMenuAdapter extends RecyclerView.Adapter<RestaurantMenuAdapter.ViewHolder>
{
  private static final String TAG = "RestaurantMenu";
  private List<HashMap<String, String>> mDataset;
  private CheckChangedListener checkedListener;
  public RestaurantMenuAdapter(List<HashMap<String, String>> data, MenuDetailViewFragment context)
  {
    mDataset = data;
    try
    {
      checkedListener = context;
    }
    catch (ClassCastException e)
    {
      logMessage("Parent have not implemented the checked listener");
      e.printStackTrace();
    }
  }
  public class ViewHolder extends RecyclerView.ViewHolder
  {
    public TextView vName;
    public TextView vPrice;
    public TextView vDesc;
    public Switch vSwitch;

    public ViewHolder(View v)
    {
      super(v);
      vName = (TextView) v.findViewById(R.id.item_name);
      vPrice = (TextView) v.findViewById(R.id.item_price);
      vDesc = (TextView) v.findViewById(R.id.item_desc);
      vSwitch = (Switch) v.findViewById(R.id.item_switch);

      if (vSwitch != null)
      {
        vSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
          {
            HashMap<String, String> item = mDataset.get(getAdapterPosition());
            checkedListener.onCheckChanged(isChecked, mDataset.get(getAdapterPosition()).get("apikey").toString());
//            if (isChecked)
//            {
//              // The toggle is enabled
//              checkedListener.onCheckChanged(isChecked, mDataset.get(getAdapterPosition()).get("apikey").toString());
//              //item.put("selection", "true");
//              //logMessage(mDataset.get(getAdapterPosition()).get("apikey").toString()+" selected");
//            }
//            else
//            {
//              // The toggle is disabled
//              item.put("selection", "false");
//              logMessage(mDataset.toString());
//              logMessage(mDataset.get(getAdapterPosition()).get("apikey").toString()+" not selected");
//            }
          }
        });
      }
    }

    public void bindMovieData(HashMap<String, String> map)
    {
      vName.setText(map.get("name"));
      vPrice.setText(map.get("price"));
      String desc = map.get("description");
      if (desc != null && !desc.equals("null"))   vDesc.setText(desc);
      else                                        vDesc.setVisibility(View.GONE);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card_fragment, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position)
  {
    HashMap<String, String> map = mDataset.get(position);
    holder.bindMovieData(map);
  }

  @Override
  public int getItemCount()
  {
    return mDataset.size();
  }
  public interface CheckChangedListener
  {
    void onCheckChanged(boolean isChecked, String apiKey);
  }

  public void logMessage(String msg) {if (Shared.DEBUG_MODE) Log.i(TAG, msg);}

}
