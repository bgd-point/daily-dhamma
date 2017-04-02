package red.point.dailydhamma;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            SharedPreferences settings = getSharedPreferences("red.point.DailyDhamma", Context.MODE_PRIVATE);
            String font_size_list = settings.getString("font_size_list", "Small");
            if (font_size_list.equals("Large")) {
                setTheme(R.style.FontSizeLarge);
            } else if (font_size_list.equals("Medium")) {
                setTheme(R.style.FontSizeMedium);
            } else {
                setTheme(R.style.FontSizeSmall);
            }
        } catch (Exception e) {

        }

        super.onCreate(savedInstanceState);
    }
}
