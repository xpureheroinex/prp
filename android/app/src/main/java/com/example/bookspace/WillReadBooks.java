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

public class WillReadBooks extends Fragment {
    ListView lvBooks3;
    BooksListAdapter adapter3;
    List<UserBook> mBooksList3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.willreadbooks,container,false);

        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");
        lvBooks3 = view.findViewById(R.id.list3);

        Call<GetBooksResponse> getFutureBooks = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getFutureBooks("Bearer " + token);

        getFutureBooks.enqueue(new Callback<GetBooksResponse>() {
            @Override
            public void onResponse(Call<GetBooksResponse> call, Response<GetBooksResponse> response) {
                UserBook[] userBooks = response.body().getInfo();
                mBooksList3 = new ArrayList<>();

                for(UserBook book : userBooks){
                    mBooksList3.add(book);
                }

                adapter3 = new BooksListAdapter(getContext(),mBooksList3);
                lvBooks3.setAdapter(adapter3);
            }

            @Override
            public void onFailure(Call<GetBooksResponse> call, Throwable t) {
            }
        });

        lvBooks3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(),"Hy" + view.getTag(),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
