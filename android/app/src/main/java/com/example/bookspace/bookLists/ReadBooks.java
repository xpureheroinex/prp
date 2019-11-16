package com.example.bookspace.bookLists;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bookspace.BookPageActivity;
import com.example.bookspace.BooksListAdapter;
import com.example.bookspace.R;
import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.GetBooksResponse;
import com.example.bookspace.model.books.UserBook;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ReadBooks extends Fragment {
    ListView readBooksListView;
    BooksListAdapter booksListAdapter;
    List<UserBook> userBookList;
    int[] booksId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.readbooks,container,false);
        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");
        readBooksListView = view.findViewById(R.id.readBooksList);


        Call<GetBooksResponse> getReadBooks = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getReadBooks("Bearer " + token);

        getReadBooks.enqueue(new Callback<GetBooksResponse>() {
            @Override
            public void onResponse(Call<GetBooksResponse> call, Response<GetBooksResponse> response) {
                UserBook[] userBooks = response.body().getInfo();
                booksId = new int[userBooks.length];
                userBookList = new ArrayList<>();

                for(int i = 0; i < userBooks.length; i++){
                    userBooks[i].setTitle(userBooks[i].getTitle());
                    userBookList.add(userBooks[i]);
                    booksId[i] = userBooks[i].getId();
                }

                booksListAdapter = new BooksListAdapter(getContext(), userBookList);
                readBooksListView.setAdapter(booksListAdapter);
            }

            @Override
            public void onFailure(Call<GetBooksResponse> call, Throwable t) {

            }
        });

        readBooksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), BookPageActivity.class);
                intent.putExtra("bookId", userBookList.get(position).getId());
                startActivity(intent);
            }
        });

        return view;
    }

}
