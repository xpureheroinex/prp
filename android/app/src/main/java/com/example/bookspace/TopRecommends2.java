package com.example.bookspace;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    ListView bookListElement;
    BooksListAdapterTop adapter;
    List<MainPageBook> bookList;
    int[] booksId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.top_recommendations,container,false);
        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");
        bookListElement = view.findViewById(R.id.listRec);


        Call<TopResponse> getRecs = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getRecs("Bearer " + token);

        getRecs.enqueue(new Callback<TopResponse>() {
            @Override
            public void onResponse(Call<TopResponse> call, Response<TopResponse> response) {
                MainPageBook[] books = response.body().getBooks();
                int count = books.length;
                booksId = new int[count];

                bookList = new ArrayList<>();

                for(int i = 0; i < count; i++){
                    bookList.add(books[i]);
                    booksId[i] = books[i].getId();
                }

                adapter = new BooksListAdapterTop(getContext(), bookList);
                try{
                    bookListElement.setAdapter(adapter);

                }
                catch(Exception ignore){

                }

            }



            @Override
            public void onFailure(Call<TopResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return view;
    }
}