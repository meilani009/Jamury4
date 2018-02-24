package com.a4bit.meilani.jamury4.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by root on 2/23/18.
 */

public class AppPreference {
    static String APP_FIRST_RUN = "app_first_run";

    SharedPreferences prefs;
    Context context;

    public AppPreference(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public void setFirstRun(Boolean input){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(APP_FIRST_RUN, input);
        editor.commit();
    }

    public Boolean getFirstRun(){
        return prefs.getBoolean(APP_FIRST_RUN, true);
    }
}
