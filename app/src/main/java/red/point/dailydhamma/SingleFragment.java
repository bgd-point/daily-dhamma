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
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class SingleFragment extends Fragment {

    public SingleFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single, container, false);
        String key = this.getArguments().getString("key");

        // Firebase instance variables
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference singleQuestionAnswerRef = database.getReference("question-answer/" + key);
        singleQuestionAnswerRef.keepSynced(true);

        singleQuestionAnswerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                QuestionAnswer data = dataSnapshot.getValue(QuestionAnswer.class);
                HtmlTextView questionAnswer = (HtmlTextView) getView().findViewById(R.id.questionAnswer);
                questionAnswer.setHtml("<h1>Question</h1> \n"
                        + data.getQuestion()
                        + "\n"
                        + "<h1>Answer</h1>"
                        + "\n"
                        + data.getAnswer()
                );

                questionAnswer.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}
