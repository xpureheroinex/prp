package com.example.bookspace;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.MainPageBook;
import com.example.bookspace.model.books.TopResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class TopRecommends2 extends Fragment {


    ListView booksRecs;
    BooksListAdapter2 adapter;
    List<MainPageBook> mBooksList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.top_recommendations,container,false);

        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");


        booksRecs = view.findViewById(R.id.list4);

        Call<TopResponse> getRecs = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getRecs("Bearer " + token);

        getRecs.enqueue(new Callback<TopResponse>() {
            @Override
            public void onResponse(Call<TopResponse> call, Response<TopResponse> response) {
//                MainPageBook[] books = response.body().getBooks();
//
//                mBooksList = new ArrayList<>();
//
//                for(MainPageBook book : books){
//                    mBooksList.add(book);
//                }
//
//                adapter = new BooksListAdapter2(getContext(), mBooksList);
//                booksRecs.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<TopResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return view;
    }
}