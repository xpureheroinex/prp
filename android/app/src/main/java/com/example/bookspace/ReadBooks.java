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
import android.widget.ImageButton;
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

public class ReadBooks extends Fragment {
    ListView lvBooks1;
    BooksListAdapter adapter1;
    List<UserBook> mBooksList1;
    int[] booksId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.readbooks,container,false);
        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");
        lvBooks1 = view.findViewById(R.id.list1);


        Call<GetBooksResponse> getReadBooks = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getReadBooks("Bearer " + token);

        getReadBooks.enqueue(new Callback<GetBooksResponse>() {
            @Override
            public void onResponse(Call<GetBooksResponse> call, Response<GetBooksResponse> response) {
                UserBook[] userBooks = response.body().getInfo();
                booksId = new int[userBooks.length];

                mBooksList1 = new ArrayList<>();

                for(int i = 0; i < userBooks.length; i++){
                    mBooksList1.add(userBooks[i]);
                    booksId[i] = userBooks[i].getId();
                }

                adapter1 = new BooksListAdapter(getContext(),mBooksList1);
                lvBooks1.setAdapter(adapter1);
            }

            @Override
            public void onFailure(Call<GetBooksResponse> call, Throwable t) {

            }
        });


        lvBooks1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), BookPageActivity.class);
                intent.putExtra("bookId", mBooksList1.get(position).getId());
                startActivity(intent);
            }
        });

        return view;
    }
}
