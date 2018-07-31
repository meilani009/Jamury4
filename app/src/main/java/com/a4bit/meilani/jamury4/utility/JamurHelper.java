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
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_JAMURY;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WARNA;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WEIGHT_1;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WEIGHT_2;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WEIGHT_3;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.WarnaColumns.EKS_WARNA;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.Weight1Coloumns.Weight_1;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.Weight2Coloumns.Weight_2;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.Weight3Coloumns.Weight_3;

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
                jamurModel.setCook(cursor.getString(cursor.getColumnIndexOrThrow(COOK)));
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
        initialValues.put(COOK, jamurModel.getCook());

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
        String sql = "INSERT INTO "+ tableName + " ("+IMG_NAME + ", "+RANGE+ ", "+MUSHROOM_NAME+ ", "+STATUS+ ", "+EDIBILITY+ ", "+USABILITY+ ", "+HABITAT+ ", "+COLOR+ ", "+CAP_SHAPE+ ", "+COOK+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
        stmt.bindString(10,jamurModel.getCook());

        stmt.execute();
        stmt.clearBindings();

        Log.d("loggy", sql);
    }

    public double[][] getAllWarna(){
        Log.d("ekstrak", "mulai ambil warna");
        Cursor cursor = db.query(TABLE_WARNA,null,null,null,null,null,_ID + " ASC",null);
        cursor.moveToFirst();
        int n = cursor.getCount();
        Log.d("ekstrak", "total query: " +n);

        double[][] warna= new double[n][];
        WarnaModel warnaModel;
        if(cursor.getCount()>0){
            try {
                int i = 0;
                do {
                    String temp = cursor.getString(cursor.getColumnIndexOrThrow(EKS_WARNA));
                    String[] split = temp.split(",");

                    double[] tempD = new double[split.length];

                    for (int j = 0; j < split.length; j++) {
                        tempD[j] = Double.parseDouble(split[j]);
                    }
                    warna[i] = tempD;
                    Log.d("ekstrak", "data ke "+ i);
                    cursor.moveToNext();
                    i++;
                } while (!cursor.isAfterLast());
            }catch (Exception e){
                Log.d("ekstrak", e.toString());
            }
        }
        cursor.close();
        return warna;
    }

    public double[][] getAllBentuk(){
        Log.d("ekstrak", "mulai ambil bentuk");
        Cursor cursor = db.query(TABLE_BENTUK,null,null,null,null,null,_ID + " ASC",null);
        cursor.moveToFirst();
        int n = cursor.getCount();
        Log.d("ekstrak", "total query: " +n);

        double[][] bentuk= new double[n][];
        BentukModel bentukModel;
        if(cursor.getCount()>0){
            try {
                int i = 0;
                do {
                    String temp = cursor.getString(cursor.getColumnIndexOrThrow(EKS_BENTUK));
                    String[] split = temp.split(",");

                    double[] tempD = new double[split.length];

                    for (int j = 0; j < split.length; j++) {
                        tempD[j] = Double.parseDouble(split[j]);
                    }
                    bentuk[i] = tempD;
                    Log.d("ekstrak", "data ke "+ i);
                    cursor.moveToNext();
                    i++;
                } while (!cursor.isAfterLast());
            }catch (Exception e){
                Log.d("ekstrak", e.toString());
            }
        }
        cursor.close();
        return bentuk;
    }

    public double[][] getAllWeight1(){
        Log.d("ekstrak", "mulai ambil weight1");
        Cursor cursor = db.query(TABLE_WEIGHT_1,null,null,null,null,null,_ID + " ASC",null);
        cursor.moveToFirst();
        int n = cursor.getCount();
        Log.d("ekstrak", "total query: " +n);

        double[][] weight1= new double[n][];
        Weight1Model weight1model;
        if(cursor.getCount()>0){
            try {
                int i = 0;
                do {
                    String temp = cursor.getString(cursor.getColumnIndexOrThrow(Weight_1));
                    String[] split = temp.split(",");

                    double[] tempD = new double[split.length];

                    for (int j = 0; j < split.length; j++) {
                        tempD[j] = Double.parseDouble(split[j]);
                    }
                    weight1[i] = tempD;
                    Log.d("ekstrak", "data ke "+ i);
                    cursor.moveToNext();
                    i++;
                } while (!cursor.isAfterLast());
            }catch (Exception e){
                Log.d("ekstrak", e.toString());
            }
        }
        cursor.close();
        return weight1;
    }

    public double[][] getAllWeight2(){
        Log.d("ekstrak", "mulai ambil weight2");
        Cursor cursor = db.query(TABLE_WEIGHT_2,null,null,null,null,null,_ID + " ASC",null);
        cursor.moveToFirst();
        int n = cursor.getCount();
        Log.d("ekstrak", "total query: " +n);

        double[][] weight2= new double[n][];
        Weight2Model weight2model;
        if(cursor.getCount()>0){
            try {
                int i = 0;
                do {
                    String temp = cursor.getString(cursor.getColumnIndexOrThrow(Weight_2));
                    String[] split = temp.split(",");

                    double[] tempD = new double[split.length];

                    for (int j = 0; j < split.length; j++) {
                        tempD[j] = Double.parseDouble(split[j]);
                    }
                    weight2[i] = tempD;
                    Log.d("ekstrak", "data ke "+ i);
                    cursor.moveToNext();
                    i++;
                } while (!cursor.isAfterLast());
            }catch (Exception e){
                Log.d("ekstrak", e.toString());
            }
        }
        cursor.close();
        return weight2;
    }

    public double[][] getAllWeight3(){
        Log.d("ekstrak", "mulai ambil weight3");
        Cursor cursor = db.query(TABLE_WEIGHT_3,null,null,null,null,null,_ID + " ASC",null);
        cursor.moveToFirst();
        int n = cursor.getCount();
        Log.d("ekstrak", "total query: " +n);

        double[][] weight3= new double[n][];
        Weight3Model weight3model;
        if(cursor.getCount()>0){
            try {
                int i = 0;
                do {
                    String temp = cursor.getString(cursor.getColumnIndexOrThrow(Weight_3));
                    String[] split = temp.split(",");

                    double[] tempD = new double[split.length];

                    for (int j = 0; j < split.length; j++) {
                        tempD[j] = Double.parseDouble(split[j]);
                    }
                    weight3[i] = tempD;
                    Log.d("ekstrak", "data ke "+ i);
                    cursor.moveToNext();
                    i++;
                } while (!cursor.isAfterLast());
            }catch (Exception e){
                Log.d("ekstrak", e.toString());
            }
        }
        cursor.close();
        return weight3;
    }

    public void insertTransaction(String tableName, WarnaModel warnaModel){
        String sql = "INSERT INTO " + tableName + " ("+EKS_WARNA +") VALUES (?)";

        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1,warnaModel.getEks_warna());

        stmt.execute();
        stmt.clearBindings();

        Log.d("loggy",sql);
        Log.d("loggy",warnaModel.getEks_warna());

    }

    public void insertTransaction(String tableName, BentukModel bentukModel){
        String sql = "INSERT INTO " + tableName + " ("+EKS_BENTUK +") VALUES (?)";

        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, bentukModel.getEks_bentuk());

        stmt.execute();
        stmt.clearBindings();

        Log.d("loggy",sql);
        Log.d("loggy", bentukModel.getEks_bentuk());

    }

    public void insertTransaction(String tableName, Weight1Model weight1Model){
        String sql = "INSERT INTO " + tableName + " ("+Weight_1 +") VALUES (?)";

        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, weight1Model.getWeight_1());

        stmt.execute();
        stmt.clearBindings();

        Log.d("loggy",sql);
        Log.d("loggy", weight1Model.getWeight_1());

    }

    public void insertTransaction(String tableName, Weight2Model weight2Model){
        String sql = "INSERT INTO " + tableName + " ("+Weight_2 +") VALUES (?)";

        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, weight2Model.getWeight_2());

        stmt.execute();
        stmt.clearBindings();

        Log.d("loggy",sql);
        Log.d("loggy", weight2Model.getWeight_2());

    }

    public void insertTransaction(String tableName, Weight3Model weight3Model){
        String sql = "INSERT INTO " + tableName + " ("+Weight_3 +") VALUES (?)";

        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, weight3Model.getWeight_3());

        stmt.execute();
        stmt.clearBindings();

        Log.d("loggy",sql);
        Log.d("loggy", weight3Model.getWeight_3());

    }
}

