package com.example.bookspace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.Book;
import com.example.bookspace.model.books.GetBookResponse;
import com.example.bookspace.model.reviews.GetReviewsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsActivity extends AppCompatActivity {
    public int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Intent inten = getIntent();
        bookId = inten.getIntExtra("bookId", 1);

        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = prefs.getString("token", "token is null");
        final ListView lView = (ListView) findViewById(R.id.listReviews);

        Call<GetReviewsResponse> call = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getReviews("Bearer " + token, bookId);

        call.enqueue(new Callback<GetReviewsResponse>() {
            @Override
            public void onResponse(Call<GetReviewsResponse> call, Response<GetReviewsResponse> response) {
                }

            @Override
            public void onFailure(Call<GetReviewsResponse> call, Throwable t) {
                Toast.makeText(ReviewsActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
            }
        });

        TextView textAbout = findViewById(R.id.textAbout);
        textAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BookPageActivity.class));
            }
        });

        TextView textNotice = findViewById(R.id.textNotice);
        textNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NoticeActivity.class));
            }
        });
    }

    public void onClickbuttonReview(View v){
        startActivity(new Intent(getApplicationContext(), AddReviewActivity.class));
    }
}
