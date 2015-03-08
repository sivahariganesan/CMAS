package com.gpsapp.shg.gpsapplicationtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.gpsapp.shg.gpsapplicationtest.register.DBOperation;
import com.gpsapp.shg.gpsapplicationtest.register.GPSTracker;
import com.gpsapp.shg.gpsapplicationtest.register.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class DisplayMessageActivity extends ActionBarActivity implements View.OnClickListener
{
    public final static String EXTRA_MESSAGE = "com.gpsapp.shg.MESSAGE";
    private ProgressDialog progressDialog;
    JSONParser jsonParser=new JSONParser();
    private int success;
    private String message;
    private EditText textCrisis;
    private static String registerUrl="http://192.168.1.6/GPSJSON/sendCrisis.php";
    private static final String successTag="success";
    private DBOperation dbOperation=DBOperation.getInstance();
    private static Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view)
    {
        if (view.getId()==R.id.register)
        {
            mainContext=this;
            Log.e("My Error","Entered click");
            textCrisis=(EditText) findViewById(R.id.edit_crisis);
            new SendCrisis().execute();
        }
    }
    public void sendCrisis(View view)
    {
        if (view.getId()==R.id.send)
        {
            mainContext=this;
            Log.e("My Error","Entered click");
            textCrisis=(EditText) findViewById(R.id.edit_crisis);
            new GPSTracker().getLocation();
            new SendCrisis().execute();
        }
    }
    class SendCrisis extends AsyncTask<String,String,String>
    {
        String mobileNumber=dbOperation.getMobileNumber();
        String crisis=textCrisis.getText().toString();
        String latitude=dbOperation.getLatitude();
        String longitude=dbOperation.getLongitude();
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog=new ProgressDialog(DisplayMessageActivity.this);
            progressDialog.setMessage("Sending Crisis...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
        protected String doInBackground(String... args)
        {
            List<NameValuePair> parameters=new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("MobileNumber",mobileNumber));
            parameters.add(new BasicNameValuePair("crisisMessage",crisis));
            parameters.add(new BasicNameValuePair("latitude",latitude));
            parameters.add(new BasicNameValuePair("longitude",longitude));
            JSONObject jsonObject=jsonParser.makeHTTPRequest(registerUrl,"GET",parameters);
            try
            {
                success=jsonObject.getInt(successTag);
                message=jsonObject.getString("message");
                Log.e("My Error", message + success);
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
                Toast.makeText(getApplicationContext(), "Crisis Sent....", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mainContext, DisplayMessageActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Sending Crisis Failed....",Toast.LENGTH_LONG).show();
            }
        }
    }
}