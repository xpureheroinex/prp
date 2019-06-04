package com.example.bookspace;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReadBooks extends Fragment {
    ListView lvBooks1;
    BooksListAdapter adapter1;
    List<Books> mBooksList1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.readbooks,container,false);
        lvBooks1 = (ListView) v.findViewById(R.id.list1);
        mBooksList1 = new ArrayList<>();
        mBooksList1.add(new Books(1,"FirstB",2.5,"01.12.2013","Author1"));
        mBooksList1.add(new Books(2,"SecondB",3.5,"11.12.2013","Author2eeee"));
        adapter1 = new BooksListAdapter(getContext(),mBooksList1);
        lvBooks1.setAdapter(adapter1);

        lvBooks1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(),"Hy" + view.getTag(),Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}
