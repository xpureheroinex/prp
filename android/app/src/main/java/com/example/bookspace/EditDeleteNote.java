package com.example.bookspace;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.notes.GetNotesResponse;
import com.example.bookspace.model.notes.Note;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDeleteNote extends AppCompatActivity {
    public int bookId;
    public String token;
    public int noteId;
    public String Title;
    public String Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_delete_note);

        Intent inten = getIntent();
        bookId = inten.getIntExtra("bookId", 11);
        noteId = inten.getIntExtra("noteId", 1);
        Title = inten.getStringExtra("Title");
        Text = inten.getStringExtra("Text");

        EditText eTitle = (EditText) findViewById(R.id.editTitleEdit);
        eTitle.setText(Title, TextView.BufferType.EDITABLE);

        EditText eNote = (EditText) findViewById(R.id.editAddNoteEdit);
        eNote.setText(Text, TextView.BufferType.EDITABLE);

        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        token = prefs.getString("token", "token is null");

        TextView textCancel = findViewById(R.id.textCancel3);
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(getApplicationContext(), NoticeActivity.class);
                inten.putExtra("bookId", bookId);
                startActivity(inten);
            }
        });
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    public void onClickHide(View v){
        hideKeyboard();
    }

    public void onClickEdit(View v){
        EditText eTitle = (EditText) findViewById(R.id.editTitleEdit);
        EditText eNote = (EditText) findViewById(R.id.editAddNoteEdit);

        if(eTitle.getText().toString().matches("") && eNote.getText().toString().matches("")){
            Toast.makeText(EditDeleteNote.this, "You must fill in all fields", Toast.LENGTH_LONG).show();
            return;
        } else if(eTitle.getText().toString().matches("")) {
            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getBookSpaceAPI()
                    .editNote("Bearer " + token, noteId, null, eNote.getText().toString());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(EditDeleteNote.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                }
            });
            Intent inten = new Intent(getApplicationContext(), NoticeActivity.class);
            inten.putExtra("bookId", bookId);
            startActivity(inten);
        } else if(eNote.getText().toString().matches("")) {
            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getBookSpaceAPI()
                    .editNote("Bearer " + token, noteId, eTitle.getText().toString(), null);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(EditDeleteNote.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                }
            });
            Intent inten = new Intent(getApplicationContext(), NoticeActivity.class);
            inten.putExtra("bookId", bookId);
            startActivity(inten);
        } else {
            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getBookSpaceAPI()
                    .editNote("Bearer " + token, noteId, eTitle.getText().toString(), eNote.getText().toString());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(EditDeleteNote.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                }
            });
            Intent inten = new Intent(getApplicationContext(), NoticeActivity.class);
            inten.putExtra("bookId", bookId);
            startActivity(inten);
        }
    }

    public void onClickDelete(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure, you want to delete this note?")
                .setCancelable(true).setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Call<ResponseBody> call = RetrofitClient
                                    .getInstance()
                                    .getBookSpaceAPI()
                                    .deleteNote("Bearer " + token, noteId);

                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(EditDeleteNote.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                                }
                            });
                            Intent inten = new Intent(getApplicationContext(), NoticeActivity.class);
                            inten.putExtra("bookId", bookId);
                            startActivity(inten);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}