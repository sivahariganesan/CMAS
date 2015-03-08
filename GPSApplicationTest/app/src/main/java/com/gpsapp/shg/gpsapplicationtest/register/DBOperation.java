package com.gpsapp.shg.gpsapplicationtest.register;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.math.BigInteger;

/**
 * Created by HP on 08-03-2015.
 */
public class DBOperation
{
    private static SQLiteDatabase database;
    private static DBAccess dbAccess;
    private static DBOperation dbOperation=new DBOperation(CMAS.getContext());
    private ContentResolver contentResolver;

    public DBOperation(Context context)
    {
        dbAccess=new DBAccess(context);
        contentResolver=context.getContentResolver();
    }
    /*
     * @return the instance of the DBOperation class
     */
    public static DBOperation getInstance()
    {
        return dbOperation;
    }
    /*
     * @return the instance of SQLiteDatabase
     */
    public static SQLiteDatabase getDatabase()
    {
        return database;
    }
    /*
     * Opens the database connection
     */
    public void openConnection()
    {
        if(database==null || !database.isOpen())
        {
            database=dbAccess.getWritableDatabase();
        }
    }
    /*
     * Closes the database connection
     */
    public void closeConnection()
    {
        database.close();
    }
    /*
	 * @param mobile number of the user
	 * Inserts the user details into the USER_TABLE
	 */
    public void registerUser(String mobileNumber,String latitude,String longitude)
    {
        try
        {
            openConnection();
            ContentValues values=new ContentValues();
            values.put(DBAccess.USER_TABLE_COLUMN_1_MOBILE,mobileNumber);
            values.put(DBAccess.USER_TABLE_COLUMN_2_LATITUDE,latitude);
            values.put(DBAccess.USER_TABLE_COLUMN_3_LONGITUDE,longitude);
            database.insert(DBAccess.USER_TABLE, null, values);
        }
        finally
        {
            //closeConnection();
        }
    }
    /*
     * @return true if there is any user registered
     * @return false if there is no user registered
     */
    public boolean isUserRegistered()
    {
        openConnection();
        boolean operationResult=false;
        Cursor cursor=null;
        try
        {
            cursor=database.rawQuery("select "+DBAccess.USER_TABLE_COLUMN_1_MOBILE+" from "+DBAccess.USER_TABLE, null);
            if(cursor.moveToNext())
            {
                operationResult=true;
            }
            else
            {
                operationResult=false;
            }
        }
        finally
        {
            if(cursor!=null)
            {
                cursor.close();
                //closeConnection();
            }
        }
        return operationResult;
    }
    /*
	 * @return the mobile number of the user
	 */
    public String getMobileNumber()
    {
        String mobileNumber="";
        Cursor cursor=null;
        try
        {
            openConnection();
            cursor=database.rawQuery("select "+DBAccess.USER_TABLE_COLUMN_1_MOBILE+" from "+DBAccess.USER_TABLE, null);
            if(cursor.moveToNext())
            {
                mobileNumber=cursor.getString(0);
            }
            else
            {
                mobileNumber=null;
            }
        }
        finally
        {
            if(cursor!=null)
            {
                cursor.close();
            }
            //closeConnection();
        }
        return mobileNumber;
    }
    public String getLatitude()
    {
        String latitude="";
        Cursor cursor=null;
        try
        {
            openConnection();
            cursor=database.rawQuery("select "+DBAccess.USER_TABLE_COLUMN_2_LATITUDE+" from "+DBAccess.USER_TABLE, null);
            if(cursor.moveToNext())
            {
                latitude=cursor.getString(0);
            }
            else
            {
                latitude=null;
            }
        }
        finally
        {
            if(cursor!=null)
            {
                cursor.close();
            }
            //closeConnection();
        }
        return latitude;
    }
    public String getLongitude()
    {
        String longitude="";
        Cursor cursor=null;
        try
        {
            openConnection();
            cursor=database.rawQuery("select "+DBAccess.USER_TABLE_COLUMN_3_LONGITUDE+" from "+DBAccess.USER_TABLE, null);
            if(cursor.moveToNext())
            {
                longitude=cursor.getString(0);
            }
            else
            {
                longitude=null;
            }
        }
        finally
        {
            if(cursor!=null)
            {
                cursor.close();
            }
            //closeConnection();
        }
        return longitude;
    }
    /*
	 * @param latitude of location
	 * @param longitude of location
	 * Updates location of the user
	 */
    public void updateUserLocation(String latitude,String longitude)
    {
        try
        {
            openConnection();
            ContentValues values=new ContentValues();
            values.put(DBAccess.USER_TABLE_COLUMN_2_LATITUDE, latitude);
            values.put(DBAccess.USER_TABLE_COLUMN_3_LONGITUDE,longitude);
            Log.e("DataBase Error","Test");
            database.update(DBAccess.USER_TABLE,values,null,null);
        }
        finally
        {
            //closeConnection();
        }
    }
    /*
	 * @return true if the database is opened
	 * @return false if the database is closed
	 */
    public boolean isOpen()
    {
        if(database.isOpen())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}