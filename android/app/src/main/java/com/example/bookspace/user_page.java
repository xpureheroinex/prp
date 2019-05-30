package com.example.bookspace;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
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
import android.widget.Toast;

import com.example.bookspace.model.ProfileResponse;
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
        mReadTextView = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_gallery));
        initializeCountDrawer();


        //-------------------- test query

        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = prefs.getString("token", "token is null");
        final TextView text = findViewById(R.id.textView29);

        //сначала поменяем username

        Call<ResponseBody> callTest = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .changeProfile("Bearer " + token, "newUsername", null);

        callTest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        //---


        Call<ProfileResponse> call = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getProfileInfo("Bearer " + token);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                ProfileResponse resp = response.body();
                text.setText(resp.getUser().getUsername());

            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(user_page.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
            }
        });
        //-------------------------------

    }
    private void initializeCountDrawer(){
        mReadTextView.setGravity(Gravity.CENTER_VERTICAL);
        mReadTextView.setTypeface(null, Typeface.BOLD);
        mReadTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        mReadTextView.setText("4");
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
