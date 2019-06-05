package com.example.bookspace;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bookspace.Books;
import com.example.bookspace.R;
import com.example.bookspace.model.books.MainPageBook;
import com.example.bookspace.model.books.UserBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BooksListAdapter2 extends BaseAdapter{
    Context mContext1;
    List<MainPageBook> mBooksList1;
    ArrayList<MainPageBook> arrayList;
    ImageButton deletebtn;

    public BooksListAdapter2(Context mContext,List<MainPageBook> mBooksList){
        this.mContext1 = mContext;
        this.mBooksList1 = mBooksList;
        this.arrayList = new ArrayList<MainPageBook>();
        this.arrayList.addAll(mBooksList);
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



        row1.setTag(mBooksList1.get(position).getId());

        //deletebtn = (ImageButton) row1.findViewById(R.id.deletebtn);
        //deletebtn.setTag(mBooksList1.get(position).getId());
       // deletebtn.setOnClickListener(new View.OnClickListener() {
          //  @Override
           // public void onClick(View v) {
             //  int index = (int) v.getTag();
             //   mBooksList1.remove(index);
              //  notifyDataSetChanged();

           // }
       // });
        myTitle.setText(mBooksList1.get(position).getTitle());
        myRate.setText(String.valueOf(mBooksList1.get(position).getRate()));
        myAuthor.setText(mBooksList1.get(position).getAuthor());
        myGenre.setText(mBooksList1.get(position).getGenre());

        return row1;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        mBooksList1.clear();
        if(charText.length()==0){
            mBooksList1.addAll(arrayList);
        }
        else{
            for(MainPageBook book : arrayList){
                if(book.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    mBooksList1.add(book);
                }
                else if(book.getAuthor().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    mBooksList1.add(book);
                }
                else if(book.getGenre().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    mBooksList1.add(book);
                }
            }
        }

        notifyDataSetChanged();
    }
}