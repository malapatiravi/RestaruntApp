package com.app.android.mapgeneral;

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
public class SignInEat extends Fragment {
    private String registerURL = "https://api.eatstreet.com/publicapi/v1/signin";
    String EMAIL="email";
    String PWD="password";

    public static SignInEat newInstance(int position, String title)
    {
        SignInEat frag=new SignInEat();
        Bundle args=new Bundle();
        args.putInt("position",position);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView;
        rootView=(View)inflater.inflate(R.layout.fragment_eatsignin,container,false);
        String apiKey1 = getString(R.string.eat_street_api_key);
        final EditText email=(EditText)rootView.findViewById(R.id.edit_text_email);
        final EditText pWd=(EditText)rootView.findViewById(R.id.edit_text_password);
        Button signin_eat=(Button)rootView.findViewById(R.id.btnRegisterMe);
        signin_eat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String tEmail= email.getText().toString();
                String tPwd= pWd.getText().toString();
                RegisterData rg=new RegisterData();
                rg.sendData(tEmail, tPwd);

            }
        });

        return rootView;
    }



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
                Log.i("data here",""+ URLEncoder.encode(json_m.toString(),"UTF-8"));
                //wr.writeUTF(URLEncoder.encode(json_m.toString(),"UTF-8"));
                wr.writeBytes(json_m.toString());

                wr.flush();
                wr.close();
                int responseCode = postConn.getResponseCode();
                if(responseCode!=200)
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "User Details Invalid", Toast.LENGTH_LONG).show();

                    return null;
                }
                Log.i("Resonse code is","Response code"+responseCode);
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
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            if(jsonString!=null)
            {
                RegisterData rg=new RegisterData();
                rg.converUserOutputToJSON(jsonString);
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
                    main_act.putExtra("user_id",""+first_name+" "+last_name);
                    main_act.putExtra("user_apikey",user_apikey);
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
        public void sendData(String email, String pWd)
        {
            JSONObject item = new JSONObject();

            try
            {
                item.put(EMAIL,email);
                item.put(PWD,pWd);

            }
            catch(JSONException e)
            {
                Log.i("RegisterMe", "JSON EXception");
            }
            MyUploadUser task=new MyUploadUser(item, registerURL);
            task.execute(registerURL);

        }

    }
}
