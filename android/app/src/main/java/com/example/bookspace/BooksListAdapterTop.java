package com.example.bookspace;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bookspace.model.books.MainPageBook;
import java.util.List;

public class BooksListAdapterTop extends BaseAdapter{
    private Context mContextTop;
    private List<MainPageBook> mBooksListTop;

    public BooksListAdapterTop(Context mContext, List<MainPageBook> mBooksList){
        this.mContextTop = mContext;
        this.mBooksListTop = mBooksList;
    }
    @Override
    public int getCount() {
        return mBooksListTop.size();
    }

    @Override
    public Object getItem(int position) {
        return mBooksListTop.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row1 = View.inflate(mContextTop, R.layout.row1, null);
        
        TextView myTitle = row1.findViewById(R.id.topBookTitle);
        TextView myRate = row1.findViewById(R.id.topBookRate);
        TextView myAuthor = row1.findViewById(R.id.topBookAuthor);
        TextView myGenre = row1.findViewById(R.id.topBookGenre);

        myTitle.setText(mBooksListTop.get(position).getTitle());
        myRate.setText(String.valueOf(mBooksListTop.get(position).getRate()));
        myAuthor.setText(mBooksListTop.get(position).getAuthor());
        myGenre.setText(mBooksListTop.get(position).getGenre());

        row1.setTag(mBooksListTop.get(position).getId());
        return row1;
    }

    @Override
    public boolean isEnabled(int position){
        return true;
    }
}