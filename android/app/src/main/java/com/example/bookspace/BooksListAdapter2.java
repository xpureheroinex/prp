package com.example.bookspace;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bookspace.Books;
import com.example.bookspace.R;

import java.util.List;

public class BooksListAdapter2 extends BaseAdapter{
    private Context mContext1;
    private List<Books2> mBooksList1;

    public BooksListAdapter2(Context mContext,List<Books2> mBooksList){
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
        View row = View.inflate(mContext1, R.layout.row1, null);
        TextView myTitle = (TextView) row.findViewById(R.id.textView16);
        TextView myRate = (TextView) row.findViewById(R.id.textView17);
        TextView myAuthor = (TextView) row.findViewById(R.id.textView18);
        TextView myGenre = (TextView) row.findViewById(R.id.textView19);

        myTitle.setText(mBooksList1.get(position).getName());
        myRate.setText(String.valueOf(mBooksList1.get(position).getRate()));
        myAuthor.setText(mBooksList1.get(position).getAuthor());
        myGenre.setText(mBooksList1.get(position).getGenre());

        row.setTag(mBooksList1.get(position).getId());
        return row;
    }
}