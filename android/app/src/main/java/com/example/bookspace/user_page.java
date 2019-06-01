package com.example.bookspace;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;

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

        setTitle("BookSpace");
        TopRecommends2 topRecommends2 = new TopRecommends2();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragments, topRecommends2).commit();

        FragmentManager fom = getSupportFragmentManager();
        FragmentTransaction transaction = fom.beginTransaction();
        TopFragment startFragment = new TopFragment();
        transaction.add(R.id.ll3, startFragment);
        transaction.commit();

        final TextView text = findViewById(R.id.textView29);
        final TextView text2 = findViewById(R.id.textView30);
        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = prefs.getString("token", "token is null");


        //-------------------- test queries


        //меняем юзернейм

//        Call<ResponseBody> callTest = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .changeProfile("Bearer " + token, "newUsername", null);
//
//        callTest.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });


        //получаем инфо о юзере
//        Call<ProfileResponse> call = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .getProfileInfo("Bearer " + token);
//
//        call.enqueue(new Callback<ProfileResponse>() {
//            @Override
//            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
//                ProfileResponse resp = response.body();
//                text.setText(String.valueOf(resp.getStatus()));
//
//            }
//
//            @Override
//            public void onFailure(Call<ProfileResponse> call, Throwable t) {
//                Toast.makeText(user_page.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
//            }
//        });

        //получаем статистику

//        Call<StatisticsResponse> call3 = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .getStats("Bearer " + token, "week");
//
//        call3.enqueue(new Callback<StatisticsResponse>() {
//            @Override
//            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
//                StatisticsResponse resp = response.body();
//                text2.setText(String.valueOf(resp.getPlan().getPlan()));
//
//
//
//
//
//            }
//
//            @Override
//            public void onFailure(Call<StatisticsResponse> call, Throwable t) {
//                if (t instanceof IOException) {
//                    Toast.makeText(user_page.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
//                    // logging probably not necessary
//                }
//                else {
//                    Toast.makeText(user_page.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
//                    // todo log to some central bug tracking service
//                    t.printStackTrace();
//                }
//
//            }
//        });


//        //устанавливаем план
//
//        Call<SetPlanResponse> call4 = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .setPlan("Bearer " + token, "week", 10, null, null);
//
//        call4.enqueue(new Callback<SetPlanResponse>() {
//            @Override
//            public void onResponse(Call<SetPlanResponse> call, Response<SetPlanResponse> response) {
//                SetPlanResponse resp = response.body();
//                text2.setText(String.valueOf(resp.getStatus()));
//            }
//
//            @Override
//            public void onFailure(Call<SetPlanResponse> call, Throwable t) {
//
//            }
//        });


        //restore pass
//        Call<ResponseBody> call6 = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .restorePassword("al.skoruk@gmail.com");
//
//        call6.enqueue(new Callback<ResponseBody>() {
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

        Call<ResponseBody> call8 = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .setRate("Bearer " + token, 10, 5);

        call8.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Toast.makeText(user_page.this, "rate = 5", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }
    private void initializeCountDrawer(){
       mReadTextView.setGravity(Gravity.CENTER_VERTICAL);
       mReadTextView.setTypeface(null, Typeface.BOLD);
        mReadTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        mReadTextView.setText("4");
    }

    public void onShowUser (View view){
        Toast toast = Toast.makeText(getApplicationContext(),"Your user name was changed",Toast.LENGTH_SHORT);
        toast.show();
    }
    public void onShowPassword(View view){
        Toast toast = Toast.makeText(getApplicationContext(),"Your password name was changed",Toast.LENGTH_SHORT);
        toast.show();
    }
    public void onShowTarget(View view){
        Toast toast = Toast.makeText(getApplicationContext(),"Your targets name was changed",Toast.LENGTH_SHORT);
        toast.show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_page, menu);
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

    public void onSelectFragment3(View view) {
        Fragment newFragment;
        if (view == findViewById(R.id.btnfr6)) {
            newFragment = new StatisticYear();
        } else if (view == findViewById(R.id.btnfr7)) {
            newFragment = new StatisticMonth();
        } else if (view == findViewById(R.id.btnfr8)) {
            newFragment = new StatisticWeek();
        } else {
            newFragment = new StatisticYear();
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
