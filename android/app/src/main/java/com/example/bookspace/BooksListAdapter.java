package com.example.bookspace;

import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BooksListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Books> mBooksList;

    public BooksListAdapter(Context mContext,List<Books> mBooksList){
        this.mContext = mContext;
        this.mBooksList = mBooksList;
    }
    @Override
    public int getCount() {
        return mBooksList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBooksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = View.inflate(mContext,R.layout.row, null);
        TextView myTitle = (TextView) row.findViewById(R.id.textView12);
        TextView myRate = (TextView) row.findViewById(R.id.textView13);
        TextView myDate = (TextView) row.findViewById(R.id.textView14);
        TextView myAuthor = (TextView) row.findViewById(R.id.textView15);

        myTitle.setText(mBooksList.get(position).getName());
        myRate.setText(String.valueOf(mBooksList.get(position).getRate()));
        myDate.setText(mBooksList.get(position).getDate());
        myAuthor.setText(mBooksList.get(position).getAuthor());

        row.setTag(mBooksList.get(position).getId());
        return row;
    }
}
