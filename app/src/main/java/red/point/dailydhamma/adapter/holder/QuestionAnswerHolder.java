package red.point.dailydhamma.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import red.point.dailydhamma.adapter.ItemClickListener;
import red.point.dailydhamma.R;

public  class QuestionAnswerHolder extends RecyclerView.ViewHolder {

    public TextView title;
    ItemClickListener itemClickListener;

    public QuestionAnswerHolder(View itemView) {
        super(itemView);

        this.title = (TextView) itemView.findViewById(R.id.title);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view,getLayoutPosition());
            }
        });
    }

    public void setItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener=ic;
    }

}


