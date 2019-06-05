package com.example.bookspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
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

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class TopFragment extends Fragment {

    ListView bookListTop;
    BooksListAdapterTop adapterTop;
    List<MainPageBook> mBooksListTop;
    final String LOG_TAG = "myLogs";
    int[] booksId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.top_fragment,container,false);
        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");
        bookListTop = view.findViewById(R.id.listTop);
        ImageButton statusButton = view.findViewById(R.id.addbtn1);

        Call<TopResponse> getTop = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getTop("Bearer " + token);

        getTop.enqueue(new Callback<TopResponse>() {
            @Override
            public void onResponse(Call<TopResponse> call, Response<TopResponse> response) {
                MainPageBook[] books = response.body().getBooks();
                int count = books.length;
                booksId = new int[count];

                mBooksListTop = new ArrayList<>();

                for(int i = 0; i < count; i++){
                    mBooksListTop.add(books[i]);
                    booksId[i] = books[i].getId();
                }

                adapterTop = new BooksListAdapterTop(getContext(), mBooksListTop);
                bookListTop.setAdapter(adapterTop);
            }

            @Override
            public void onFailure(Call<TopResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        bookListTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), String.valueOf(booksId[position]), Toast.LENGTH_SHORT).show();
            }
        });

       return view;
    }
}