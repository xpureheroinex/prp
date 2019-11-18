package com.example.bookspace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.SimilarBooks;
import com.example.bookspace.model.books.Book;
import com.example.bookspace.model.books.GetBookResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookPageActivity extends AppCompatActivity {
    public String status;
    public int bookId;
    ImageButton editStatusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_page);

        Intent inten = getIntent();
        bookId = inten.getIntExtra("bookId", 11);

        //region настраиваем toolbar
        Toolbar basicToolbar = findViewById(R.id.basicToolbar);
        setSupportActionBar(basicToolbar);
        getSupportActionBar().setTitle("Book Page");
        basicToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        //endregion

        //region устанавливаем обработчик нажатия для back arrow иконки
        basicToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), user_page.class));
            }
        });
        //endregion

        Bundle bundle = new Bundle();
        bundle.putInt("bookId", bookId);
        BookAboutFragment bookAboutFragment = new BookAboutFragment();
        bookAboutFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.bookPageFragmentContainer, bookAboutFragment).commit();

    }

    private final int IDD_LIST_Status = 1;

    public void editStatus(View v) {
        if (v.getId() == R.id.editStatusButton) {
            showDialog(IDD_LIST_Status);
        }

    }

    protected Dialog onCreateDialog ( int id){
        if (id == IDD_LIST_Status) {
            final String[] statusName = {"Read", "Reading", "Will Read"};
            SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
            final String token = prefs.getString("token", "token is null");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose status:"); // заголовок для диалога

            builder.setCancelable(true);
            builder.setItems(statusName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    switch (statusName[item]) {
                        case "Read":
                            status = "DN";
                            break;
                        case "Reading":
                            status = "IP";
                            break;
                        case "Will Read":
                            status = "WR";
                            break;
                    }
                    Call<ResponseBody> call7 = RetrofitClient
                            .getInstance()
                            .getBookSpaceAPI()
                            .setStatus("Bearer " + token, bookId, status);

                    call7.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(BookPageActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Selected status: " + statusName[item],
                            Toast.LENGTH_SHORT).show();
                }
            });
            return builder.create();
        }
        return null;
    }

    public void showRatingDialog(View view) {

        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(this);

        ratingdialog.setTitle("Rate it!");

        View linearlayout = getLayoutInflater().inflate(R.layout.ratingdialog, null);
        ratingdialog.setView(linearlayout);
        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        final String token_rate = prefs.getString("token", "token is null");
        final TextView textGrade = findViewById(R.id.textGrade);

        final RatingBar rating = linearlayout.findViewById(R.id.ratingbar);
        ratingdialog.setCancelable(false);

        ratingdialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Call<ResponseBody> call8 = RetrofitClient
                                .getInstance()
                                .getBookSpaceAPI()
                                .setRate("Bearer " + token_rate, bookId, Math.round(rating.getRating()));

                        call8.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(BookPageActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                            }
                        });

                        try{
                            Thread.sleep(100);
                        }
                        catch (InterruptedException e){
                        }

                        Call<GetBookResponse> call_getRate = RetrofitClient
                                .getInstance()
                                .getBookSpaceAPI()
                                .getBook("Bearer " + token_rate, bookId);

                        call_getRate.enqueue(new Callback<GetBookResponse>() {
                            @Override
                            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                                Book resp = response.body().getBook();
                                textGrade.setText(String.valueOf(resp.getRate()));
                            }

                            @Override
                            public void onFailure(Call<GetBookResponse> call, Throwable t) {
                                Toast.makeText(BookPageActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                            }
                        });
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        ratingdialog.create();
        ratingdialog.show();
    }
}

