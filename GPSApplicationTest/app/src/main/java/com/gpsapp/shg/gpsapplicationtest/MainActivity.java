package com.gpsapp.shg.gpsapplicationtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gpsapp.shg.gpsapplicationtest.register.CMAS;
import com.gpsapp.shg.gpsapplicationtest.register.DBOperation;
import com.gpsapp.shg.gpsapplicationtest.register.GPSTracker;
import com.gpsapp.shg.gpsapplicationtest.register.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    public final static String EXTRA_MESSAGE = "com.gpsapp.shg.MESSAGE";
    private ProgressDialog progressDialog;
    JSONParser jsonParser=new JSONParser();
    private int success;
    private DBOperation dbOperation=DBOperation.getInstance();
    private static Context mainContext;
    private String message,latitude,longitude;
    private EditText textMobileNumber;
    private Button btnRegister;
    private static String registerUrl="http://192.168.1.6/GPSJSON/register.php";
    private static final String successTag="success";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final String PREFS_NAME = "PrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (!dbOperation.isUserRegistered())
        {
            //here the app is being launched for first time, launch your screen
            settings.edit().putBoolean("app_first_time", false).commit();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            textMobileNumber= (EditText) findViewById(R.id.edit_message);
            btnRegister= (Button) findViewById(R.id.register);
        }
        else
        {
            super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_display_message);
            Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onClick(View view)
    {
        if (view.getId()==R.id.register)
        {
            mainContext=this;
            new RegisterMobile().execute();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void sendMessage(View view)
    {
        if (view.getId()==R.id.register)
        {
            mainContext=this;
            Location location=new GPSTracker().getLocation();
            latitude=Double.toString(location.getLatitude());
            longitude=Double.toString(location.getLongitude());
            dbOperation.registerUser(textMobileNumber.getText().toString(),latitude,longitude);
            new RegisterMobile().execute();
        }
    }
    class RegisterMobile extends AsyncTask<String,String,String>
    {
        String mobileNumber=textMobileNumber.getText().toString();
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Registering New Mobile Number("+mobileNumber+")...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
        protected String doInBackground(String... args)
        {
            List<NameValuePair> parameters=new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("MobileNumber",mobileNumber));
            parameters.add(new BasicNameValuePair("latitude",latitude));
            parameters.add(new BasicNameValuePair("longitude",longitude));
            JSONObject jsonObject=jsonParser.makeHTTPRequest(registerUrl,"GET",parameters);
            try
            {
                 success=jsonObject.getInt(successTag);
                 message=jsonObject.getString("message");
                 Log.e("My Error",message+success);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String url)
        {
            progressDialog.dismiss();
            if (success==1)
            {
                Log.e("My Error","Success");
                Toast.makeText(getApplicationContext(),"New Mobile Number Saved....",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mainContext, DisplayMessageActivity.class);
                EditText editText = (EditText) findViewById(R.id.edit_message);
                String message = editText.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"New Mobile Number Failed To Save....",Toast.LENGTH_LONG).show();
            }
        }
    }
}
