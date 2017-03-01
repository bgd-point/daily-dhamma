package red.point.dailydhamma;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public  class QuestionAnswerHolder extends RecyclerView.ViewHolder {
    public final TextView mTitleView;

    public QuestionAnswerHolder(View itemView) {
        super(itemView);
        mTitleView = (TextView) itemView.findViewById(android.R.id.text1);
    }

    public void setTitle(String title) {
        mTitleView.setText(title);
    }

}


