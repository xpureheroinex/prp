package com.example.bookspace;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bookspace.model.SimilarBooks;

import java.util.List;

public class SimilarBooksAdapter extends BaseAdapter {

    private Context context;
    private List<SimilarBooks> similarBooksList;

    public SimilarBooksAdapter(Context mContext, List<SimilarBooks> mBooksList){
        this.context = mContext;
        this.similarBooksList = mBooksList;
    }
    @Override
    public int getCount() {
        return similarBooksList.size();
    }

    @Override
    public Object getItem(int position) {
        return similarBooksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row2 = View.inflate(context, R.layout.row2, null);

        TextView myTitle = row2.findViewById(R.id.topBookTitle);
        TextView myAuthor = row2.findViewById(R.id.topBookAuthor);
        TextView myGenre = row2.findViewById(R.id.topBookGenre);
        ImageButton addbtn = row2.findViewById(R.id.addbtn1);
        addbtn.setVisibility(View.GONE);


        myTitle.setText(similarBooksList.get(position).getName());
        myAuthor.setText(similarBooksList.get(position).getAuthor());
        myGenre.setText(similarBooksList.get(position).getGenre());

        row2.setTag(similarBooksList.get(position).getId());
        return row2;
    }
}
