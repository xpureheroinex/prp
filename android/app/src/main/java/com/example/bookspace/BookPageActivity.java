package com.example.bookspace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.solver.widgets.ConstraintTableLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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
    public int i;
    public int[] idBook;

    ListView lvBooks4;
    ZhenYaArrayAdapter adapter4;
    List<SimilarBooks> mBooksList4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_page);

        Intent inten = getIntent();
        bookId = inten.getIntExtra("bookId", 11);

        TextView textReviews = findViewById(R.id.textReviews);
        textReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(getApplicationContext(), ReviewsActivity.class);
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

        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = prefs.getString("token", "token is null");
        final TextView textName = (TextView) findViewById(R.id.textName);
        final TextView textGrade = (TextView) findViewById(R.id.textGrade);
        final TextView textGenre = (TextView) findViewById(R.id.textGenre);
        final TextView textAuthor = (TextView) findViewById(R.id.textAuthor);
        final TextView textNumberofPages = (TextView) findViewById(R.id.textNumberOfPages);

        Call<GetBookResponse> call7 = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getBook("Bearer " + token, bookId);

        call7.enqueue(new Callback<GetBookResponse>() {
            @Override
            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                Book resp = response.body().getBook();
                textName.setText(resp.getTitle());
                textGrade.setText("Grade: " + String.valueOf(resp.getRate()));
                textGenre.setText("Genre: " + resp.getGenre());
                textAuthor.setText("Author: " + resp.getAuthor());
                textNumberofPages.setText("Number of pages: " + String.valueOf(resp.getPages()));
                final Book[] resp1 = resp.getRecs();
                if(resp1.length == 0){
                    ConstraintLayout cLayout = (ConstraintLayout) findViewById(R.id.constraintlayout);
                    TextView txt1Book = new TextView(BookPageActivity.this);
                    txt1Book.setTextColor(getResources().getColor(R.color.colorTextPrimary));
                    txt1Book.setTextSize(24);
                    txt1Book.setText("Similar books not found in our database");
                    cLayout.addView(txt1Book);
                }else{
                    mBooksList4 = new ArrayList<>();
                    idBook = new int[resp1.length];
                    for(i = 0; i < resp1.length; i++) {
                        mBooksList4.add(new SimilarBooks(i,resp1[i].getTitle(),
                                "Author: " + resp1[i].getAuthor(),
                                "Genre:" + resp1[i].getGenre()));
                        idBook[i] = resp1[i].getId();
                    }
                    lvBooks4 = (ListView) findViewById(R.id.ListOfBooks);
                    adapter4 = new ZhenYaArrayAdapter(getApplicationContext(),mBooksList4);
                    lvBooks4.setAdapter(adapter4);
                    lvBooks4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent inten = new Intent(getApplicationContext(), BookPageActivity.class);
                            inten.putExtra("bookId", idBook[position]);
                            startActivity(inten);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetBookResponse> call, Throwable t) {
                Toast.makeText(BookPageActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private final int IDD_LIST_Status = 1;

    public void editStatus(View v) {
        switch (v.getId()) {
            case R.id.textEditStatus:
                showDialog(IDD_LIST_Status);
                break;
        }
    }

    protected Dialog onCreateDialog ( int id){
            switch (id) {
                case IDD_LIST_Status:

                    final String[] statusName = {"Read", "Reading", "Will Read"};
                    SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                    final String token = prefs.getString("token", "token is null");

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Choose status:"); // заголовок для диалога

                    builder.setCancelable(true);
                    builder.setItems(statusName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            // TODO Auto-generated method stub
                            if(statusName[item].equals("Read")){
                                status = "DN";
                            } else if(statusName[item].equals("Reading")){
                                status = "IP";
                            } else if(statusName[item].equals("Will Read")){
                                status = "WR";
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
                            Toast.makeText(getApplicationContext(),"Selected status: " + statusName[item],
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    return builder.create();

                default:
                    return null;
            }
    }

    public void showRatingDialog(View view) {

        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(this);

        ratingdialog.setTitle("Rate it!");

        View linearlayout = getLayoutInflater().inflate(R.layout.ratingdialog, null);
        ratingdialog.setView(linearlayout);
        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        final String token_rate = prefs.getString("token", "token is null");
        final TextView textGrade = (TextView) findViewById(R.id.textGrade);

        final RatingBar rating = (RatingBar)linearlayout.findViewById(R.id.ratingbar);
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
                                textGrade.setText("Grade: " + String.valueOf(resp.getRate()));
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

