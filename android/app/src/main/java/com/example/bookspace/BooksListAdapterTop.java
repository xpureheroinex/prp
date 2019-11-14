package com.example.bookspace;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.MainPageBook;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class BooksListAdapterTop extends BaseAdapter{
    private Context mContextTop;
    private List<MainPageBook> mBooksListTop;
    ImageButton addButton;

    public BooksListAdapterTop(Context mContext, List<MainPageBook> mBooksList){
        this.mContextTop = mContext;
        this.mBooksListTop = mBooksList;
    }
    @Override
    public int getCount() {
        return mBooksListTop.size();
    }

    @Override
    public Object getItem(int position) {
        return mBooksListTop.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row1 = View.inflate(mContextTop, R.layout.row1, null);
        
        TextView myTitle = row1.findViewById(R.id.topBookTitle);
        TextView myRate = row1.findViewById(R.id.topBookRate);
        TextView myAuthor = row1.findViewById(R.id.topBookAuthor);
        TextView myGenre = row1.findViewById(R.id.topBookGenre);

        myTitle.setText(mBooksListTop.get(position).getTitle());
        myRate.setText(String.valueOf(mBooksListTop.get(position).getRate()));

        //set relevant rating color
        double rateValue = mBooksListTop.get(position).getRate();
        if(rateValue >= 0 && rateValue < 3){
            myRate.setTextColor(Color.parseColor("#D50000"));
        }
        else if(rateValue >= 3 && rateValue < 4){
            myRate.setTextColor(Color.parseColor("#ffd600"));
        }
        else if(rateValue >= 4 && rateValue <= 5){
            myRate.setTextColor(Color.parseColor("#1faa00"));
        }

        myAuthor.setText(mBooksListTop.get(position).getAuthor());
        myGenre.setText(mBooksListTop.get(position).getGenre());
        addButton = row1.findViewById(R.id.addbtn1);
        addButton.setTag(mBooksListTop.get(position).getId());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                SharedPreferences prefs = v.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE);
                final String token = prefs.getString("token", "");

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Choose status:");

                String[] items= {"Read", "Reading", "Will Read"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:

                                Call<ResponseBody> addBook = RetrofitClient
                                        .getInstance()
                                        .getBookSpaceAPI()
                                        .addBook("Bearer " + token, mBooksListTop.get(position).getId(), "DN");

                                addBook.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast toast = Toast.makeText(v.getContext(),"The status of book was changed on read",Toast.LENGTH_SHORT);
                                        toast.show();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                                break;
                            case 1:
                                Call<ResponseBody> addBook2 = RetrofitClient
                                        .getInstance()
                                        .getBookSpaceAPI()
                                        .addBook("Bearer " + token, mBooksListTop.get(position).getId(), "IP");

                                addBook2.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast toast = Toast.makeText(v.getContext(),"The status of book was changed on reading",Toast.LENGTH_SHORT);
                                        toast.show();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                                break;
                            case 2:
                                Call<ResponseBody> addBook3 = RetrofitClient
                                        .getInstance()
                                        .getBookSpaceAPI()
                                        .addBook("Bearer " + token, mBooksListTop.get(position).getId(), "WR");

                                addBook3.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast toast = Toast.makeText(v.getContext(),"The status of book was changed on will read",Toast.LENGTH_SHORT);
                                        toast.show();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                                break;
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        row1.setTag(mBooksListTop.get(position).getId());
        return row1;
    }

    @Override
    public boolean isEnabled(int position){
        return true;
    }
}