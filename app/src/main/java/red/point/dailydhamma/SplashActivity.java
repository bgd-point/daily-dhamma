package red.point.dailydhamma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

import red.point.dailydhamma.Utils.MPreferenceManager;

public class SplashActivity extends AppCompatActivity {

    String Language;
    boolean Lang_Indonesian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Language = Locale.getDefault().getDisplayLanguage().toString();
        Lang_Indonesian = (Language.equals("Indonesia")? true: false);

        SharedPreferences settings = getSharedPreferences("red.point.DailyDhamma", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = settings.edit();
        ed.putString("language_list", Language.equals("Indonesia")? "Indonesian": "English");
        ed.apply();

        MPreferenceManager.saveBoolInformation(this, MPreferenceManager.DEFAULT_LANG, Lang_Indonesian);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
