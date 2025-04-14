package red.point.dailydhamma.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import red.point.dailydhamma.R;
import red.point.dailydhamma.utils.MPreferenceManager;
import red.point.dailydhamma.adapter.QuestionAnswerAdapter;
import red.point.dailydhamma.adapter.holder.QuestionAnswerHolder;
import red.point.dailydhamma.model.QuestionAnswer;
import red.point.dailydhamma.ui.DividerItemDecoration;

public class ListFragment extends Fragment {

    private FirebaseRecyclerAdapter<QuestionAnswer, QuestionAnswerHolder> mAdapter;

    protected SearchView searchView;
    protected QuestionAnswerAdapter adapter;
    private long listNumber = 1;

    public ListFragment() {}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_box, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(adapter != null) {
                    adapter.getFilter().filter(query);
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        setHasOptionsMenu(true);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference listRef = (MPreferenceManager.readBoolInformation(getContext(), MPreferenceManager.DEFAULT_LANG)
                ? database.getReference("question-answer")
                : database.getReference("question-answer-en"));

        if (listRef == null) {
            listRef = database.getReference("question-answer");
        }

        listRef.keepSynced(true);

        final RecyclerView recycleView = view.findViewById(R.id.listQuestionAnswer);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), Color.LTGRAY, 1.0f);
        recycleView.addItemDecoration(decoration);
        listNumber = 1;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<QuestionAnswer> arrayListQA = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    QuestionAnswer questionAnswer = child.getValue(QuestionAnswer.class);

                    questionAnswer.setKey(child.getRef().getKey());
                    questionAnswer.setTitle(listNumber + ". " + questionAnswer.getTitle());

                    listNumber += 1;

                    arrayListQA.add(questionAnswer);
                }

                if(arrayListQA != null) {
                    adapter = new QuestionAnswerAdapter(getContext(),arrayListQA);
                    recycleView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };

        listRef.addValueEventListener(postListener);

        return view;
    }
}

