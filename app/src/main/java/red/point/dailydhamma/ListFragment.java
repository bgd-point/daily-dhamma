package red.point.dailydhamma;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListFragment extends Fragment {

    private FirebaseRecyclerAdapter<QuestionAnswer, QuestionAnswerHolder> mAdapter;

    public ListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        DatabaseReference listRef = FirebaseDatabase.getInstance().getReference("question-answer");
        listRef.keepSynced(true);

        RecyclerView listQuestionAnswerView = (RecyclerView) view.findViewById(R.id.listQuestionAnswer);
        listQuestionAnswerView.setHasFixedSize(true);
        listQuestionAnswerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new FirebaseRecyclerAdapter<QuestionAnswer, QuestionAnswerHolder>(QuestionAnswer.class, android.R.layout.two_line_list_item, QuestionAnswerHolder.class, listRef) {
            @Override
            public void populateViewHolder(final QuestionAnswerHolder questionAnswerHolder, final QuestionAnswer questionAnswer, final int position) {
                questionAnswerHolder.setTitle(questionAnswer.getTitle());

                questionAnswerHolder.mTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String key = getRef(position).getKey();
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        Fragment singleFragment = new SingleFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("key", key);
                        singleFragment.setArguments(bundle);
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_container, singleFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        };

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), Color.GRAY, 1.0f);
        listQuestionAnswerView.addItemDecoration(decoration);
        listQuestionAnswerView.setAdapter(mAdapter);

        return view;
    }
}
