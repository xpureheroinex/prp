package com.example.bookspace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TopRecommendations extends AppCompatActivity

    {

        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_recommendations);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        StatisticYear startFragment = new StatisticYear();
        transaction.add(R.id.ll3, startFragment);
        transaction.commit();


    }

        public void onSelectFragment1 (View view){
        Fragment newFragment;
        if (view == findViewById(R.id.btnfr4)) {
            newFragment = new TopFragment();
        } else if (view == findViewById(R.id.btnfr5)) {
            newFragment = new RecommendFragment();
        }  else {
            newFragment = new TopFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ll3, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    }
