package com.app.android.mapgeneral.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.android.mapgeneral.Activities.MainActivity;
import com.app.android.mapgeneral.R;
import com.devmarvel.creditcardentry.library.CardValidCallback;
import com.devmarvel.creditcardentry.library.CreditCard;
import com.devmarvel.creditcardentry.library.CreditCardForm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by malz on 8/13/16.
 */

public class AddCardFragment extends Fragment
{
  String user_id;
  String cardName = "cardholderName";
  String streetAddress = "cardholderStreetAddress";
  String cardHolderZip = "cardholderZip";
  String CVV = "CVV";
  String cardNumber = "cardNumber";
  String expMonth = "expirationMonth";
  String expYear = "expirationYear";
  JSONObject card_details = new JSONObject();
  String registerURL = "https://api.eatstreet.com/publicapi/v1/user/";
  View rootView;
  Button buttonAuthorize;
  Bundle args;
  private LinearLayout linearLayout;
  private CreditCardForm form;

  public AddCardFragment newInstance(String position, String origin)
  {

    AddCardFragment frag = new AddCardFragment();
    args = new Bundle();
    args.putString("user_id", position);
    args.putString("origin", origin);
    frag.setArguments(args);
    return frag;
  }

  CardValidCallback cardValidCallback = new CardValidCallback()
  {
    @Override
    public void cardValid(CreditCard card)
    {
      String str = card.getCardNumber();
      String t_exp = card.getExpDate();
      String t_zip = card.getZipCode();
      String t_cvv = card.getSecurityCode();
      String t_cardNum = str.replaceAll(" ", "");
      String[] str_array = t_exp.split("/");
      String t_month = str_array[0];
      String t_year = str_array[1];

      try
      {
        card_details.put(cardNumber, t_cardNum);
        card_details.put(expMonth, t_month);
        card_details.put(expYear, t_year);
        card_details.put(CVV, t_cvv);
        card_details.put(cardHolderZip, t_zip);
      }
      catch (JSONException e)
      {
        e.printStackTrace();
      }

      Log.d("Credit Card Fragment", "valid card: " + t_month + card);
      Toast.makeText(getActivity(), "Card valid and complete", Toast.LENGTH_SHORT).show();
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    user_id = getArguments().getString("user_id");

    //user_id=args.getString("user_id");
    Log.i("Received USer id", "" + user_id);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    super.onCreateView(inflater, container, savedInstanceState);
    rootView = inflater.inflate(R.layout.fragment_addcreditcard, container, false);
    final CreditCardForm noZipForm = (CreditCardForm) rootView.findViewById(R.id.card_and_zip_form);
    final EditText name_of_card_holder = (EditText) rootView.findViewById(R.id.card_holder_Name);
    final EditText add_of_card_holder = (EditText) rootView.findViewById(R.id.streetAddress);
    Button add_card_toeat = (Button) rootView.findViewById(R.id.btnAddCard);


    noZipForm.setOnCardValidCallback(cardValidCallback);
    noZipForm.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      @Override
      public void onFocusChange(View v, boolean hasFocus)
      {
        Log.d("Credit Card", v.getClass().getSimpleName() + " " + (hasFocus ? "gained" : "lost") + " focus. card valid: " + noZipForm.isCreditCardValid());
      }

    });
    noZipForm.getCreditCard().getCardNumber();
    add_card_toeat.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        String t_name_of_card_holder = name_of_card_holder.getText().toString();
        String t_address_of_card_holder = add_of_card_holder.getText().toString();
        try
        {
          card_details.put(cardName, t_name_of_card_holder);
          card_details.put(streetAddress, t_address_of_card_holder);
          RegisterData rg = new RegisterData();
          rg.sendJsonObj(card_details);
        }
        catch (JSONException e)
        {
          e.printStackTrace();
        }

      }
    });
    //Log.i("Exp Month",""+noZipForm.getCreditCard().getExpMonth());
    //final CreditCardForm clear = (CreditCardForm) rootView.findViewById(R.id.clear_test_form);
    rootView.findViewById(R.id.clear_test_button).setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        noZipForm.clearForm();
      }
    });

    return rootView;

  }

  public class RegisterData
  {
    public JSONObject converUserOutputToJSON(String jsonString)
    {
      JSONObject obj = null;
      try
      {
        obj = new JSONObject(jsonString);
        if (obj != null)
        {
          String first_name = (String) obj.get("firstName");
          String last_name = (String) obj.get("lastName");
          String user_apikey = (String) obj.get("apiKey");
          String user_email = (String) obj.get("email");
          Log.i("json converted", "" + first_name);
          Intent main_act = new Intent(getActivity().getBaseContext(), MainActivity.class);

          main_act.putExtra("image_url", "null");
          main_act.putExtra("user_email", "" + user_email);
          main_act.putExtra("user_id", user_apikey);
          main_act.putExtra("user_name", "" + first_name + " " + last_name);
          main_act.putExtra("origin", "eat");
          startActivity(main_act);

        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      return obj;
    }

    public void sendJsonObj(JSONObject json_user_card)
    {
      registerURL = registerURL + user_id + "/add-card";
      MyUploadUser task = new MyUploadUser(json_user_card, registerURL);
      task.execute(registerURL);
    }

    public void sendData(String email, String pWd, String firstName, String lastName, String phoneNumber)
    {
      JSONObject item = new JSONObject();

      try
      {
        //  item.put(EMAIL,email);
        item.put(user_id, pWd);
        //  item.put(FNAME,firstName);
        //  item.put(LNAME,lastName);
        //   item.put(PNUM,phoneNumber);
      }
      catch (JSONException e)
      {
        Log.i("RegisterMe", "JSON EXception");
      }
      registerURL = registerURL + user_id + "/add-card";
      MyUploadUser task = new MyUploadUser(item, registerURL);
      task.execute(registerURL);
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
               /* if(responseCode!=200)
                {
                    return null;
                }*/

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
      if (response_string != null)
      {
        //RegisterData rg=new RegisterData();
        //rg.converUserOutputToJSON(response_string);
        Log.i("response string", "" + response_string);
      }

      else
        return;
    }
  }
}
