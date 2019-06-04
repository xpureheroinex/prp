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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TopFragment extends Fragment {

    ListView lvBooks4;
    BooksListAdapter2 adapter4;
    List<Books2> mBooksList4;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.top_fragment,container,false);
        lvBooks4 = (ListView) v.findViewById(R.id.list4);
        mBooksList4 = new ArrayList<>();
        mBooksList4.add(new Books2(1,"FirstB1",2.5,"AUTHOR1","GENRE1"));
        mBooksList4.add(new Books2(2,"SecondB2",3.5,"AUTHOR2","GENRE2"));
        adapter4 = new BooksListAdapter2(getContext(),mBooksList4);
        lvBooks4.setAdapter(adapter4);

        lvBooks4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(),"Hy" + view.getTag(),Toast.LENGTH_SHORT).show();
            }
        });
       return v;
    }

}