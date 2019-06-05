package com.example.bookspace;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bookspace.model.SimilarBooks;

import java.util.List;

public class ZhenYaArrayAdapter extends BaseAdapter {
    private Context mContext1;
    private List<SimilarBooks> mBooksList1;

    public ZhenYaArrayAdapter(Context mContext,List<SimilarBooks> mBooksList){
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
        View row1 = View.inflate(mContext1, R.layout.similarbooks, null);
        TextView myTitle = (TextView) row1.findViewById(R.id.textViewNameBook);
        TextView myAuthor = (TextView) row1.findViewById(R.id.textViewAuthor);
        TextView myGenre = (TextView) row1.findViewById(R.id.textViewGenre);

        myTitle.setText(mBooksList1.get(position).getName());
        myAuthor.setText(mBooksList1.get(position).getAuthor());
        myGenre.setText(mBooksList1.get(position).getGenre());

        row1.setTag(mBooksList1.get(position).getId());
        return row1;
    }
}
