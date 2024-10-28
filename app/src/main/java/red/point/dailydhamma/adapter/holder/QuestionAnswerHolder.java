package red.point.dailydhamma.adapter.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import red.point.dailydhamma.R;
import red.point.dailydhamma.adapter.ItemClickListener;

public  class QuestionAnswerHolder extends RecyclerView.ViewHolder {

    public TextView title;
    ItemClickListener itemClickListener;

    public QuestionAnswerHolder(View itemView) {
        super(itemView);

        this.title = itemView.findViewById(R.id.title);

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


