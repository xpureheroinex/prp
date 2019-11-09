package com.example.bookspace;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.example.bookspace.bookLists.ReadBooks;
import com.example.bookspace.bookLists.ReadingBooks;
import com.example.bookspace.bookLists.WillReadBooks;
import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.profile.ImageResponse;
import com.example.bookspace.model.profile.ProfileResponse;
import com.example.bookspace.model.profile.User;
import com.example.bookspace.model.statistics.SetPlanResponse;
import com.example.bookspace.registration.LoginActivity;
import com.example.bookspace.settings.ChangeAvatar;
import com.example.bookspace.settings.ChangeName;
import com.example.bookspace.settings.ChangePassword;
import com.example.bookspace.settings.Settings2;
import com.example.bookspace.statistics.SetTargets;
import com.example.bookspace.statistics.StatisticMonth;
import com.example.bookspace.statistics.StatisticWeek;
import com.example.bookspace.statistics.StatisticYear;
import com.example.bookspace.statistics.Statistics2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class user_page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView mReadTextView;
    private static final int RESULT_LOAD_IMAGE = 1;
    public Bitmap bmapAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mReadTextView = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_read));
        initializeCountDrawer();
        mReadTextView = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_reading));
        initializeCountDrawer1();
        mReadTextView = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_willread));
        initializeCountDrawer2();

        setTitle("BookSpace");
        // ----------------------------------------------------------------------

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragments, new TopRecommends2()).commit();
        fm.beginTransaction().replace(R.id.fragmentsTopRecsContainer, new TopFragment()).commit();

        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = prefs.getString("token", "token is null");
    }

    public void onClickUpload(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case RESULT_LOAD_IMAGE:
            if(resultCode == RESULT_OK){
                Uri selectedImage = data.getData();
                try {
                    bmapAvatar = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),"Image was chosen",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //region initialize count drawers
    private void initializeCountDrawer(){
        mReadTextView.setGravity(Gravity.CENTER_VERTICAL);
        mReadTextView.setTypeface(null, Typeface.BOLD);
        mReadTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }
    private void initializeCountDrawer1(){
        mReadTextView.setGravity(Gravity.CENTER_VERTICAL);
        mReadTextView.setTypeface(null, Typeface.BOLD);
        mReadTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }
    private void initializeCountDrawer2(){
        mReadTextView.setGravity(Gravity.CENTER_VERTICAL);
        mReadTextView.setTypeface(null, Typeface.BOLD);
        mReadTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }
    //endregion

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

            //show username
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

    public void onShowAvatar (final View view){

        //change user photo
        if(bmapAvatar == null){
            Toast.makeText(getApplicationContext(),"You did not choose user image",Toast.LENGTH_SHORT).show();
        }
        else {
            String strBase64 = encodeToBase64(bmapAvatar, Bitmap.CompressFormat.JPEG, 100);
            String result = "data:image/png;base64," + strBase64;

            Call<ResponseBody> callTest = RetrofitClient
                    .getInstance()
                    .getBookSpaceAPI()
                    .changeImage("Bearer " + getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", ""),
                            result);

            callTest.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(getApplicationContext(), "Your avatar was saved", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                }
            });

            //display changed avatar
            final ImageView profilePhoto = findViewById(R.id.imageViewAvatar);
            Bitmap imageBitmap = Bitmap.createScaledBitmap(bmapAvatar, 80, 80, false);
            imageBitmap = getCircledBitmap(imageBitmap);
            profilePhoto.setImageBitmap(imageBitmap);
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

    public static Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public void callSearch(String query){
        SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        preferences.edit().putString("query", query).apply();
        Fragment searchFragment = new SearchFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentsTopRecsContainer, searchFragment).commit();
        findViewById(R.id.topButton).setBackgroundColor(Color.parseColor("#bdbdbd"));
        findViewById(R.id.recommendationsButton).setBackgroundColor(Color.parseColor("#bdbdbd"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_page, menu);

        //region display username and email
        final TextView profileUsername = findViewById(R.id.textViewProfileUsername);
        final TextView profileEmail = findViewById(R.id.textViewProfileEmail);
        final TextView profileRole = findViewById(R.id.textViewProfileRole);

        //searchable configuration
        SearchManager sManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView sView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sView.setSearchableInfo(sManager.getSearchableInfo(getComponentName()));
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextChange(String newText){
                if(!newText.equals("")) {
                    callSearch(newText);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query){
                if(!query.equals("")){
                    callSearch(query);
                }
                sView.setQuery("", false);
                sView.setIconified(true);
                sView.clearFocus();
                return true;
            }
        });

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
                    profileRole.setText(String.format("%s%s", user.getRole().substring(0, 1).toUpperCase(), user.getRole().substring(1)));

                } catch(Exception ignore){}
            }
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        //endregion

        //region display user photo
        final ImageView profilePhoto = findViewById(R.id.imageViewAvatar);

        Call<ImageResponse> getProfileImage = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getProfileImage("Bearer " + getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", ""));

        getProfileImage.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                try{
                    ImageResponse resp = response.body();
                    String image = resp.getImage();
                    Bitmap myBitmap = decodeBase64(image.replace("data:image/png;base64,", ""));
                    myBitmap = getCircledBitmap(Bitmap.createScaledBitmap(myBitmap, 80, 80, false));
                    profilePhoto.setImageBitmap(myBitmap);
                }
                catch(Exception ignored) { }
            }
            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        //endregion

        return true;
    }

    //region image encoding
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality){
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input){
        byte[] decodeBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.length);
    }
    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        } else if (view == findViewById(R.id.btnfr9)) {
            newFragment = new ChangeAvatar();
        } else {
            newFragment = new Empty();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ll2,newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void onClickTopButton(View view){
        Fragment topFragment = new TopFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentsTopRecsContainer, topFragment).commit();
        view.setBackgroundColor(Color.parseColor("#757575"));
        findViewById(R.id.recommendationsButton).setBackgroundColor(Color.parseColor("#bdbdbd"));

    }

    public void onClickRecommendationsButton(View view){
        Fragment recsFragment = new RecommendFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentsTopRecsContainer, recsFragment).commit();
        view.setBackgroundColor(Color.parseColor("#757575"));
        findViewById(R.id.topButton).setBackgroundColor(Color.parseColor("#bdbdbd"));
    }

    //statistics
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        SearchView sView = (SearchView) findViewById(R.id.app_bar_search);
        sView.setQuery("", false);
        sView.setIconified(true);
        sView.clearFocus();


        if (id == R.id.nav_home) {
            setTitle("BookSpace");
            TopRecommends2 topRecommends2 = new TopRecommends2();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragments, topRecommends2).commit();


            FragmentManager fom = getSupportFragmentManager();
            FragmentTransaction transaction = fom.beginTransaction();
            TopFragment startFragment = new TopFragment();
            transaction.add(R.id.fragmentsTopRecsContainer, startFragment);
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
            transaction.add(R.id.fragmentsTopRecsContainer, startFragment);
            transaction.commit();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}