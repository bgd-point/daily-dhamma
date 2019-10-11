package red.point.dailydhamma.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.ShareActionProvider;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import java.util.Random;
import red.point.dailydhamma.R;
import red.point.dailydhamma.Utils.MPreferenceManager;

public class RandomFragment extends Fragment {

    ShareActionProvider mShareActionProvider;
    String shareText;

    public RandomFragment() {}

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

        // Firebase instance variables
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference questionAnswerRef = (MPreferenceManager.readBoolInformation(getContext(), MPreferenceManager.DEFAULT_LANG)?
                database.getReference("question-answer"): database.getReference("question-answer-en"));

        if (questionAnswerRef == null) {
            questionAnswerRef = database.getReference("question-answer");
        }

        questionAnswerRef.keepSynced(true);

        questionAnswerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Random random = new Random();
                int index = random.nextInt((int) dataSnapshot.getChildrenCount());
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (count == index) {
                        if(getView() != null) {
                            HtmlTextView questionAnswer = getView().findViewById(R.id.questionAnswer);
                            questionAnswer.setHtml("<h1>Question</h1> \n"
                                    + snapshot.child("question").getValue().toString()
                                    + "\n"
                                    + "<h1>Answer</h1>"
                                    + "\n"
                                    + snapshot.child("answer").getValue().toString());

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
                    count++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}
