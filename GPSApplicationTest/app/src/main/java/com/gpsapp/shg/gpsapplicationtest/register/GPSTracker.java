package com.gpsapp.shg.gpsapplicationtest.register;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by HP on 08-03-2015.
 */
public class GPSTracker extends Service implements LocationListener
{
    private Location location;
    private final Context context;
    private boolean isGPSEnabled,isNetworkEnabled,canGetLocation;
    private double latitude,longitude;
    private DBOperation dbOperation=DBOperation.getInstance();
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000*10;//10 km
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 30;
    protected LocationManager locationManager;
    public GPSTracker()
    {
        context=CMAS.getContext();
        getLocation();
    }
    public Location getLocation()
    {
        try
        {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetworkEnabled)
            {
                canGetLocation = true;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d("Location", "Network Location");
                if (locationManager != null)
                {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (location != null)
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
            else if (isGPSEnabled)
            {
                canGetLocation = true;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d("Location", "GPS Location");
                if (locationManager != null)
                {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (location != null)
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
            else
            {
                showSettingsAlert();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return location;
    }
    @Override
    public void onLocationChanged(Location location)
    {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        dbOperation.updateUserLocation(Double.toString(latitude),Double.toString(longitude));
    }
    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    public void stopUsingGPS()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

}
