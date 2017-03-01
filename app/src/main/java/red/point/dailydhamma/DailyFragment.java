package red.point.dailydhamma;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.Random;

public class DailyFragment extends Fragment {

    public DailyFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        // Firebase instance variables
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference questionAnswerRef = database.getReference("question-answer");
        questionAnswerRef.keepSynced(true);

        // Save device token
        String token = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference devicesRef = database.getReference("devices/token/" + token);
        devicesRef.setValue(true);

        questionAnswerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Random random = new Random();
                int index = random.nextInt((int) dataSnapshot.getChildrenCount());
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (count == index) {
                        HtmlTextView questionAnswer = (HtmlTextView) getView().findViewById(R.id.questionAnswer);
                        questionAnswer.setHtml("<h1>Question</h1> \n"
                                + snapshot.child("question").getValue().toString()
                                + "\n"
                                + "<h1>Answer</h1>"
                                + "\n"
                                + snapshot.child("answer").getValue().toString());

                        questionAnswer.setMovementMethod(LinkMovementMethod.getInstance());
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
