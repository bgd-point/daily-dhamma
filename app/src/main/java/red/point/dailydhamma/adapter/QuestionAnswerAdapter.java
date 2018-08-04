package red.point.dailydhamma.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import java.util.ArrayList;

import red.point.dailydhamma.R;
import red.point.dailydhamma.fragment.SingleFragment;
import red.point.dailydhamma.adapter.holder.QuestionAnswerHolder;
import red.point.dailydhamma.model.QuestionAnswer;

public class QuestionAnswerAdapter extends RecyclerView.Adapter<QuestionAnswerHolder> implements Filterable {

    Context c;
    ArrayList<QuestionAnswer> questionAnswerList, filterList;
    CustomFilter filter;

    public QuestionAnswerAdapter(Context contex,ArrayList<QuestionAnswer> questionAnswerList)
    {
        this.c = contex;
        this.questionAnswerList = questionAnswerList;
        this.filterList = questionAnswerList;
    }

    @Override
    public QuestionAnswerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, null);

        return new QuestionAnswerHolder(v);
    }

    // DATA BOUND TO VIEWS
    @Override
    public void onBindViewHolder(QuestionAnswerHolder holder, int position) {

        // BIND DATA
        holder.title.setText(questionAnswerList.get(position).getTitle());

        // IMPLEMENT CLICK LISTENER
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Fragment singleFragment = new SingleFragment();
            Bundle bundle = new Bundle();
            bundle.putString("key", questionAnswerList.get(pos).getKey());
            singleFragment.setArguments(bundle);
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, singleFragment)
                    .addToBackStack(null)
                    .commit();
            }
        });
    }

    //GET TOTAL NUM OF PLAYERS
    @Override
    public int getItemCount() {
        return questionAnswerList.size();
    }

    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if(filter == null)
        {
            filter = new CustomFilter(filterList, this);
        }
        return filter;
    }
}

