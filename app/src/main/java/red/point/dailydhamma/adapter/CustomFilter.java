package red.point.dailydhamma.adapter;

import android.widget.Filter;
import java.util.ArrayList;

import red.point.dailydhamma.model.QuestionAnswer;

public class CustomFilter extends Filter{

    QuestionAnswerAdapter adapter;
    ArrayList<QuestionAnswer> filterList;


    CustomFilter(ArrayList<QuestionAnswer> filterList, QuestionAnswerAdapter adapter)
    {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    // FILTERING
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        // CHECK CONSTRAINT VALIDITY
        if (constraint != null && constraint.length() > 0)
        {
            // CHANGE TO UPPER
            constraint = constraint.toString().toUpperCase();
            // STORE OUR FILTERED PLAYERS
            ArrayList<QuestionAnswer> filteredQuestionAnswer = new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                // CHECK
                if(filterList.get(i).getTitle().toUpperCase().contains(constraint))
                {
                    // ADD QUESTION ANSWER TO FILTERED PLAYERS
                    filteredQuestionAnswer.add(filterList.get(i));
                }
            }

            results.count=filteredQuestionAnswer.size();
            results.values=filteredQuestionAnswer;
        } else {
            results.count=filterList.size();
            results.values=filterList;
        }


        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.questionAnswerList = (ArrayList<QuestionAnswer>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
