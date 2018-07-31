package com.a4bit.meilani.jamury4;

/**
 * Created by root on 2/13/18.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.a4bit.meilani.jamury4.utility.AppPreference;
import com.a4bit.meilani.jamury4.utility.BentukModel;
import com.a4bit.meilani.jamury4.utility.JamurHelper;
import com.a4bit.meilani.jamury4.utility.JamurModel;
import com.a4bit.meilani.jamury4.utility.WarnaModel;
import com.a4bit.meilani.jamury4.utility.Weight1Model;
import com.a4bit.meilani.jamury4.utility.Weight2Model;
import com.a4bit.meilani.jamury4.utility.Weight3Model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_BENTUK;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_JAMURY;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WARNA;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WEIGHT_1;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WEIGHT_2;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WEIGHT_3;

/**
 * Created by Meilani Wulandari on 20-Jan-18.
 */

public class SplashActivity extends Activity {
    @BindView(R.id.progress_bar)ProgressBar progressBar;
    @BindView(R.id.txt_progress)TextView txt_progress;

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashfile);
        ButterKnife.bind(this);

//        handler=new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        },3000);

        new LoadData(TABLE_JAMURY, TABLE_WARNA, TABLE_BENTUK, TABLE_WEIGHT_1, TABLE_WEIGHT_2 , TABLE_WEIGHT_3).execute();
    }

    private class LoadData extends AsyncTask<Void, Integer, Void>{
        final String TAG = LoadData.class.getSimpleName();
        JamurHelper jamurHelper;
        AppPreference appPreference;

        String tableJamur,tableWarna, tableBentuk, tableWeight1, tableWeight2 , tableWeight3;
        double progress;
        double maxprogress = 100;

        public LoadData(String tableJamur, String tableWarna, String tableBentuk, String tableWeight1 , String tableWeight2 , String tableWeight3){
            this.tableJamur = tableJamur;
            this.tableWarna = tableWarna;
            this.tableBentuk = tableBentuk;
            this.tableWeight1 = tableWeight1;
            this.tableWeight2 = tableWeight2;
            this.tableWeight3 = tableWeight3;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Boolean firstRun = appPreference.getFirstRun();
            if(firstRun){
                ArrayList<JamurModel> jamurModels = preLoadRaw();
                ArrayList<WarnaModel> warnaModels = preLoadColorEks();
                ArrayList<BentukModel> bentukModels = preLoadBentukEks();
                ArrayList<Weight1Model> weight1Models = preLoadWeight1();
                ArrayList<Weight2Model> weight2Models = preLoadWeight2();
                ArrayList<Weight3Model> weight3Models = preLoadWeight3();




                progress = 30;
                publishProgress((int)progress);
                Double progressMaxInsert = 80.0;
                Double progressDiff = (progressMaxInsert - progress) / (jamurModels.size() + warnaModels.size()+ bentukModels.size()+ weight1Models.size()+ weight2Models.size() + weight3Models.size());

                jamurHelper.open();
                jamurHelper.beginTransaction();


                try {
                    for (JamurModel model : jamurModels) {
                        jamurHelper.insertTransaction(tableJamur, model);
                        progress += progressDiff;
                        publishProgress((int) progress);
                        Log.d("loggy", "insert success");
                    }

                    for (WarnaModel model : warnaModels){
                        jamurHelper.insertTransaction(tableWarna,model);
//                        progress+=progressDiff;
//                        publishProgress((int)progress);
                        Log.d("loggy","insert warna success");
                    }

                    for (BentukModel model : bentukModels){
                        jamurHelper.insertTransaction(tableBentuk,model);
//                        progress+=progressDiff;
//                        publishProgress((int)progress);
                        Log.d("loggy","insert bentuk success");
                    }

                    for (Weight1Model model : weight1Models){
                        jamurHelper.insertTransaction(tableWeight1,model);
//                        progress+=progressDiff;
//                        publishProgress((int)progress);
                        Log.d("loggy","insert weight1 success");
                    }

                    for (Weight2Model model : weight2Models){
                        jamurHelper.insertTransaction(tableWeight2,model);
//                        progress+=progressDiff;
//                        publishProgress((int)progress);
                        Log.d("loggy","insert weight2 success");
                    }

                    for (Weight3Model model : weight3Models){
                        jamurHelper.insertTransaction(tableWeight3,model);
//                        progress+=progressDiff;
//                        publishProgress((int)progress);
                        Log.d("loggy","insert weight3 success");
                    }

                    jamurHelper.setTransactionSuccess();
                }catch(Exception e){
                    Log.e(TAG, "dict err insertTransaction");
                    Log.e(TAG, e.toString());
                }
                jamurHelper.endTransaction();

                jamurHelper.close();


                appPreference.setFirstRun(false);
                publishProgress((int)maxprogress);
            } else {
                try {
                    synchronized(this){
                        this.wait(1000);

                        publishProgress(50);

                        this.wait(1000);
                        publishProgress((int)maxprogress);
                    }
                }catch(Exception e){

                }
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            jamurHelper = new JamurHelper(SplashActivity.this);
            appPreference = new AppPreference(SplashActivity.this);
            if(appPreference.getFirstRun())
                txt_progress.setVisibility(View.VISIBLE);
        }



        @Override
        protected void onProgressUpdate(Integer... values){
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public ArrayList<JamurModel> preLoadRaw() {
        ArrayList<JamurModel> jamurModels = new ArrayList<>();
        String line = null;
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict;

            raw_dict = res.openRawResource(R.raw.database_new);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            int count = 0;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\\|");

                JamurModel jamurModel;

                jamurModel = new JamurModel(splitstr[0], splitstr[1], splitstr[2], splitstr[3], splitstr[4], splitstr[5], splitstr[6], splitstr[7], splitstr[8], splitstr[9]);
                jamurModels.add(jamurModel);

                count++;
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jamurModels;
    }

    public ArrayList<WarnaModel> preLoadColorEks(){
        ArrayList<WarnaModel> warnaModels = new ArrayList<>();
        String line = null;
        BufferedReader reader2;
        try{
            Resources res = getResources();
            InputStream color_eks;

            color_eks = res.openRawResource(R.raw.combined);

            reader2 = new BufferedReader(new InputStreamReader(color_eks));
            int count =0;
            do{
                line = reader2.readLine();
                if(line!=null) {
                    Log.d("colourr", line);
                    WarnaModel warnaModel;
                    warnaModel = new WarnaModel(line);

                    warnaModels.add(warnaModel);

                    count++;
                }

            }while (line!=null);
        } catch (Exception e){
            Log.d("colourrr", "apakah error");
            e.printStackTrace();
        }
        return warnaModels;
    }

    public ArrayList<BentukModel> preLoadBentukEks(){
        ArrayList<BentukModel> bentukModels = new ArrayList<>();
        String line = null;
        BufferedReader reader2;
        try{
            Resources res = getResources();
            InputStream bentuk_eks;

            bentuk_eks = res.openRawResource(R.raw.opencv);

            reader2 = new BufferedReader(new InputStreamReader(bentuk_eks));
            int count =0;
            do{
                line = reader2.readLine();
                if(line!=null) {
                    Log.d("colourr", line);
                    BentukModel bentukModel;
                    bentukModel = new BentukModel(line);

                    bentukModels.add(bentukModel);


                    count++;
                }

            }while (line!=null);
        } catch (Exception e){
            Log.d("colourrr", "apakah error");
            e.printStackTrace();
        }
        return bentukModels;
    }

    public ArrayList<Weight1Model> preLoadWeight1(){
        ArrayList<Weight1Model> weight1Models = new ArrayList<>();
        String line = null;
        BufferedReader reader2;
        try{
            Resources res = getResources();
            InputStream weight1;

            weight1 = res.openRawResource(R.raw.weight1);

            reader2 = new BufferedReader(new InputStreamReader(weight1));
            int count =0;
            do{
                line = reader2.readLine();
                if(line!=null) {
                    Log.d("colourr", line);
                    Weight1Model weight1Model;
                    weight1Model = new Weight1Model(line);

                    weight1Models.add(weight1Model);

                    count++;
                }

            }while (line!=null);
        } catch (Exception e){
            Log.d("colourrr", "apakah error");
            e.printStackTrace();
        }
        return weight1Models;
    }

    public ArrayList<Weight2Model> preLoadWeight2(){
        ArrayList<Weight2Model> weight2Models = new ArrayList<>();
        String line = null;
        BufferedReader reader2;
        try{
            Resources res = getResources();
            InputStream weight2;

            weight2 = res.openRawResource(R.raw.weight2);

            reader2 = new BufferedReader(new InputStreamReader(weight2));
            int count =0;
            do{
                line = reader2.readLine();
                if(line!=null) {
                    Log.d("colourr", line);
                    Weight2Model weight2Model;
                    weight2Model = new Weight2Model(line);

                    weight2Models.add(weight2Model);

                    count++;
                }

            }while (line!=null);
        } catch (Exception e){
            Log.d("colourrr", "apakah error");
            e.printStackTrace();
        }
        return weight2Models;
    }

    public ArrayList<Weight3Model> preLoadWeight3(){
        ArrayList<Weight3Model> weight3Models = new ArrayList<>();
        String line = null;
        BufferedReader reader2;
        try{
            Resources res = getResources();
            InputStream weight3;

            weight3 = res.openRawResource(R.raw.weight3);

            reader2 = new BufferedReader(new InputStreamReader(weight3));
            int count =0;
            do{
                line = reader2.readLine();
                if(line!=null) {
                    Log.d("colourr", line);
                    Weight3Model weight3Model;
                    weight3Model = new Weight3Model(line);

                    weight3Models.add(weight3Model);

                    count++;
                }

            }while (line!=null);
        } catch (Exception e){
            Log.d("colourrr", "apakah error");
            e.printStackTrace();
        }
        return weight3Models;
    }
}

