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
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.CAP_SHAPE;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.COLOR;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.EDIBILITY;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.HABITAT;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.IMG_NAME;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.MUSHROOM_NAME;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.RANGE;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.STATUS;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.DictionaryColumns.USABILITY;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_JAMURY;

/**
 * Created by root on 2/23/18.
 */

public class JamurHelper {
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public JamurHelper(Context context){
        this.context = context;
    }

    public JamurHelper open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public ArrayList<JamurModel> getAllData(){
        Cursor cursor = db.query(TABLE_JAMURY, null, null, null, null, null, _ID + " ASC", null);
        cursor.moveToFirst();
        ArrayList<JamurModel> arrayList = new ArrayList<>();
        JamurModel jamurModel;
        if(cursor.getCount()>0){
            do{
                jamurModel = new JamurModel();
                jamurModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                jamurModel.setImg_name(cursor.getString(cursor.getColumnIndexOrThrow(IMG_NAME)));
                jamurModel.setRange(cursor.getString(cursor.getColumnIndexOrThrow(RANGE)));
                jamurModel.setMushroom_name(cursor.getString(cursor.getColumnIndexOrThrow(MUSHROOM_NAME)));
                jamurModel.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(STATUS)));
                jamurModel.setEdibility(cursor.getString(cursor.getColumnIndexOrThrow(EDIBILITY)));
                jamurModel.setUsability(cursor.getString(cursor.getColumnIndexOrThrow(USABILITY)));
                jamurModel.setHabitat(cursor.getString(cursor.getColumnIndexOrThrow(HABITAT)));
                jamurModel.setColor(cursor.getString(cursor.getColumnIndexOrThrow(COLOR)));
                jamurModel.setCap_shape(cursor.getString(cursor.getColumnIndexOrThrow(CAP_SHAPE)));


                arrayList.add(jamurModel);
                cursor.moveToNext();
            }while(!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(String tableName, JamurModel jamurModel){
        ContentValues initialValues = new ContentValues();
        initialValues.put(IMG_NAME, jamurModel.getImg_name());
        initialValues.put(RANGE, jamurModel.getRange());
        initialValues.put(MUSHROOM_NAME, jamurModel.getMushroom_name());
        initialValues.put(STATUS, jamurModel.getStatus());
        initialValues.put(EDIBILITY, jamurModel.getEdibility());
        initialValues.put(USABILITY, jamurModel.getUsability());
        initialValues.put(HABITAT, jamurModel.getHabitat());
        initialValues.put(COLOR, jamurModel.getColor());
        initialValues.put(CAP_SHAPE, jamurModel.getCap_shape());
        return db.insert(tableName, null, initialValues);
    }

    public void beginTransaction(){
        db.beginTransaction();
    }

    public void setTransactionSuccess(){
        db.setTransactionSuccessful();
    }

    public void endTransaction(){
        db.endTransaction();
    }

    public void insertTransaction(String tableName, JamurModel jamurModel){
        String sql = "INSERT INTO "+ tableName + " ("+IMG_NAME + ", "+RANGE+ ", "+MUSHROOM_NAME+ ", "+STATUS+ ", "+EDIBILITY+ ", "+USABILITY+ ", "+HABITAT+ ", "+COLOR+ ", "+CAP_SHAPE+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, jamurModel.getImg_name());
        stmt.bindString(2, jamurModel.getRange());
        stmt.bindString(3, jamurModel.getMushroom_name());
        stmt.bindString(4, jamurModel.getStatus());
        stmt.bindString(5, jamurModel.getEdibility());
        stmt.bindString(6, jamurModel.getUsability());
        stmt.bindString(7, jamurModel.getHabitat());
        stmt.bindString(8, jamurModel.getColor());
        stmt.bindString(9, jamurModel.getCap_shape());

        stmt.execute();
        stmt.clearBindings();

        Log.d("loggy", sql);
    }
}
