package com.example.bookspace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.Book;
import com.example.bookspace.model.books.GetBookResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_page);

        TextView textReviews = findViewById(R.id.textReviews);
        textReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReviewsActivity.class));
            }
        });

        TextView textNotice = findViewById(R.id.textNotice);
        textNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NoticeActivity.class));
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
                .getBook("Bearer " + token, 11);

        call7.enqueue(new Callback<GetBookResponse>() {
            @Override
            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                Book resp = response.body().getBook();
                textName.setText(resp.getTitle());
                textGrade.setText("Grade: " + String.valueOf(resp.getRate()));
                textGenre.setText("Genre: " + resp.getGenre());
                textAuthor.setText("Author: " + resp.getAuthor());
                textNumberofPages.setText("Number of pages: " + String.valueOf(resp.getPages()));
                Book[] resp1 = resp.getRecs();
                if(resp1.length == 0){
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ListSimilarBooks);
                    TextView txt1Book = new TextView(BookPageActivity.this);
                    txt1Book.setTextColor(getResources().getColor(R.color.colorTextPrimary));
                    txt1Book.setTextSize(24);
                    txt1Book.setText("Not found");
                    txt1Book.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    linearLayout.addView(txt1Book);
                }else{
                    for(int i = 0; i < resp1.length; i++) {
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ListSimilarBooks);
                        TextView txt1Book = new TextView(BookPageActivity.this);
                        txt1Book.setBackgroundResource(R.drawable.textlines);
                        txt1Book.setTextColor(getResources().getColor(R.color.colorTextPrimaryForZhenYa));
                        txt1Book.setTextSize(17);
                        //txt1Book.setHeight(100);
                        txt1Book.setText(resp1[i].getTitle() + "\n\n" + "Author: " +
                                resp1[i].getAuthor() + "   \nGenre: " + resp1[i].getGenre());
                        linearLayout.addView(txt1Book);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBookResponse> call, Throwable t) {
                t.printStackTrace();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Choose status:"); // заголовок для диалога

                    builder.setCancelable(true);
                    builder.setItems(statusName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            // TODO Auto-generated method stub
                            Toast.makeText(getApplicationContext(),
                                    "Selected status: " + statusName[item],
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
                                .setRate("Bearer " + token_rate, 11, Math.round(rating.getRating()));

                        call8.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Toast.makeText(user_page.this, "rate = 5", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

                        try{
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e){
                        }

                        Call<GetBookResponse> call_getRate = RetrofitClient
                                .getInstance()
                                .getBookSpaceAPI()
                                .getBook("Bearer " + token_rate, 11);

                        call_getRate.enqueue(new Callback<GetBookResponse>() {
                            @Override
                            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                                Book resp = response.body().getBook();
                                textGrade.setText("Grade: " + String.valueOf(resp.getRate()));
                            }

                            @Override
                            public void onFailure(Call<GetBookResponse> call, Throwable t) {
                                t.printStackTrace();
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

