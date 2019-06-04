package com.example.bookspace;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bookspace.Books;
import com.example.bookspace.R;
import com.example.bookspace.model.books.MainPageBook;

import java.util.List;

public class BooksListAdapter2 extends BaseAdapter{
    private Context mContext1;
    private List<MainPageBook> mBooksList1;

    public BooksListAdapter2(Context mContext,List<MainPageBook> mBooksList){
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
        View row1 = View.inflate(mContext1, R.layout.row1, null);
        TextView myTitle = row1.findViewById(R.id.topBookTitle);
        TextView myRate = row1.findViewById(R.id.topBookRate);
        TextView myAuthor = row1.findViewById(R.id.topBookAuthor);
        TextView myGenre = row1.findViewById(R.id.topBookGenre);

        myTitle.setText(mBooksList1.get(position).getTitle());
        myRate.setText(String.valueOf(mBooksList1.get(position).getRate()));
        myAuthor.setText(mBooksList1.get(position).getAuthor());
        myGenre.setText(mBooksList1.get(position).getGenre());

        row1.setTag(mBooksList1.get(position).getId());
        return row1;
    }
}