package com.gpsapp.shg.gpsapplicationtest.register;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by HP on 01-03-2015.
 */
public class JSONParser
{
    static InputStream inputStream=null;
    static JSONObject jsonObject=null;
    static String json="";
    public JSONParser()
    {
    }
    public JSONObject makeHTTPRequest(String url, String method, List<NameValuePair> parameters)
    {
        try
        {
            if(method=="POST")
            {
                DefaultHttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(parameters));

                HttpResponse httpResponse=httpClient.execute(httpPost);
                HttpEntity httpEntity=httpResponse.getEntity();
                inputStream=httpEntity.getContent();
            }
            else if (method=="GET")
            {
                DefaultHttpClient httpClient=new DefaultHttpClient();
                String paramString= URLEncodedUtils.format(parameters,"utf-8");
                url+="?"+paramString;
                HttpGet httpGet=new HttpGet(url);

                HttpResponse httpResponse=httpClient.execute(httpGet);
                HttpEntity httpEntity=httpResponse.getEntity();
                inputStream=httpEntity.getContent();
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
            StringBuilder stringBuilder=new StringBuilder();
            String line=null;
            while ((line=reader.readLine())!=null)
            {
                stringBuilder.append(line+"\n");
            }
            inputStream.close();
            json=stringBuilder.toString();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            Log.e("My Error", "JSON init started");
            jsonObject=new JSONObject(json);
            Log.e("My Error", "JSON initialised");
        }
        catch (JSONException e)
        {
            Log.e("My Error line 110",e.getLocalizedMessage());
            e.printStackTrace();
        }
        return jsonObject;
    }
}
