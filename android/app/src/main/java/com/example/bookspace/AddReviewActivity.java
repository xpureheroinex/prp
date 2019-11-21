package com.example.bookspace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.Book;
import com.example.bookspace.model.books.GetBookResponse;
import com.example.bookspace.model.reviews.GetReviewsResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReviewActivity extends AppCompatActivity {
    public int bookId;
    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        token = prefs.getString("token", "token is null");

        Intent inten = getIntent();
        bookId = inten.getIntExtra("bookId", 11);

        TextView textCancel = findViewById(R.id.textCancel);
//        textCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent inten = new Intent(getApplicationContext(), ReviewsActivity.class);
//                inten.putExtra("bookId", bookId);
//                startActivity(inten);
//            }
//        });
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    public void onClickHide(View v){
        hideKeyboard();
    }

    public void onClickAddReview(View v) {
//        EditText editaddreview = findViewById(R.id.editAddReview);

//        if(editaddreview.getText().toString().matches("")){
//            Toast.makeText(AddReviewActivity.this, "You must fill the review field", Toast.LENGTH_LONG).show();
//            return;
//        }
//        Call<ResponseBody> call7 = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .postReview("Bearer " + token, bookId, editaddreview.getText().toString());
//
//        call7.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(AddReviewActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        final RatingBar rating = findViewById(R.id.ratingBarOnAddReview);
//
//        Call<ResponseBody> call8 = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .setRate("Bearer " + token, bookId, Math.round(rating.getRating()));
//
//        call8.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(AddReviewActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
//            }
//        });
//        hideKeyboard();
//        Intent inten = new Intent(getApplicationContext(), ReviewsActivity.class);
//        inten.putExtra("bookId", bookId);
//        startActivity(inten);
    }
}
