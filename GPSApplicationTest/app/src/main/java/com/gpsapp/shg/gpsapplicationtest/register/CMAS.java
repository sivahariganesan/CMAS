package com.gpsapp.shg.gpsapplicationtest.register;

import android.app.Application;
import android.content.Context;

/**
 * Created by HP on 08-03-2015.
 */
public class CMAS extends Application
{
    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext()
    {
        return context;
    }
}
