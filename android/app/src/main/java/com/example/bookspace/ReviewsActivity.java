package com.example.bookspace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.SimilarBooks;
import com.example.bookspace.model.books.Book;
import com.example.bookspace.model.books.GetBookResponse;
import com.example.bookspace.model.reviews.GetReviewsResponse;
import com.example.bookspace.model.reviews.Review;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsActivity extends AppCompatActivity {
    public int bookId;
    public int i;

    ListView lvBooks4;
    ReviewsAdapter adapter4;
    List<ReviewsClass> mBooksList4;

    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Intent inten = getIntent();
        bookId = inten.getIntExtra("bookId", 11);

        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        token = prefs.getString("token", "token is null");

        Call<GetReviewsResponse> call = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getReviews("Bearer " + token, bookId);

        call.enqueue(new Callback<GetReviewsResponse>() {
            @Override
            public void onResponse(Call<GetReviewsResponse> call, Response<GetReviewsResponse> response) {
                Review[] info = response.body().getInfo();
                if (info == null) {
                    TextView txtView = (TextView) findViewById(R.id.textNoReview);
                    txtView.setText("No reviews about this book,\n" +
                            "you can be the first");
                    txtView.setVisibility(View.VISIBLE);
                } else {
                    mBooksList4 = new ArrayList<>();
                    for (i = 0; i < info.length; i++) {
                        mBooksList4.add(new ReviewsClass(i, info[i].getUsername(),
                                "\n" + info[i].getText(), "Date: " + info[i].getCreated()));
                    }
                    lvBooks4 = (ListView) findViewById(R.id.listReviews);
                    adapter4 = new ReviewsAdapter(getApplicationContext(), mBooksList4);
                    lvBooks4.setAdapter(adapter4);
                    lvBooks4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //maybe new event for reviews
                        }
                    });
                }
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
                Intent inten = new Intent(getApplicationContext(), BookPageActivity.class);
                inten.putExtra("bookId", bookId);
                startActivity(inten);
            }
        });

        TextView textNotice = findViewById(R.id.textNotice);
        textNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(getApplicationContext(), NoticeActivity.class);
                inten.putExtra("bookId", bookId);
                startActivity(inten);
            }
        });

        Button toAddReview = (Button) findViewById(R.id.buttonReviews);
        toAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickbutton(token, bookId);
            }
        });

    }

    public void onClickbutton(String token, int bookId){
        final String token1 = token;
        final int id = bookId;

        Call<GetBookResponse> call7 = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getBook("Bearer " + token1, id);

        call7.enqueue(new Callback<GetBookResponse>() {
            @Override
            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                Book resp = response.body().getBook();
                if(resp.getList() == null)
                    Toast.makeText(ReviewsActivity.this, "Add this book to profile to add review", Toast.LENGTH_LONG).show();
                else {
                    Call<GetReviewsResponse> call7 = RetrofitClient
                            .getInstance()
                            .getBookSpaceAPI()
                            .getReviews("Bearer " + token1, id);

                    call7.enqueue(new Callback<GetReviewsResponse>() {
                        @Override
                        public void onResponse(Call<GetReviewsResponse> call, Response<GetReviewsResponse> response) {
                            int status1 = response.body().getStatus();
                            if (status1 == 404) {
                                Intent inten = new Intent(getApplicationContext(), AddReviewActivity.class);
                                inten.putExtra("bookId", id);
                                startActivity(inten);
                            } else {
                                Boolean status = response.body().getReview();
                                if (status == false)
                                    Toast.makeText(ReviewsActivity.this, "You already added review", Toast.LENGTH_LONG).show();
                                else {
                                    Intent inten = new Intent(getApplicationContext(), AddReviewActivity.class);
                                    inten.putExtra("bookId", id);
                                    startActivity(inten);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetReviewsResponse> call, Throwable t) {
                            Toast.makeText(ReviewsActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetBookResponse> call, Throwable t) {
                Toast.makeText(ReviewsActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
            }
        });
    }
}
