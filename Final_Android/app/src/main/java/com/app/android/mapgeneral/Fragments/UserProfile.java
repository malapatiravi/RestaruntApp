package com.app.android.mapgeneral.Fragments;

import android.content.Intent;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.android.mapgeneral.Objects.LightSensorService;
import com.app.android.mapgeneral.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by malz on 8/13/16.
 */
public class UserProfile extends Fragment
{
  TextView tvHomePhone_g = null;
  TextView tvWorkPhone_g = null;
  EditText tvHomePhoneEdit_g = null;
  EditText tvWorkPhoneEdit_g = null;
  TextView tvHomeS_g = null;
  TextView tvWorkS_g = null;

  TextView tvEmail_g = null;
  EditText tvEmailEdit_g = null;
  TextView tvEmailAdd_g = null;

  TextView tvAddress_g = null;
  EditText tvAddreddEdit_g = null;
  TextView tvAddView_g = null;

  TextView tvCreditCard_g = null;
  TextView tvCrediView_g = null;
  JSONObject updateUser = new JSONObject();
  Bundle args;
  String getUserUrl = "https://api.eatstreet.com/publicapi/v1/user/";
  String updateUserUrl = "https://api.eatstreet.com/publicapi/v1/update-user/";
  String user_id;
  JSONObject resultUserDetails = new JSONObject();
  boolean editStatus = false;
  Intent serviceIntent;// = new Intent(getActivity(), LightSensorService.class);


