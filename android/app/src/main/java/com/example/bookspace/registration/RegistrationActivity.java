package com.example.bookspace.registration;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.bookspace.MainActivity;
import com.example.bookspace.R;
import com.example.bookspace.SuccessfullyRegisteredFragment;
import com.example.bookspace.model.registration.CreateUserResponse;
import com.example.bookspace.model.RetrofitClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements SuccessfullyRegisteredFragment.OnFragmentInteractionListener {

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

        //далее работаем с фрагментом

        FrameLayout fragmentContainer = findViewById(R.id.registrationFragmentContainer);


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

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String confirmPasswordString = confirmPassword.getText().toString();
                Matcher matcher = VALID_EMAIL_ADDRESS.matcher(emailString);
                boolean validData = true;

                if(!matcher.find()){
                    email.setError(getText(R.string.invalidMail));
                    validData = false;
                }

                if(!passwordString.equals(confirmPasswordString) || passwordString.equals("") || confirmPasswordString.equals("")){
                    password.setError(getText(R.string.invalidPass));
                    validData = false;
                }

                if(password.length() < 6){
                    password.setError(getText(R.string.passLength));

                }

                if(validData){
                    Call<CreateUserResponse> call = RetrofitClient
                            .getInstance()
                            .getBookSpaceAPI()
                            .createUser(emailString, passwordString);

                    call.enqueue(new Callback<CreateUserResponse>() {
                        @Override
                        public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {
                            try{
                                CreateUserResponse resp = response.body();
                                if(resp.getStatus() == 400){
                                    email.setError(getText(R.string.existUser));
                                }
                                else{
                                    openFragment();
                                }
                            } catch(NullPointerException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<CreateUserResponse> call, Throwable t) {
                            Toast.makeText(RegistrationActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public void openFragment(){
        SuccessfullyRegisteredFragment fragment = SuccessfullyRegisteredFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.add(R.id.registrationFragmentContainer, fragment, "SUCCESSFULLY_REGISTERED_FRAGMENT").commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

}
