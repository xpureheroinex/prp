package com.example.bookspace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.GetBooksResponse;
import com.example.bookspace.model.notes.GetNotesResponse;
import com.example.bookspace.model.notes.Note;
import com.example.bookspace.model.profile.ProfileResponse;
import com.example.bookspace.model.profile.User;
import com.example.bookspace.model.reviews.GetReviewsResponse;
import com.example.bookspace.model.reviews.Review;
import com.example.bookspace.model.statistics.SetPlanResponse;

import java.security.PrivateKey;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class user_page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView mReadTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mReadTextView = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_read));
        initializeCountDrawer();
        mReadTextView = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_reading));
        initializeCountDrawer1();
        mReadTextView = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_willread));
        initializeCountDrawer2();

        setTitle("BookSpace");
        TopRecommends2 topRecommends2 = new TopRecommends2();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragments, topRecommends2).commit();

        FragmentManager fom = getSupportFragmentManager();
        FragmentTransaction transaction = fom.beginTransaction();
        TopFragment startFragment = new TopFragment();
        transaction.add(R.id.ll3, startFragment);
        transaction.commit();

        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = prefs.getString("token", "token is null");



        //-------------------- test queries
        

        //получаем информацию о книге

//        Call<GetBookResponse> call7 = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .getBook("Bearer " + token, 10);
//
//        call7.enqueue(new Callback<GetBookResponse>() {
//            @Override
//            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
//                Book resp = response.body().getBook();
//                Toast.makeText(user_page.this, resp.getTitle(), Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onFailure(Call<GetBookResponse> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });

        //выставляем оценку

//        Call<ResponseBody> call8 = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .setRate("Bearer " + token, 10, 5);
//
//        call8.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
////                Toast.makeText(user_page.this, "rate = 5", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });

        //получаем отзывы


//        Call<GetReviewsResponse> callTest10 = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .getReviews("Bearer " + token, 10, "month");
//
//        callTest10.enqueue(new Callback<GetReviewsResponse>() {
//            @Override
//            public void onResponse(Call<GetReviewsResponse> call, Response<GetReviewsResponse> response) {
//                Review[] rews = response.body().getInfo();
//
//                Toast.makeText(getApplicationContext(), rews[0].getText(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<GetReviewsResponse> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });

        //отправляем отзыв

//        Call<ResponseBody> calltt = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .postReview("Bearer " + token, 10, "test review");
//
//        calltt.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });

        //получаем заметки

//        Call<GetNotesResponse> callpp = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .getNotes("Bearer " + token, 10);
//
//        callpp.enqueue(new Callback<GetNotesResponse>() {
//            @Override
//            public void onResponse(Call<GetNotesResponse> call, Response<GetNotesResponse> response) {
//                Note[] notes = response.body().getNotes();
//
//                Toast.makeText(getApplicationContext(), notes[0].getText(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<GetNotesResponse> call, Throwable t) {
//
//            }
//        });

        //редактирование заметки

//        Call<ResponseBody> callrr = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .editNote("Bearer " + token, 6, null, "hello");
//
//        callrr.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });

//        Call<GetBooksResponse> call100 = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .getReadBooks("Bearer " + token);
//
//        call100.enqueue(new Callback<GetBooksResponse>() {
//            @Override
//            public void onResponse(Call<GetBooksResponse> call, Response<GetBooksResponse> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<GetBooksResponse> call, Throwable t) {
//
//            }
//        });

        Button bu = (Button) findViewById(R.id.button2);
        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BookPageActivity.class));
            }
        });
    }


    private void initializeCountDrawer(){
        mReadTextView.setGravity(Gravity.CENTER_VERTICAL);
        mReadTextView.setTypeface(null, Typeface.BOLD);
        mReadTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        mReadTextView.setText("4");
    }
    private void initializeCountDrawer1(){
        mReadTextView.setGravity(Gravity.CENTER_VERTICAL);
        mReadTextView.setTypeface(null, Typeface.BOLD);
        mReadTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        mReadTextView.setText("3");
    }
    private void initializeCountDrawer2(){
        mReadTextView.setGravity(Gravity.CENTER_VERTICAL);
        mReadTextView.setTypeface(null, Typeface.BOLD);
        mReadTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        mReadTextView.setText("1");
    }

