package com.example.bookspace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.Book;
import com.example.bookspace.model.books.GetBookResponse;
import com.example.bookspace.model.books.MainPageBook;
import com.example.bookspace.model.books.TopResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class TopFragment extends Fragment {

    ListView lvBooks4;
    BooksListAdapter2 adapter4;
    List<MainPageBook> mBooksList4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.top_fragment,container,false);
        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");
        lvBooks4 = view.findViewById(R.id.list4);

        Call<TopResponse> getTop = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getTop("Bearer " + token);

        getTop.enqueue(new Callback<TopResponse>() {
            @Override
            public void onResponse(Call<TopResponse> call, Response<TopResponse> response) {
                MainPageBook[] books = response.body().getBooks();

                mBooksList4 = new ArrayList<>();

                for(MainPageBook book : books){
                    mBooksList4.add(book);
                }

                adapter4 = new BooksListAdapter2(getContext(),mBooksList4);
                lvBooks4.setAdapter(adapter4);
            }

            @Override
            public void onFailure(Call<TopResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


        lvBooks4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(),"Hy" + view.getTag(),Toast.LENGTH_SHORT).show();
            }
        });
       return view;
    }
}