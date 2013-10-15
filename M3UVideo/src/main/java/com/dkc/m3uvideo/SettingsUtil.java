package com.dkc.m3uvideo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by barbarian on 15.10.13.
 */
public class SettingsUtil {
    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getStringOption(Context context,String key,String defValue){
        SharedPreferences settings = getPreferences(context);
        return settings.getString(key,defValue);
    }

    public static void setOption(Context context,String key, String value){
        if(context!=null){SharedPreferences settings = getPreferences(context.getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();

            editor.putString(key,value);

            editor.commit();
        }
    }
}
