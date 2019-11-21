package com.example.bookspace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNoteActivity extends AppCompatActivity {
    public int bookId;
    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_notice);

        Intent inten = getIntent();
        bookId = inten.getIntExtra("bookId", 11);

        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        token = prefs.getString("token", "token is null");

    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    public void onClickHide(View v){
        hideKeyboard();
    }

    public void onClickAddNote(View v){
        EditText eTitle = findViewById(R.id.editTitle);
        EditText eNote = findViewById(R.id.editAddNote);

        if(eTitle.getText().toString().matches("") || eNote.getText().toString().matches("")){
            Toast.makeText(AddNoteActivity.this, "You must fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }

        Call<ResponseBody> call7 = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .postNote("Bearer " + token, bookId, eTitle.getText().toString(), eNote.getText().toString());

        call7.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddNoteActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
            }
        });
    }
}
