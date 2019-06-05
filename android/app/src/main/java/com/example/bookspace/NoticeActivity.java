package com.example.bookspace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.Book;
import com.example.bookspace.model.books.GetBookResponse;
import com.example.bookspace.model.notes.GetNotesResponse;
import com.example.bookspace.model.notes.Note;
import com.example.bookspace.model.reviews.GetReviewsResponse;
import com.example.bookspace.model.reviews.Review;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeActivity extends AppCompatActivity {
    public int bookId;
    public String token;
    public int i;

    ListView lvBooks4;
    NoteAdapter adapter4;
    List<NoteClass> mBooksList4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        Intent inten = getIntent();
        bookId = inten.getIntExtra("bookId", 11);

        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        token = prefs.getString("token", "token is null");

        Call<GetNotesResponse> call = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getNotes("Bearer " + token, bookId);

        call.enqueue(new Callback<GetNotesResponse>() {
            @Override
            public void onResponse(Call<GetNotesResponse> call, Response<GetNotesResponse> response) {
                Note[] notes = response.body().getNotes();
                if (notes.length == 0) {
                    TextView txtView = (TextView) findViewById(R.id.textViewNoNotes);
                    txtView.setText("No notes on this book");
                    txtView.setVisibility(View.VISIBLE);
                } else {
                    mBooksList4 = new ArrayList<>();
                    for (i = 0; i < notes.length; i++) {
                        mBooksList4.add(new NoteClass(i, notes[i].getTitle(),
                                "\n" + notes[i].getText(), "Date: " + notes[i].getCreated()));
                    }
                    lvBooks4 = (ListView) findViewById(R.id.ListNotes);
                    adapter4 = new NoteAdapter(getApplicationContext(), mBooksList4);
                    lvBooks4.setAdapter(adapter4);
                    lvBooks4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //maybe new event for notes
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetNotesResponse> call, Throwable t) {
                Toast.makeText(NoticeActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(NoticeActivity.this, "Add this book to profile to add notes", Toast.LENGTH_LONG).show();
                else {
                    Intent inten = new Intent(getApplicationContext(), AddNoteActivity.class);
                    inten.putExtra("bookId", id);
                    startActivity(inten);
                }
            }

            @Override
            public void onFailure(Call<GetBookResponse> call, Throwable t) {
                Toast.makeText(NoticeActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
            }
        });
    }
}
