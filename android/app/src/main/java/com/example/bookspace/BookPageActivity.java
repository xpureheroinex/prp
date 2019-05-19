package com.example.bookspace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class BookPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_page);
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
        final TextView txtView = (TextView)findViewById(R.id.textGrade);

        final RatingBar rating = (RatingBar)linearlayout.findViewById(R.id.ratingbar);
        ratingdialog.setCancelable(false);

        ratingdialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        txtView.setText("Grade: " + String.valueOf(rating.getRating()));
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

