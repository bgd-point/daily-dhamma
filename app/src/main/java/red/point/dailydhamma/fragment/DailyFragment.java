package red.point.dailydhamma.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import red.point.dailydhamma.R;
import red.point.dailydhamma.model.QuestionAnswer;

public class DailyFragment extends Fragment {

    ShareActionProvider mShareActionProvider;
    String shareText;
    ProgressDialog loading = null;
    int count = 0;

    public DailyFragment() {}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        super.onCreateOptionsMenu(menu,inflater);
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        setHasOptionsMenu(true);

        loading = new ProgressDialog(getContext());
        loading.setCancelable(false);
        loading.setMessage("Loading");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Firebase instance variables
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference questionAnswerRef = database.getReference("dhamma-today");
        questionAnswerRef.keepSynced(true);

        // Save device token
        String token = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference devicesRef = database.getReference("devices/token/" + token);
        devicesRef.setValue(true);

        questionAnswerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(getView() != null) {
                    QuestionAnswer data = dataSnapshot.getValue(QuestionAnswer.class);
                    HtmlTextView questionAnswer = (HtmlTextView) getView().findViewById(R.id.questionAnswer);
                    String date = new SimpleDateFormat("dd MMM yyyy").format(new Date());
                    questionAnswer.setHtml("<center'>" + date + "</center>"
                        + "\n <h1>Question</h1> \n"
                        + data.getQuestion()
                        + "\n"
                        + "<h1>Answer</h1>"
                        + "\n"
                        + data.getAnswer());

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareText = questionAnswer.getText().toString();
                    shareText = shareText + "\n\n check this app to see more dhamma \n\n";
                    shareText = shareText + "https://play.google.com/store/apps/details?id=red.point.dailydhamma \n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    shareIntent.setType("text/plain");
                    if(shareIntent != null && mShareActionProvider != null) {
                        mShareActionProvider.setShareIntent(shareIntent);
                    }

                    questionAnswer.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        questionAnswerRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousKey) {
                count++;

                if (count >= dataSnapshot.getChildrenCount()){
                    loading.dismiss();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        loading.show();
        return view;
    }
}
