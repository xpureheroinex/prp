package com.example.bookspace;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class RecoveryActivity extends AppCompatActivity implements RecoveryFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        //настраиваем toolbar
        Toolbar basicToolbar = findViewById(R.id.basicToolbar);
        setSupportActionBar(basicToolbar);
        getSupportActionBar().setTitle(null);
        basicToolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        //устанавливаем обработчик нажатия для back arrow иконки
        basicToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        //далее работаем с фрагментом

        FrameLayout fragmentContainer = findViewById(R.id.recFragmentContainer);
        TextView fragmentTitle = findViewById(R.id.textViewFragTitle);
        Button nextButton = findViewById(R.id.buttonRecNext);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment();
            }
        });
    }

    public void openFragment(){
        RecoveryFragment fragment = RecoveryFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.add(R.id.recFragmentContainer, fragment, "RECOVERY_FRAGMENT").commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}