package com.app.android.mapgeneral;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.android.mapgeneral.Activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by malz on 8/12/16.
 */
public class RegisterMe extends Fragment {
    private String registerURL = "https://api.eatstreet.com/publicapi/v1/register-user";

    String EMAIL="email";
    String PWD="password";
    String FNAME="firstName";
    String LNAME="lastName";
    String PNUM="phone";
    public static RegisterMe newInstance(int position, String title)
    {
        RegisterMe frag=new RegisterMe();
        Bundle args=new Bundle();
        args.putInt("position",position);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_register_me);
        View rootView;
        rootView=inflater.inflate(R.layout.fragment_register_me, container, false);
        String apiKey1 = getString(R.string.eat_street_api_key);
        final EditText email=(EditText)rootView.findViewById(R.id.edit_text_email);
        final EditText pWd=(EditText)rootView.findViewById(R.id.edit_text_password);
        final EditText firstName=(EditText)rootView.findViewById(R.id.firstName);
        final EditText lastName=(EditText)rootView.findViewById(R.id.lastName);
        final EditText pNumber=(EditText)rootView.findViewById(R.id.phoneNumber);
        Button click_registerme=(Button)rootView.findViewById(R.id.btnRegisterMe);

        click_registerme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tEmail= email.getText().toString();
                String tPwd= pWd.getText().toString();
                String tFirstName= firstName.getText().toString();
                String tLastName= lastName.getText().toString();
                String tPhone= pNumber.getText().toString();
                Log.i("Received Emain is:",""+tEmail);
                RegisterData rg=new RegisterData();
                rg.sendData(tEmail, tPwd, tFirstName, tLastName, tPhone);
            }
        });
        return rootView;
    }
    /*// protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register_me);
        String apiKey1 = getString(R.string.eat_street_api_key);
        final EditText email=(EditText)findViewById(R.id.edit_text_email);
        final EditText pWd=(EditText)findViewById(R.id.edit_text_password);
        final EditText firstName=(EditText)findViewById(R.id.firstName);
        final EditText lastName=(EditText)findViewById(R.id.lastName);
        final EditText pNumber=(EditText)findViewById(R.id.phoneNumber);

        Button click_registerme=(Button)findViewById(R.id.btnRegisterMe);
        click_registerme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String tEmail= email.getText().toString();
                String tPwd= pWd.getText().toString();
                String tFirstName= firstName.getText().toString();
                String tLastName= lastName.getText().toString();
                String tPhone= pNumber.getText().toString();
                Log.i("Received Emain is:",""+tEmail);
                RegisterData rg=new RegisterData();
                rg.sendData(tEmail, tPwd, tFirstName, tLastName, tPhone);
            }
        });
    }*/


    public class MyUploadUser extends AsyncTask<String, String, String>
    {

        public  JSONObject json_m;
        public String url_s;
        public MyUploadUser(JSONObject _json, String _url)
        {
            json_m=_json;
            url_s=_url;

        }
        @Override
        protected String doInBackground(String... strings) {
            String response_string="";
            try
            {
                URL url = new URL(url_s);
                HttpURLConnection postConn=(HttpURLConnection)url.openConnection();

               // postConn.setDoInput(true);
                postConn.setRequestMethod("POST");
                postConn.setRequestProperty("X-Access-Token","5d26f33f30a7418c");

                postConn.setRequestProperty("Content-Type","application/json");
                postConn.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(postConn.getOutputStream());
                //wr.write(URLEncoder.encode(item.toString(),"UTF-8"));
                Log.i("data here",""+URLEncoder.encode(json_m.toString(),"UTF-8"));
                //wr.writeUTF(URLEncoder.encode(json_m.toString(),"UTF-8"));
                wr.writeBytes(json_m.toString());

                wr.flush();
                wr.close();
                int responseCode = postConn.getResponseCode();
                Log.i("Resonse code is","Response code"+responseCode);
                if(responseCode!=200)
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "User Details Not in Correct Format", Toast.LENGTH_LONG).show();

                    return null;
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(postConn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                response_string=response.toString();
                System.out.println(response_string);
            }
            catch(Exception e)
            {
                Log.i("Http", "Http Exception"+e);
            }
            return response_string;
        }

        @Override
        protected void onPostExecute(String response_string) {
            super.onPostExecute(response_string);
            if(response_string!=null)
            {
                RegisterData rg=new RegisterData();
                rg.converUserOutputToJSON(response_string);
            }

            else
                return;
        }
    }
    public class RegisterData
    {
        public JSONObject converUserOutputToJSON(String jsonString)
        {JSONObject obj=null;
            try
            {
                obj = new JSONObject(jsonString);
                if(obj!=null)
                {
                    String first_name = (String) obj.get("firstName");
                    String last_name = (String) obj.get("lastName");
                    String user_apikey=(String)obj.get("apiKey");
                    String user_email=(String)obj.get("email");
                    Log.i("json converted",""+first_name);
                    Intent main_act=new Intent(getActivity().getBaseContext(), MainActivity.class);

                    main_act.putExtra("image_url","null");
                    main_act.putExtra("user_email",""+user_email);
                    main_act.putExtra("user_id",user_apikey);
                    main_act.putExtra("user_name",""+first_name+" "+last_name);
                    main_act.putExtra("origin","eat");
                    startActivity(main_act);

                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return obj;
        }

        public void sendData(String email, String pWd, String firstName, String lastName, String phoneNumber)
        {
            JSONObject item = new JSONObject();

            try
            {
                item.put(EMAIL,email);
                item.put(PWD,pWd);
                item.put(FNAME,firstName);
                item.put(LNAME,lastName);
                item.put(PNUM,phoneNumber);
            }
            catch(JSONException e)
            {
                Log.i("RegisterMe", "JSON EXception");
            }
            MyUploadUser task=new MyUploadUser(item, registerURL);
            task.execute(registerURL);
           /* try
            {
                URL url = new URL(registerURL);
                HttpURLConnection postConn=(HttpURLConnection)url.openConnection();
                postConn.setDoOutput(true);
                postConn.setDoInput(true);
                postConn.setRequestProperty("X-Access-Token","5d26f33f30a7418c");
                postConn.setRequestMethod("POST");

                postConn.setRequestProperty("Content-Type","application/json");
                DataOutputStream wr = new DataOutputStream(postConn.getOutputStream());
                //wr.write(URLEncoder.encode(item.toString(),"UTF-8"));
                wr.writeUTF(URLEncoder.encode(item.toString(),"UTF-8"));
                wr.flush();
                wr.close();
                InputStream in = new BufferedInputStream(postConn.getInputStream());
                String inputLine;
                StringBuffer response = new StringBuffer();

               Log.i("output",""+in.toString());
                in.close();
            }

            catch (IOException e)
            {
                Log.i("Http", "Http Exception"+e);
            }*/



        }

    }
}
