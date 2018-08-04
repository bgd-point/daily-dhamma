package red.point.dailydhamma.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class MPreferenceManager {

    public static final String DEFAULT_LANG = "default_lang";


    public static void saveStringInformation(Context context, String key, String value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(key, value);
        ed.apply();
    }

    public static String readStringInformation(Context context, String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, "");
    }

    public static void saveBoolInformation(Context context, String key, boolean value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(key, value);
        ed.apply();
    }

    public static boolean readBoolInformation(Context context, String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key, false);
    }

    public static void saveIntInformation(Context context, String key, int value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(key, value);
        ed.apply();
    }

    public static int readIntInformation(Context context, String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key, 0);
    }

}
