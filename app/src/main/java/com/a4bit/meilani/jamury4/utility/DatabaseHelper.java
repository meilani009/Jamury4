package com.a4bit.meilani.jamury4.utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.provider.BaseColumns._ID;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.BentukColumns.EKS_BENTUK;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.CAP_SHAPE;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.COLOR;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.COOK;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.EDIBILITY;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.HABITAT;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.IMG_NAME;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.MUSHROOM_NAME;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.RANGE;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.STATUS;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.USABILITY;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_BENTUK;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WEIGHT_1;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WEIGHT_2;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WEIGHT_3;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.WarnaColumns.EKS_WARNA;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.Weight1Coloumns.Weight_1;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_JAMURY;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WARNA;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.Weight2Coloumns.Weight_2;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.Weight3Coloumns.Weight_3;

/**
 * Created by root on 2/23/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "dbjamur";
    private static final int DATABASE_VERSION = 1;

    public static String CREATE_TABLE_JAMURY = "create table "
            + TABLE_JAMURY + " (" + _ID + " integer primary key autoincrement, "+
            IMG_NAME + " text not null, "+
            RANGE + " text not null, "+
            MUSHROOM_NAME + " text not null, "+
            STATUS + " text not null, "+
            EDIBILITY + " text not null, "+
            USABILITY + " text not null, "+
            HABITAT + " text not null, "+
            COLOR + " text not null, "+
            CAP_SHAPE + " text not null, "+
            COOK + " text not null);";

    public static String CREATE_TABLE_WARNA = "create table " + TABLE_WARNA + " (" + _ID + " integer primary key autoincrement, " +
            EKS_WARNA + " text not null);";

    public static String CREATE_TABLE_BENTUK = "create table " + TABLE_BENTUK + " (" + _ID + " integer primary key autoincrement, " +
            EKS_BENTUK + " text not null);";

    public static String CREATE_TABLE_WEIGHT1 = "create table " + TABLE_WEIGHT_1 + " ("+ _ID + " integer primary key autoincrement, " +
            Weight_1 + " text not null);";

    public static String CREATE_TABLE_WEIGHT2 = "create table " + TABLE_WEIGHT_2 + " ("+ _ID + " integer primary key autoincrement, " +
            Weight_2 + " text not null);";

    public static String CREATE_TABLE_WEIGHT3 = "create table " + TABLE_WEIGHT_3 + " ("+ _ID + " integer primary key autoincrement, " +
            Weight_3 + " text not null);";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_JAMURY);
        db.execSQL(CREATE_TABLE_WARNA);
        db.execSQL(CREATE_TABLE_BENTUK);
        db.execSQL(CREATE_TABLE_WEIGHT1);
        db.execSQL(CREATE_TABLE_WEIGHT2);
        db.execSQL(CREATE_TABLE_WEIGHT3);

        Log.d("loggy", CREATE_TABLE_JAMURY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_JAMURY);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_WARNA);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_BENTUK);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_WEIGHT_1);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_WEIGHT_2);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_WEIGHT_3);

        onCreate(db);
    }
}
