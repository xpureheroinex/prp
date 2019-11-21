package com.example.bookspace.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.bookspace.Empty;
import com.example.bookspace.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        Empty startFragment = new Empty();
//        transaction.add(R.id.ll2,startFragment);
//        transaction.commit();

    }
//    public void onSelectFragment(View view) {
//        Fragment newFragment;
//        if (view == findViewById(R.id.buttonChangeUsername)) {
//            newFragment = new ChangeNameFragment();
//        } else if (view == findViewById(R.id.buttonChangePassword)) {
//            newFragment = new ChangePasswordFragment();
//        } else if (view == findViewById(R.id.buttonSetTargets)) {
//            newFragment = new SetTargetsFragment();
//        } else if (view == findViewById(R.id.buttonChangeAvatar)) {
//            newFragment = new ChangeAvatarFragment();
//        } else {
//            newFragment = new Empty();
//        }
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.ll2,newFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//
//    }

    public void onShowUser (View view){
        Toast toast = Toast.makeText(getApplicationContext(),"Your user name was changed",Toast.LENGTH_SHORT);
        toast.show();
    }
    public void onShowPassword(View view){
        Toast toast = Toast.makeText(getApplicationContext(),"Your password name was changed",Toast.LENGTH_SHORT);
        toast.show();
    }
    public void onShowAvatar(View view){
        Toast toast = Toast.makeText(getApplicationContext(),"Your avatar was changed",Toast.LENGTH_SHORT);
        toast.show();
    }
    public void onShowTarget(View view){
        Toast toast = Toast.makeText(getApplicationContext(),"Your targets name was changed",Toast.LENGTH_SHORT);
        toast.show();
    }
}
