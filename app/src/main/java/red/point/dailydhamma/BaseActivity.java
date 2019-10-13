package red.point.dailydhamma;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import red.point.dailydhamma.Utils.MPreferenceManager;


public class BaseActivity extends AppCompatActivity {

    String Language;
    boolean Lang_Indonesian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            SharedPreferences settings = getSharedPreferences("red.point.DailyDhamma", Context.MODE_PRIVATE);

            if (!MPreferenceManager.readBoolInformation(this, MPreferenceManager.IS_REOPEN)) {
                Language = Locale.getDefault().getDisplayLanguage().toString();
                Lang_Indonesian = (Language.equals("Indonesia"));

                SharedPreferences.Editor ed = settings.edit();
                ed.putString("language_list", Language.equals("Indonesia")? "Indonesian": "English");
                ed.apply();

                MPreferenceManager.saveBoolInformation(this, MPreferenceManager.IS_REOPEN, true);
            }

            String font_size_list = settings.getString("font_size_list", "Small");
            if (font_size_list.equals("Large")) {
                setTheme(R.style.FontSizeLarge);
            } else if (font_size_list.equals("Medium")) {
                setTheme(R.style.FontSizeMedium);
            } else {
                setTheme(R.style.FontSizeSmall);
            }

            String language_list = settings.getString("language_list", "Indonesian");
            MPreferenceManager.saveBoolInformation(BaseActivity.this, MPreferenceManager.DEFAULT_LANG,
                    language_list.equals("Indonesian"));

        } catch (Exception e) {

        }

        super.onCreate(savedInstanceState);
    }
}
