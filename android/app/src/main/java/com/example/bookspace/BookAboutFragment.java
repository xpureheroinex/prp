package com.example.bookspace;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.adapters.SimilarBooksAdapter;
import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.SimilarBooks;
import com.example.bookspace.model.books.Book;
import com.example.bookspace.model.books.GetBookResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class BookAboutFragment extends Fragment {

    ListView booksListView;
    SimilarBooksAdapter similarBooksAdapter;
    List<SimilarBooks> similarBooksList;
    int[] idBook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_book_about, container, false);
        final TextView textName = view.findViewById(R.id.textName);
        final TextView textGrade = view.findViewById(R.id.textGrade);
        final TextView textGenreAndPages = view.findViewById(R.id.textGenreAndPages);
        final TextView textAuthor = view.findViewById(R.id.textAuthor);
        String token = getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE)
                .getString("token", "token is null");


        if (getArguments() != null){
            int bookId = getArguments().getInt("bookId");

            Call<GetBookResponse> call7 = RetrofitClient
                    .getInstance()
                    .getBookSpaceAPI()
                    .getBook("Bearer " + token, bookId);

            call7.enqueue(new Callback<GetBookResponse>() {
                @Override
                public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                    Book resp = response.body().getBook();
                    textName.setText(resp.getTitle());
                    textGrade.setText(String.valueOf(resp.getRate()));

                    //        set relevant rating color
                    double rateValue = resp.getRate();
                    if(rateValue >= 0 && rateValue < 3){
                        textGrade.setTextColor(Color.parseColor("#D50000"));
                    }
                    else if(rateValue >= 3 && rateValue < 4){
                        textGrade.setTextColor(Color.parseColor("#ffd600"));
                    }
                    else if(rateValue >= 4 && rateValue <= 5){
                        textGrade.setTextColor(Color.parseColor("#1faa00"));
                    }

                    textGenreAndPages.setText(resp.getGenre().concat(", " + String.valueOf(resp.getPages()).concat(" pages")));
                    textAuthor.setText(resp.getAuthor());

                    final Book[] resp1 = resp.getRecs();
                    if(resp1.length == 0){
//                    TextView txt1Book = new TextView(BookPageActivity.this);
//                    txt1Book.setTextColor(getResources().getColor(R.color.colorTextPrimary));
//                    txt1Book.setTextSize(24);
//                    txt1Book.setText("Similar books not found");
                    }else{
                        similarBooksList = new ArrayList<>();
                        idBook = new int[resp1.length];
                        for(int i = 0; i < resp1.length; i++) {
                            similarBooksList.add(new SimilarBooks(i,resp1[i].getTitle(),
                                    resp1[i].getAuthor(),
                                    resp1[i].getGenre()));
                            idBook[i] = resp1[i].getId();
                        }
                        booksListView = view.findViewById(R.id.ListOfBooks);
                        similarBooksAdapter = new SimilarBooksAdapter(getContext(), similarBooksList);
                        booksListView.setAdapter(similarBooksAdapter);
                        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent inten = new Intent(getContext(), BookPageActivity.class);
                                inten.putExtra("bookId", idBook[position]);
                                startActivity(inten);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<GetBookResponse> call, Throwable t) {
                    Toast.makeText(getContext(), getText(R.string.wrongRes), Toast.LENGTH_LONG).show();
                }
            });
        }
        return view;
    }
}
