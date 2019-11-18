package com.example.bookspace;

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

import com.example.bookspace.adapters.BooksListAdapterTop;
import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.MainPageBook;
import com.example.bookspace.model.books.TopResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class TopFragment extends Fragment {

    ListView bookListTop;
    BooksListAdapterTop adapterTop;
    List<MainPageBook> mainPageBooks;
    int[] booksId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.top_fragment, container,false);
        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");
        bookListTop = view.findViewById(R.id.listTop);

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

                mainPageBooks = new ArrayList<>();

                for(int i = 0; i < count; i++){
                    books[i].setTitle(books[i].getTitle());
                    mainPageBooks.add(books[i]);
                    booksId[i] = books[i].getId();
                }

                adapterTop = new BooksListAdapterTop(getContext(), mainPageBooks);
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
                Intent intent = new Intent(getContext(), BookPageActivity.class);
                intent.putExtra("bookId", mainPageBooks.get(position).getId());
                startActivity(intent);
            }
        });

       return view;
    }
}
