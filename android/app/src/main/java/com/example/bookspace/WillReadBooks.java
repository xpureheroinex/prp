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

public class WillReadBooks extends Fragment {
    ListView lvBooks3;
    BooksListAdapter adapter3;
    List<Books> mBooksList3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.willreadbooks,container,false);
        lvBooks3 = (ListView) v.findViewById(R.id.list3);
        mBooksList3 = new ArrayList<>();
        mBooksList3.add(new Books(1,"FirstB",2.5,"01.12.2013","Author1"));
        mBooksList3.add(new Books(2,"SecondB",3.5,"11.12.2013","Author3"));
        adapter3 = new BooksListAdapter(getContext(),mBooksList3);
        lvBooks3.setAdapter(adapter3);

        lvBooks3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(),"Hy" + view.getTag(),Toast.LENGTH_SHORT).show();
            }
        });
        return v;

    }
}
