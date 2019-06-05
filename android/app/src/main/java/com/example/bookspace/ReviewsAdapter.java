package com.example.bookspace;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ReviewsAdapter extends BaseAdapter {
    private Context mContext1;
    private List<ReviewsClass> mBooksList1;

    public ReviewsAdapter(Context mContext,List<ReviewsClass> mBooksList){
        this.mContext1 = mContext;
        this.mBooksList1 = mBooksList;
    }
    @Override
    public int getCount() {
        return mBooksList1.size();
    }

    @Override
    public Object getItem(int position) {
        return mBooksList1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row1 = View.inflate(mContext1, R.layout.reviewview, null);
        TextView myName = (TextView) row1.findViewById(R.id.textViewUsername);
        TextView myDate = (TextView) row1.findViewById(R.id.textViewDate);
        TextView myReview = (TextView) row1.findViewById(R.id.textViewReview);

        myName.setText(mBooksList1.get(position).getName());
        myDate.setText(mBooksList1.get(position).getCreated());
        myReview.setText(mBooksList1.get(position).getText());

        row1.setTag(mBooksList1.get(position).getId());
        return row1;
    }
}
