package com.example.bookspace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NoticeActivity extends AppCompatActivity {
    public int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        Intent inten = getIntent();
        bookId = inten.getIntExtra("bookId", 11);

        TextView textAbout = findViewById(R.id.textAbout);
        textAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(getApplicationContext(), BookPageActivity.class);
                inten.putExtra("bookId", bookId);
                startActivity(inten);
            }
        });

        TextView textReviews = findViewById(R.id.textReviews);
        textReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(getApplicationContext(), ReviewsActivity.class);
                inten.putExtra("bookId", bookId);
                startActivity(inten);
            }
        });
    }

    public void onClickbuttonNote(View v){
        startActivity(new Intent(getApplicationContext(), AddNoteActivity.class));
    }
}
