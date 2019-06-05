package com.example.bookspace;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends BaseAdapter {
    private Context mContext1;
    private List<NoteClass> mBooksList1;

    public NoteAdapter(Context mContext,List<NoteClass> mBooksList){
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
        View row1 = View.inflate(mContext1, R.layout.noteview, null);
        TextView myName = (TextView) row1.findViewById(R.id.textViewTitle);
        TextView myDate = (TextView) row1.findViewById(R.id.textViewDateNote);
        TextView myNote = (TextView) row1.findViewById(R.id.textViewNote);

        myName.setText(mBooksList1.get(position).getTitle());
        myDate.setText(mBooksList1.get(position).getCreated());
        myNote.setText(mBooksList1.get(position).getText());

        row1.setTag(mBooksList1.get(position).getId());
        return row1;
    }
}