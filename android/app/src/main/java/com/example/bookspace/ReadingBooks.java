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

import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.GetBooksResponse;
import com.example.bookspace.model.books.UserBook;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ReadingBooks extends Fragment {
    ListView lvBooks2;
    BooksListAdapter adapter2;
    List<UserBook> mBooksList2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.readingbooks,container,false);
        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");
        lvBooks2 = view.findViewById(R.id.list2);

        Call<GetBooksResponse> getInProgressBooks = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getInProgressBooks("Bearer " + token);

        getInProgressBooks.enqueue(new Callback<GetBooksResponse>() {
            @Override
            public void onResponse(Call<GetBooksResponse> call, Response<GetBooksResponse> response) {
                UserBook[] userBooks = response.body().getInfo();
                mBooksList2 = new ArrayList<>();

                for(UserBook book : userBooks){
                    mBooksList2.add(book);
                }

                adapter2 = new BooksListAdapter(getContext(),mBooksList2);
                lvBooks2.setAdapter(adapter2);
            }

            @Override
            public void onFailure(Call<GetBooksResponse> call, Throwable t) {
            }
        });

        lvBooks2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(),"Hy" + view.getTag(),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
