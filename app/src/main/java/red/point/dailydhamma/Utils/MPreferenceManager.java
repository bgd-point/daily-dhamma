package red.point.dailydhamma.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class MPreferenceManager {

    public static final String DEFAULT_LANG = "default_lang";
    public static final String IS_REOPEN = "is_reopen"; // false: first run,  true: rerun


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

    public static String readStringInformation(Context context, String key, boolean useDefaultSharePreferences) {

        SharedPreferences sp;

        if (useDefaultSharePreferences) {
            sp = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            sp = context.getSharedPreferences("red.point.DailyDhamma", Context.MODE_PRIVATE);
        }

        return sp.getString(key, "");
    }

}
