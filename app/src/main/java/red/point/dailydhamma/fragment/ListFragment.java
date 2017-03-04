package red.point.dailydhamma.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import red.point.dailydhamma.adapter.QuestionAnswerAdapter;
import red.point.dailydhamma.ui.DividerItemDecoration;
import red.point.dailydhamma.model.QuestionAnswer;
import red.point.dailydhamma.adapter.holder.QuestionAnswerHolder;
import red.point.dailydhamma.R;

public class ListFragment extends Fragment {

    private FirebaseRecyclerAdapter<QuestionAnswer, QuestionAnswerHolder> mAdapter;

    protected SearchView searchView;
    protected QuestionAnswerAdapter adapter;

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

        DatabaseReference listRef = FirebaseDatabase.getInstance().getReference("question-answer");
        listRef.keepSynced(true);

        final RecyclerView recycleView = (RecyclerView) view.findViewById(R.id.listQuestionAnswer);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), Color.GRAY, 1.0f);
        recycleView.addItemDecoration(decoration);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<QuestionAnswer> arrayListQA = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    QuestionAnswer questionAnswer = child.getValue(QuestionAnswer.class);

                    questionAnswer.setKey(child.getRef().getKey().toString());
                    questionAnswer.setTitle(questionAnswer.getTitle());

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

