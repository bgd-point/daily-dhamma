package red.point.dailydhamma.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import red.point.dailydhamma.R;

public class AboutFragment extends Fragment {

    public AboutFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        HtmlTextView aboutText = view.findViewById(R.id.aboutText);
        aboutText.setHtml("<h1>About</h1>"
            + "<p>Daily Dhamma adalah aplikasi yang menyediakan konten tanya jawab yang tersedia secara publik oleh berbagai pemuka agama khususnya agama Buddha Theravada.</p>"
            + "<p>Berbagai macam cakupan tanya jawab dan diskusi tersedia; baik untuk masalah kehidupan sehari hari maupun pertanyaan mengenai Dhamma.</p>"
            + "<p>Pembuatan aplikasi ini didasari harapan semoga pengguna dapat menemukan solusi ataupun pencerahan dalam problem sehari hari.</p>"
            + "<br>"
            + "<p><b>Namo Buddhâya</b><p>"
            + "<p><b>Daily Dhamma &copy; 2017</b></p>");

        return view;
    }
}