public void Delete(final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this book?")
        .setCancelable(true).setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        Toast toast = Toast.makeText(getApplicationContext(),"The book was deleted",Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void Add(View view){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Choose status:");
//
//        String[] items= {"Read", "Reading", "Will Read"};
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which){
//                    case 0:
//                        Toast toast = Toast.makeText(getApplicationContext(),"The status of book was changed on read",Toast.LENGTH_SHORT);
//                        toast.show();
//                        break;
//                    case 1:
//                        Toast toast1 = Toast.makeText(getApplicationContext(),"The status of book was changed on reading",Toast.LENGTH_SHORT);
//                        toast1.show();
//                        break;
//                    case 2:
//                        Toast toast2 = Toast.makeText(getApplicationContext(),"The status of book was changed on will read",Toast.LENGTH_SHORT);
//                        toast2.show();
//                        break;
//                }
//            }
//        });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();

    }

    //нажатие на кнопку ChangeUsername
    public void onShowUser (final View view){

        //меняем юзернейм
        EditText newUsername = findViewById(R.id.editTextNewUsername);
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_]+");
        Matcher matcher = pattern.matcher(newUsername.getText().toString());

        if(matcher.matches()){
            Call<ResponseBody> callTest = RetrofitClient
                    .getInstance()
                    .getBookSpaceAPI()
                    .changeProfile("Bearer " + getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", ""),
                            newUsername.getText().toString(),
                            null);

            callTest.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(getApplicationContext(),"Your username was changed",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Connection failed",Toast.LENGTH_SHORT).show();
                }
            });

            //отображаем юзернейм и емейл
            final TextView profileUsername = findViewById(R.id.textViewProfileUsername);
            profileUsername.setText(newUsername.getText().toString());

        } else{
            newUsername.setError("Username can only contain latin letters, digits or underscores");
        }
    }
    //нажатие на кнопку ChangePassword
    public void onShowPassword(View view){

        //меняем пароль
        EditText newPass = findViewById(R.id.editTextNewPassword);
        EditText confirmNewPass = findViewById(R.id.editTextConfirmNewPassword);

        if(newPass.getText().length() < 6){
            newPass.setError("Password must be at least 6 characters long");
        } else if(!newPass.getText().toString().equals(confirmNewPass.getText().toString())){
            confirmNewPass.setError("Passwords aren't the same, try again");
        } else{
            Call<ResponseBody> callTest = RetrofitClient
                    .getInstance()
                    .getBookSpaceAPI()
                    .changeProfile("Bearer " + getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", ""),
                            null,
                            confirmNewPass.getText().toString());

            callTest.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(getApplicationContext(),"Your password was changed",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Connection failed",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //нажатие на кнопку SetReadingTargets
    public void onShowTarget(View view){

        int targetWeek = 0;
        int targetMonth = 0;
        int targetYear = 0;
        boolean validData = true;
        final EditText weekTarget = findViewById(R.id.editTextTargetForAWeek);
        final EditText monthTarget = findViewById(R.id.editTextTargetForAMonth);
        final EditText yearTarget = findViewById(R.id.editTextTargetForAYear);

        try{
            targetWeek = Integer.parseInt(weekTarget.getText().toString());
            if(targetWeek < 0){
                weekTarget.setError("This field must be a positive number");
                validData = false;
            }
        } catch(NumberFormatException ex){
            weekTarget.setError("This field must be a positive number");
            validData = false;
        }

        try{
            targetMonth = Integer.parseInt(monthTarget.getText().toString());
            if(targetMonth < 0){
                monthTarget.setError("This field must be a positive number");
                validData = false;
            }
        } catch(NumberFormatException ex){
            monthTarget.setError("This field must be a positive number");
            validData = false;
        }

        try{
            targetYear = Integer.parseInt(yearTarget.getText().toString());
            if(targetYear < 0){
                yearTarget.setError("This field must be a positive number");
                validData = false;
            }
        } catch(NumberFormatException ex){
            yearTarget.setError("This field must be a positive number");
            validData = false;
        }

        if(validData){
            Call<SetPlanResponse> setWeek = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .setPlan("Bearer " + getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", ""),
                        "week",
                        targetWeek,
                        null,
                        null);

        setWeek.enqueue(new Callback<SetPlanResponse>() {
            @Override
            public void onResponse(Call<SetPlanResponse> call, Response<SetPlanResponse> response) {

            }

            @Override
            public void onFailure(Call<SetPlanResponse> call, Throwable t) {

            }
        });
        
        Call<SetPlanResponse> setMonth = RetrofitClient
                    .getInstance()
                    .getBookSpaceAPI()
                    .setPlan("Bearer " + getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", ""),
                            "month",
                            null,
                            targetMonth,
                            null);

            setMonth.enqueue(new Callback<SetPlanResponse>() {
                @Override
                public void onResponse(Call<SetPlanResponse> call, Response<SetPlanResponse> response) {
                }

                @Override
                public void onFailure(Call<SetPlanResponse> call, Throwable t) {
                }
            });

            Call<SetPlanResponse> setYear = RetrofitClient
                    .getInstance()
                    .getBookSpaceAPI()
                    .setPlan("Bearer " + getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", ""),
                            "year",
                            null,
                            null,
                            targetYear);

            setYear.enqueue(new Callback<SetPlanResponse>() {
                @Override
                public void onResponse(Call<SetPlanResponse> call, Response<SetPlanResponse> response) {
                }

                @Override
                public void onFailure(Call<SetPlanResponse> call, Throwable t) {
                }
            });
            Toast.makeText(getApplicationContext(), "Your targets have been saved", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onClickLogOut(View v){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to Logout?")
                    .setCancelable(true).setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                        Call<ResponseBody> call6 = RetrofitClient
                                .getInstance()
                                .getBookSpaceAPI()
                                .logout("Bearer " + getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", ""));

        call6.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("token");
        editor.apply();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_page, menu);


        //отображаем юзернейм и емейл
        final TextView profileUsername = findViewById(R.id.textViewProfileUsername);
        final TextView profileEmail = findViewById(R.id.textViewProfileEmail);

        Call<ProfileResponse> getProfileInfo = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getProfileInfo("Bearer " + getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", ""));

        getProfileInfo.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                ProfileResponse resp = response.body();
                User user = resp.getUser();

                try{
                    profileUsername.setText(user.getUsername());
                    profileEmail.setText(user.getEmail());
                } catch(Exception ignore){}
            }
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void onSelectFragment(View view) {
        Fragment newFragment;
        if (view == findViewById(R.id.btnfr1)) {
            newFragment = new ChangeName();
        } else if (view == findViewById(R.id.btnfr2)) {
            newFragment = new ChangePassword();
        } else if (view == findViewById(R.id.btnfr3)) {
            newFragment = new SetTargets();
        } else {
            newFragment = new Empty();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ll2,newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void onSelectFragment1(View view) {
        Fragment newFragment;
        if (view == findViewById(R.id.btnfr4)) {
            newFragment = new TopFragment();
            findViewById(R.id.btnfr4).setBackgroundColor(Color.parseColor("#757575"));
            findViewById(R.id.btnfr5).setBackgroundColor(Color.parseColor("#bdbdbd"));


        } else if (view == findViewById(R.id.btnfr5)) {
            newFragment = new RecommendFragment();
            findViewById(R.id.btnfr5).setBackgroundColor(Color.parseColor("#757575"));
            findViewById(R.id.btnfr4).setBackgroundColor(Color.parseColor("#bdbdbd"));
        } else {
            newFragment = new TopFragment();
            findViewById(R.id.btnfr4).setBackgroundColor(Color.parseColor("#757575"));
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ll3, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onSelectFragment3(View view) {
        Fragment newFragment;
        if (view == findViewById(R.id.btnfr6)) {
            newFragment = new StatisticYear();
            findViewById(R.id.btnfr6).setBackgroundColor(Color.parseColor("#757575"));
            findViewById(R.id.btnfr7).setBackgroundColor(Color.parseColor("#bdbdbd"));
            findViewById(R.id.btnfr8).setBackgroundColor(Color.parseColor("#bdbdbd"));
        } else if (view == findViewById(R.id.btnfr7)) {
            newFragment = new StatisticMonth();
            findViewById(R.id.btnfr7).setBackgroundColor(Color.parseColor("#757575"));
            findViewById(R.id.btnfr6).setBackgroundColor(Color.parseColor("#bdbdbd"));
            findViewById(R.id.btnfr8).setBackgroundColor(Color.parseColor("#bdbdbd"));
        } else if (view == findViewById(R.id.btnfr8)) {
            newFragment = new StatisticWeek();
            findViewById(R.id.btnfr8).setBackgroundColor(Color.parseColor("#757575"));
            findViewById(R.id.btnfr6).setBackgroundColor(Color.parseColor("#bdbdbd"));
            findViewById(R.id.btnfr7).setBackgroundColor(Color.parseColor("#bdbdbd"));
        } else {
            newFragment = new StatisticYear();
            findViewById(R.id.btnfr6).setBackgroundColor(Color.parseColor("#757575"));
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ll1,newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            setTitle("BookSpace");
            TopRecommends2 topRecommends2 = new TopRecommends2();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragments, topRecommends2).commit();


            FragmentManager fom = getSupportFragmentManager();
            FragmentTransaction transaction = fom.beginTransaction();
            TopFragment startFragment = new TopFragment();
            transaction.add(R.id.ll3, startFragment);
            transaction.commit();

        } else if (id == R.id.nav_read) {
            setTitle("Read Books");
           ReadBooks readBooks = new ReadBooks();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragments, readBooks).commit();


        } else if (id == R.id.nav_reading) {
           setTitle("Reading Books");
           ReadingBooks readingBooks = new ReadingBooks();
           FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragments, readingBooks).commit();

        } else if (id == R.id.nav_willread) {
           setTitle("Will Read Books");
            WillReadBooks willreadBooks = new WillReadBooks();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragments, willreadBooks).commit();

        } else if (id == R.id.nav_settings) {
            setTitle("Account settings");
            Settings2 setting = new Settings2();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragments, setting).commit();

            FragmentManager fim = getSupportFragmentManager();
            FragmentTransaction transaction = fim.beginTransaction();
            Empty startFragment = new Empty();
            transaction.add(R.id.ll2,startFragment);
            transaction.commit();
        } else if (id == R.id.nav_statistic) {
            setTitle("Statistics");
            Statistics2 statistics2 = new Statistics2();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragments, statistics2).commit();

            FragmentManager fem = getSupportFragmentManager();
            FragmentTransaction transaction = fem.beginTransaction();
            StatisticYear startFragment = new StatisticYear();
            transaction.add(R.id.ll1,startFragment);
            transaction.commit();

        }
        else {
            setTitle("BookSpace");
            TopRecommends2 topRecommends2 = new TopRecommends2();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragments, topRecommends2).commit();

            FragmentManager fom = getSupportFragmentManager();
            FragmentTransaction transaction = fom.beginTransaction();
            TopFragment startFragment = new TopFragment();
            transaction.add(R.id.ll3, startFragment);
            transaction.commit();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}