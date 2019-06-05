package com.example.bookspace;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.SearchView;

import android.widget.TextView;

import com.example.bookspace.model.books.MainPageBook;
import com.example.bookspace.model.books.UserBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Search extends AppCompatActivity {
   // ListView lvBooks;
  //  BooksListAdapter2 adapter;
    List<MainPageBook> mBooksList = new ArrayList<MainPageBook>();



   // ArrayList<String> listItems;
    BooksListAdapter2 adapter;
    ListView lvBooks;
    int [] id;
    String[]Title;
    String[]Author;
    String[]Genre;
    double[] rate;
    //ArrayList<MainPageBook> mBooksList = new ArrayList<MainPageBook>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);


        lvBooks = findViewById(R.id.listview);


         // mBooksList = new ArrayList<>();
       // mBooksList.add(new MainPageBook(1,"book1","author1","ppp",4.5));
       // mBooksList.add(new MainPageBook(2,"aaa","aaa","wqe",4.5));
       // mBooksList.add(new MainPageBook(3,"bbb","xx1","de",4.5));
       // mBooksList.add(new MainPageBook(4,"ccc","zzz","hhh",4.5));
       // mBooksList.add(new MainPageBook(5,"sss","ppp","iii",4.5));

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
       toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               NavUtils.navigateUpFromSameTask(Search.this);
           }
        });

       // adapter = new BooksListAdapter2(getApplicationContext(),mBooksList);
//        lvBooks.setAdapter(adapter);
        id = new int[]{1,2,3,4};
        Title = new String[]{"qqq","RRR","yyy","vvv"};
        Author = new String[]{"ddd","wer","dd","uuu"};
        Genre = new String[]{"lll","zzz","ooo","rtr"};
        rate = new double[]{1.2,1.3,5.0,3.6};

        for(int i = 0; i < id.length;i++){
            MainPageBook book = new MainPageBook(id[i],Title[i],Author[i],Genre[i],rate[i]);
            mBooksList.add(book);
        }

        adapter = new BooksListAdapter2(this,mBooksList);
        lvBooks.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forsearch,menu);

        MenuItem menuItem = menu.findItem(R.id.app_bar_search2);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(TextUtils.isEmpty(s)){
                    adapter.filter("");
                    lvBooks.clearTextFilter();
                }
                else {
                    adapter.filter(s);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
