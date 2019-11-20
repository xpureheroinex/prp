package com.example.bookspace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bookspace.adapters.SearchBookAdapter;
import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.SearchBook;
import com.example.bookspace.model.books.SearchBookResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class SearchFragment extends Fragment {
    ListView bookListElement;
    SearchBookAdapter adapter;
    List<SearchBook> bookList;
    int[] booksId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment,container,false);
        SharedPreferences preferences = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE);
        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");
        final String query = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("query", "");
        bookListElement = view.findViewById(R.id.listSearch);


        Call<SearchBookResponse> searchBook = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .searchBook("Bearer " + token, query);

        searchBook.enqueue(new Callback<SearchBookResponse>() {
            @Override
            public void onResponse(Call<SearchBookResponse> call, Response<SearchBookResponse> response) {
                try{
                    SearchBook[] books = response.body().getBooks();
                    int count = books.length;
                    booksId = new int[count];

                    bookList = new ArrayList<>();

                    for(int i = 0; i < count; i++){
                        bookList.add(books[i]);
                        booksId[i] = books[i].getId();
                    }

                    adapter = new SearchBookAdapter(getContext(), bookList);
                    bookListElement.setAdapter(adapter);
                }
                catch (Exception ignore){}
            }

            @Override
            public void onFailure(Call<SearchBookResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        preferences.edit().remove("query").apply();

        bookListElement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), BookPageActivity.class);
                intent.putExtra("bookId", bookList.get(position).getId());
                startActivity(intent);
            }
        });

        return view;
    }
}
