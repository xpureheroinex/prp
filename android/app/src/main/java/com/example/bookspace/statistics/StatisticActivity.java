package com.example.bookspace.statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.bookspace.R;

public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic_activity);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        StatisticYear startFragment = new StatisticYear();
        transaction.add(R.id.ll2,startFragment);
        transaction.commit();


    }

    public void onSelectFragment3(View view) {
        Fragment newFragment;
        if (view == findViewById(R.id.btnfr1)) {
            newFragment = new StatisticYear();
        } else if (view == findViewById(R.id.btnfr2)) {
            newFragment = new StatisticMonth();
        } else if (view == findViewById(R.id.btnfr3)) {
            newFragment = new StatisticWeek();
        } else {
            newFragment = new StatisticYear();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ll2,newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
