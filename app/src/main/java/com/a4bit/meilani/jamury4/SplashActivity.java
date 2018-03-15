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
import com.a4bit.meilani.jamury4.utility.JamurHelper;
import com.a4bit.meilani.jamury4.utility.JamurModel;
import com.a4bit.meilani.jamury4.utility.WarnaHelper;
import com.a4bit.meilani.jamury4.utility.WarnaModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        new LoadData(TABLE_JAMURY, TABLE_WARNA).execute();
    }

    private class LoadData extends AsyncTask<Void, Integer, Void>{
        final String TAG = LoadData.class.getSimpleName();
        JamurHelper jamurHelper;
        AppPreference appPreference;

        WarnaHelper warnaHelper;

        String tableJamur,tableWarna;
        double progress;
        double maxprogress = 100;

        public LoadData(String tableJamur, String tableWarna){
            this.tableJamur = tableJamur;
            this.tableWarna = tableWarna;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Boolean firstRun = appPreference.getFirstRun();
            if(firstRun){
                ArrayList<JamurModel> jamurModels = preLoadRaw();
                ArrayList<WarnaModel> warnaModels = preLoadColorEks();


                progress = 30;
                publishProgress((int)progress);
                Double progressMaxInsert = 80.0;
                Double progressDiff = (progressMaxInsert - progress) / (jamurModels.size() + warnaModels.size());

                jamurHelper.open();
                jamurHelper.beginTransaction();


                try {
                    for (JamurModel model : jamurModels) {
                        jamurHelper.insertTransaction(tableJamur, model);
                        progress += progressDiff;
                        publishProgress((int) progress);
                        Log.d("loggy", "insert success");
                    }

                    jamurHelper.setTransactionSuccess();
                }catch(Exception e){
                    Log.e(TAG, "dict err insertTransaction");
                }
                jamurHelper.endTransaction();

                jamurHelper.close();

                warnaHelper.open();
                warnaHelper.beginTransaction();

                try {
                    for (WarnaModel model : warnaModels){
                        warnaHelper.insertTransaction(tableWarna,model);
//                        progress+=progressDiff;
//                        publishProgress((int)progress);
                        Log.d("loggy","insert ekstraksi success");
                    }
                    warnaHelper.setTransactionSuccess();
                }catch (Exception e){
                    Log.e(TAG , "color err insert transaction");
                    Log.e(TAG , e.toString());
                    e.printStackTrace();

                }

                warnaHelper.endTransaction();

                warnaHelper.close();

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
            warnaHelper = new WarnaHelper(SplashActivity.this);
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
                WarnaModel warnaModel;
                warnaModel = new WarnaModel(line);

                warnaModels.add(warnaModel);

                count++;

            }while (line!=null);
        } catch (Exception e){
            e.printStackTrace();
        }
        return warnaModels;
    }
}

