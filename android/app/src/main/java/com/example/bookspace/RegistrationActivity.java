package com.example.bookspace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar basicToolbar = findViewById(R.id.basicToolbar);
        setSupportActionBar(basicToolbar);
        getSupportActionBar().setTitle(null);
        basicToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        basicToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        Button signUp = findViewById(R.id.buttonRegSignUp);
        final EditText email = findViewById(R.id.editTextRegEmail);
        final EditText password = findViewById(R.id.editTextRegPassword);
        final EditText confirmPassword = findViewById(R.id.editTextRegConfirmPassword);
        final TextView errorMessage = findViewById(R.id.textRegErrorMessage);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String confirmPasswordString = confirmPassword.getText().toString();
                Matcher matcher = VALID_EMAIL_ADDRESS.matcher(emailString);
                boolean validData = true;

                if(!matcher.find()){
                    errorMessage.setText("Invalid e-mail, try again");
                    validData = false;
                }


                if(!passwordString.equals(confirmPasswordString) || passwordString.equals("") || confirmPasswordString.equals("")){
                    errorMessage.setText("Passwords aren't the same, try again");
                    validData = false;
                }

                if(validData){
                    errorMessage.setText("success");
                }

            }
        });
    }
}
