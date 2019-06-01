package com.example.bookspace;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TopRecommendations extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.top_recommendations);

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            RecommendFragment startFragment = new RecommendFragment();
            transaction.add(R.id.ll3, startFragment);
            transaction.commit();


        }

        @Nullable

        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.top_recommendations, container, false);
        }

        public void onSelectFragment1(View view) {
            Fragment newFragment;
            if (view == findViewById(R.id.btnfr4)) {
                newFragment = new TopFragment();
            } else if (view == findViewById(R.id.btnfr5)) {
                newFragment = new RecommendFragment();
            } else {
                newFragment = new TopFragment();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.ll3, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }

