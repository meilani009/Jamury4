package com.a4bit.meilani.jamury4.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.WarnaColumns.EKS_WARNA;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WARNA;

/**
 * Created by root on 3/9/18.
 */

public class WarnaHelper {
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public WarnaHelper(Context context){
        this.context = context;
    }

    public WarnaHelper open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public ArrayList<WarnaModel> getAllData(){
        Cursor cursor = db.query(TABLE_WARNA,null,null,null,null,null,_ID + "ASC",null);
        cursor.moveToFirst();
        ArrayList<WarnaModel> arrayList = new ArrayList<>();
        WarnaModel warnaModel;
        if(cursor.getCount()>0){
            do{
                warnaModel = new WarnaModel();
                warnaModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                warnaModel.setEks_warna(cursor.getDouble(cursor.getColumnIndexOrThrow(EKS_WARNA)));

                arrayList.add(warnaModel);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(String tableName,WarnaModel warnaModel){
        ContentValues initialValues = new ContentValues();
        initialValues.put(EKS_WARNA , warnaModel.getEks_warna());
        return db.insert(tableName,null,initialValues);

    }

    public void beginTransaction(){
        db.beginTransaction();
    }

    public void setTransaction(){
        db.setTransactionSuccessful();
    }

    public void endTransaction(){
        db.endTransaction();
    }

    public void insertTransaction(String tableName, WarnaModel warnaModel){
        String sql = "INSERT INTO" + tableName + " ("+EKS_WARNA +") VALUES (?)";

        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindDouble(1,warnaModel.getEks_warna());

        stmt.execute();
        stmt.clearBindings();

        Log.d("loggy",sql);

    }




}
