package com.example.bookspace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

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

//    public void onClickbuttonReview(View v){
//        startActivity(new Intent(getApplicationContext(), AddReviewActivity.class));
//    }
}
