package com.gpsapp.shg.gpsapplicationtest.register;

/**
 * Created by HP on 08-03-2015.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAccess extends SQLiteOpenHelper
{
    /*    table for storing the user details            */
    public static final String USER_TABLE="userTable";
    public static final String USER_TABLE_COLUMN_1_MOBILE="mobilenumber";
    public static final String USER_TABLE_COLUMN_2_LATITUDE="latitude";
    public static final String USER_TABLE_COLUMN_3_LONGITUDE="longitude";
    /*     database file name and version               */
    private static final String DATABASE_NAME="CMAS.db";
    private static final int DATABASE_VERSION=1;

    /*create table sql query string for USER_TABLE   */
    public static final String CREATE_USER_TABLE="create table "+USER_TABLE+" ("+USER_TABLE_COLUMN_1_MOBILE+" text primary key,"+
            USER_TABLE_COLUMN_2_LATITUDE+" text,"+USER_TABLE_COLUMN_3_LONGITUDE+" text);";
    public DBAccess(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase)
    {
        sqliteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, int oldVersion, int newVersion)
    {
        Log.w(DBAccess.class.getName(),"Upgrading database from the old version "+oldVersion+
                " to the new version "+newVersion+" ,which will destroy the old data");
        sqliteDatabase.execSQL("drop table if exists "+USER_TABLE);
    }
}