  public UserProfile newInstance(String _user_id, String origin)
  {

    UserProfile frag = new UserProfile();
    args = new Bundle();
    args.putString("user_id", _user_id);
    args.putString("origin", origin);
    frag.setArguments(args);
    return frag;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    user_id = getArguments().getString("user_id");
    //getUserUrl=getUserUrl+"485ca34bedf9153e7ecdb0c1c698d2cee41ee9406039e889";
    getUserUrl = getUserUrl + user_id;
    updateUserUrl = updateUserUrl + user_id;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    super.onCreateView(inflater, container, savedInstanceState);
    View rootView = (View) inflater.inflate(R.layout.activity_scrolling, container, false);
    Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

    serviceIntent = new Intent(getActivity(), LightSensorService.class);
    FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
      }
    });

    //Lets get all the views
    final TextView tvHomePhone = (TextView) rootView.findViewById(R.id.tvNumberHome);
    tvHomePhone_g = tvHomePhone;
    final TextView tvWorkPhone = (TextView) rootView.findViewById(R.id.tvNumberWork);
    tvWorkPhone_g = tvWorkPhone;
    final EditText tvHomePhoneEdit = (EditText) rootView.findViewById(R.id.tvNumberHomeEdit);
    tvHomePhoneEdit_g = tvHomePhoneEdit;
    final EditText tvWorkPhoneEdit = (EditText) rootView.findViewById(R.id.tvNumberWorkEdit);
    tvWorkPhoneEdit_g = tvWorkPhoneEdit;
    final TextView tvHomeS = (TextView) rootView.findViewById(R.id.tvHome);
    tvHomeS_g = tvHomeS;
    final TextView tvWorkS = (TextView) rootView.findViewById(R.id.tvWork);
    tvWorkS_g = tvWorkS;

    final TextView tvEmail = (TextView) rootView.findViewById(R.id.tvEmail);
    tvEmail_g = tvEmail;
    final EditText tvEmailEdit = (EditText) rootView.findViewById(R.id.tvEmailEdit);
    tvEmailEdit_g = tvEmailEdit;
    final TextView tvEmailAdd = (TextView) rootView.findViewById(R.id.tvEmailAdd);
    tvEmailAdd_g = tvEmailAdd;

    final TextView tvAddress = (TextView) rootView.findViewById(R.id.tvAddress);
    tvAddress_g = tvAddress;
    final EditText tvAddreddEdit = (EditText) rootView.findViewById(R.id.tvAddressEdit);
    tvAddreddEdit_g = tvAddreddEdit;
    final TextView tvAddView = (TextView) rootView.findViewById(R.id.tvAddView);
    tvAddView_g = tvAddView;

    final TextView tvCreditCard = (TextView) rootView.findViewById(R.id.tvCreditCardNumber);
    tvCreditCard_g = tvCreditCard;
    final TextView tvCrediView = (TextView) rootView.findViewById(R.id.creditCardView);
    tvCrediView_g = tvCrediView;

    fab.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        if (editStatus == false)
        {
          editStatus = true;
          tvWorkS.setVisibility(View.GONE);
          tvHomeS.setVisibility(View.GONE);
          tvHomePhone.setVisibility(View.GONE);
          tvHomePhoneEdit.setVisibility(View.VISIBLE);
          tvWorkPhone.setVisibility(View.GONE);
          tvWorkPhoneEdit.setVisibility(View.VISIBLE);

          tvEmail.setVisibility(View.GONE);
          tvEmailEdit.setVisibility(View.VISIBLE);
          tvEmailAdd.setVisibility(View.GONE);

          tvAddress.setVisibility(View.GONE);
          tvAddreddEdit.setVisibility(View.VISIBLE);
          tvAddView.setVisibility(View.GONE);
          enableService();
        }
        else if (editStatus == true)
        {
          disableService();
          try
          {
            String userName = tvHomePhoneEdit.getText().toString();
            tvHomePhone.setText(userName);
            String[] strAr = userName.split(" ");
            if (strAr.length < 2)
            {
              updateUser.put("firstName", strAr[0]);
              updateUser.put("lastName", strAr[1]);
              //last name does not exist
            }
            else
            {
              updateUser.put("firsName", strAr[0]);
              updateUser.put("lastName", strAr[1]);
              Log.i("USer Name from Array", "" + strAr[0] + strAr[1]);
            }

            String userPhone = tvWorkPhoneEdit.getText().toString();
            tvWorkPhone.setText(userPhone);
            updateUser.put("phone", userPhone);
            Log.i("User phone number is", "" + userPhone);

            String userEmail = tvEmailEdit.getText().toString();
            tvEmail.setText(userEmail);
            MyUploadUser task = new MyUploadUser(updateUser, updateUserUrl);
            task.execute(updateUserUrl);


          }
          catch (JSONException e)
          {
            e.printStackTrace();
          }
          editStatus = false;
          tvWorkS.setVisibility(View.VISIBLE);
          tvHomeS.setVisibility(View.VISIBLE);
          tvHomePhone.setVisibility(View.VISIBLE);
          tvHomePhoneEdit.setVisibility(View.GONE);
          tvWorkPhone.setVisibility(View.VISIBLE);
          tvWorkPhoneEdit.setVisibility(View.GONE);

          tvEmail.setVisibility(View.VISIBLE);
          tvEmailEdit.setVisibility(View.GONE);
          tvEmailAdd.setVisibility(View.VISIBLE);

          tvAddress.setVisibility(View.VISIBLE);
          tvAddreddEdit.setVisibility(View.GONE);
          tvAddView.setVisibility(View.VISIBLE);
        }
      }
    });
    MyDownloadUser task = new MyDownloadUser(getUserUrl);
    task.execute(getUserUrl);
    return rootView;
  }

  private void enableService()
  {
    Log.i("TAG", "starting service...");
    getActivity().startService(serviceIntent);
//    Intent i = new Intent(LightSensorService.MY_SERVICE);
//    i.setPackage("com.app.android.mapgeneral.Fragments");
//    getActivity().startService(i);
  }

  private void disableService()
  {
    Log.i("TAG", "stopping service...");
    getActivity().stopService(serviceIntent);
//    Intent i = new Intent(LightSensorService.MY_SERVICE);
//    i.setPackage("com.app.android.mapgeneral.Fragments");
//    getActivity().stopService(i);
  }
  public void updateView(String _jsonString)
  {
    JSONObject jsonObject = null;
    try
    {
      jsonObject = new JSONObject(_jsonString);
      tvHomePhone_g.setText("" + jsonObject.getString("firstName") + " " + jsonObject.getString("lastName"));
      tvHomePhoneEdit_g.setText("" + jsonObject.getString("firstName") + " " + jsonObject.getString("lastName"));

      tvWorkPhone_g.setText("" + jsonObject.getString("phone"));
      tvWorkPhoneEdit_g.setText("" + jsonObject.getString("phone"));

      tvEmail_g.setText("" + jsonObject.getString("email"));
      tvEmailEdit_g.setText("" + jsonObject.getString("email"));
      JSONArray addressArray = jsonObject.getJSONArray("savedAddresses");

      tvAddreddEdit_g.setText("" + addressArray.getJSONObject(0).getString("streetAddress"));
      tvAddress_g.setText("" + addressArray.getJSONObject(0).getString("streetAddress"));
      Log.i("Addres is:", "" + addressArray.getJSONObject(0).getString("streetAddress"));


      Log.i("User Name", "" + jsonObject.getString("firstName"));
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }


  }

  public class MyUploadUser extends AsyncTask<String, String, String>
  {
    public JSONObject json_m;
    public String url_s;

    public MyUploadUser(JSONObject _json, String _url)
    {
      json_m = _json;
      url_s = _url;

    }

    @Override
    protected String doInBackground(String... strings)
    {
      String response_string = "";
      try
      {
        URL url = new URL(url_s);
        HttpURLConnection postConn = (HttpURLConnection) url.openConnection();

        // postConn.setDoInput(true);
        postConn.setRequestMethod("POST");
        postConn.setRequestProperty("X-Access-Token", "5d26f33f30a7418c");

        postConn.setRequestProperty("Content-Type", "application/json");
        postConn.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(postConn.getOutputStream());
        //wr.write(URLEncoder.encode(item.toString(),"UTF-8"));
        Log.i("data here", "" + URLEncoder.encode(json_m.toString(), "UTF-8"));
        //wr.writeUTF(URLEncoder.encode(json_m.toString(),"UTF-8"));
        wr.writeBytes(json_m.toString());

        wr.flush();
        wr.close();
        int responseCode = postConn.getResponseCode();
        Log.i("Resonse code is", "Response code" + responseCode);
        if (responseCode != 200)
        {
          getActivity().runOnUiThread(new Runnable()
          {
            @Override
            public void run()
            {
              Toast.makeText(getActivity(), "The user details are bad", Toast.LENGTH_SHORT).show();
            }
          });

          return null;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(postConn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null)
        {
          response.append(inputLine);
        }
        in.close();
        response_string = response.toString();
        System.out.println(response_string);
      }
      catch (Exception e)
      {
        Log.i("Http", "Http Exception" + e);
      }
      return response_string;
    }

    @Override
    protected void onPostExecute(String response_string)
    {
      super.onPostExecute(response_string);
            /*if(response_string!=null)
            {
                RegisterData rg=new RegisterData();
                rg.converUserOutputToJSON(response_string);
            }

            else*/
      return;
    }
  }

  public class MyDownloadUser extends AsyncTask<String, String, String>
  {

    public String url_s;

    public MyDownloadUser(String _url)
    {

      url_s = _url;
      Log.i("the pasedURL", "URL is" + url_s);

    }

    @Override
    protected String doInBackground(String... strings)
    {
      String response_string = "";
      try
      {
        URL url = new URL(url_s);
        HttpURLConnection postConn = (HttpURLConnection) url.openConnection();

        // postConn.setDoInput(true);
        postConn.setRequestMethod("GET");
        postConn.setRequestProperty("X-Access-Token", "5d26f33f30a7418c");

        int responseCode = postConn.getResponseCode();
        if (responseCode != 200)
        {
          Log.i("The response code is ", "Response code:" + responseCode);
          getActivity().runOnUiThread(new Runnable()
          {
            @Override
            public void run()
            {
              Toast.makeText(getActivity(), "The user details are bad", Toast.LENGTH_SHORT).show();
            }
          });
          return null;
        }
        Log.i("Resonse code is", "Response code" + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(postConn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null)
        {
          response.append(inputLine);
        }
        in.close();
        response_string = response.toString();
        resultUserDetails = new JSONObject(response_string);
        System.out.println("USerDetail-NEw is" + response_string);

      }
      catch (Exception e)
      {
        Log.i("Http", "Http Exception" + e);
      }
      return response_string;
    }

    @Override
    protected void onPostExecute(String jsonString)
    {
      super.onPostExecute(jsonString);
      if (jsonString != null)
      {
        updateView(jsonString);
        //Do noothing
      }

      else
        return;

    }
  }

}
