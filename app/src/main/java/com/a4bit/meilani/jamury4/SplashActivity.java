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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_BENTUK;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_JAMURY;
import static com.a4bit.meilani.jamury4.utility.DatabaseContract.TABLE_WARNA;

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

        new LoadData(TABLE_JAMURY, TABLE_WARNA, TABLE_BENTUK).execute();
    }

    private class LoadData extends AsyncTask<Void, Integer, Void>{
        final String TAG = LoadData.class.getSimpleName();
        JamurHelper jamurHelper;
        AppPreference appPreference;

        String tableJamur,tableWarna, tableBentuk;
        double progress;
        double maxprogress = 100;

        public LoadData(String tableJamur, String tableWarna, String tableBentuk){
            this.tableJamur = tableJamur;
            this.tableWarna = tableWarna;
            this.tableBentuk = tableBentuk;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Boolean firstRun = appPreference.getFirstRun();
            if(firstRun){
                ArrayList<JamurModel> jamurModels = preLoadRaw();
                ArrayList<WarnaModel> warnaModels = preLoadColorEks();
                ArrayList<BentukModel> bentukModels = preLoadBentukEks();


                progress = 30;
                publishProgress((int)progress);
                Double progressMaxInsert = 80.0;
                Double progressDiff = (progressMaxInsert - progress) / (jamurModels.size() + warnaModels.size()+ bentukModels.size());

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

            raw_dict = res.openRawResource(R.raw.database);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            int count = 0;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\\|");

                JamurModel jamurModel;

                jamurModel = new JamurModel(splitstr[0], splitstr[1], splitstr[2], splitstr[3], splitstr[4], splitstr[5], splitstr[6], splitstr[7], splitstr[8]);
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
}

