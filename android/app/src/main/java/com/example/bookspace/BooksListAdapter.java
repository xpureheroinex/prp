package com.example.bookspace;

import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bookspace.model.books.UserBook;

import java.util.List;

public class BooksListAdapter extends BaseAdapter {

    private Context mContext;
    private List<UserBook> mBooksList;

    public BooksListAdapter(Context mContext,List<UserBook> mBooksList){
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

        TextView myTitle = row.findViewById(R.id.statusBookTitle);
        TextView myRate = row.findViewById(R.id.statusBookRate);
        TextView myGenre = row.findViewById(R.id.statusBookGenre);
        TextView myAuthor = row.findViewById(R.id.statusBookAuthor);

        myTitle.setText(mBooksList.get(position).getTitle());
//        myRate.setText(String.valueOf(mBooksList.get(position).getRate()));
        myGenre.setText(mBooksList.get(position).getGenre());
        myAuthor.setText(mBooksList.get(position).getAuthor());

        row.setTag(mBooksList.get(position).getId());
        return row;
    }
}
