package com.example.bookspace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookspace.model.CreateUserResponse;
import com.example.bookspace.model.RetrofitClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    SignInButton signInButton;
    Button signOutButton;
    TextView  statusTextView;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //GoogleAuth
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        if(account != null){
            signIn();
        }

        statusTextView = (TextView) findViewById(R.id.status_textview);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);

        TextView textViewSignIn = (TextView) signInButton.getChildAt(0);
        textViewSignIn.setText("Sign in with Google");

        //настраиваем toolbar
        Toolbar basicToolbar = findViewById(R.id.basicToolbar);
        setSupportActionBar(basicToolbar);
        getSupportActionBar().setTitle(null);
        basicToolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        //устанавливаем обработчик нажатия для back arrow иконки
        basicToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        //устанавливаем обработчик нажатия для "Forgot your password?"
        TextView textViewForgot = findViewById(R.id.textViewLogForgot);
        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecoveryActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.signOutButton:
                signOut();
                break;
        }
    }

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount acc = result.getSignInAccount();

            //Random password
            String engLower = "abcdefghijklmnopqrstuvwxyz";
            String engUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String digits = "0123456789";
            Random random = new Random();
            String password = engLower + engUpper + digits;
            char[] resultArr = new char[8]; //Массив-пароль
            for (int i = 0; i < resultArr.length; i++)
                resultArr[i] = password.charAt(random.nextInt(password.length()));

            Call<CreateUserResponse> call = RetrofitClient
                     .getInstance()
                     .getBookSpaceAPI()
                     .createUser(acc.getEmail(), "password");

            call.enqueue(new Callback<CreateUserResponse>() {
                    @Override
                    public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {
                        try{
                            String res = response.body().getMessage();
                            int res1 = response.body().getStatus();
                            statusTextView.setText(res + " " + res1);
                        } catch(NullPointerException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<CreateUserResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        }else{
            Toast.makeText(LoginActivity.this, "Something went wrong...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){

    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                statusTextView.setText("Signed out");            }
        });
    }
}
