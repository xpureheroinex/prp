package com.example.bookspace.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.R;
import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.UserBook;

import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class BooksListAdapter extends BaseAdapter {

    private Context mContext;
    private List<UserBook> mBooksList;
    ImageButton deletebtn;
    ImageButton changeStatus;

    public BooksListAdapter(Context mContext,List<UserBook> mBooksList){
        this.mContext = mContext;
        this.mBooksList = mBooksList;
    }
    @Override
    public int getCount() {
        return mBooksList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBooksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = View.inflate(mContext, R.layout.row, null);

        TextView myTitle = row.findViewById(R.id.statusBookTitle);
        TextView myRate = row.findViewById(R.id.statusBookRate);
        TextView myGenre = row.findViewById(R.id.statusBookGenre);
        TextView myAuthor = row.findViewById(R.id.statusBookAuthor);

        myTitle.setText(mBooksList.get(position).getTitle());
        myRate.setText(String.valueOf(mBooksList.get(position).getRate()));

//        set relevant rating color
        double rateValue = mBooksList.get(position).getRate();
        if(rateValue >= 0 && rateValue < 3){
            myRate.setTextColor(Color.parseColor("#D50000"));
        }
        else if(rateValue >= 3 && rateValue < 4){
            myRate.setTextColor(Color.parseColor("#ffd600"));
        }
        else if(rateValue >= 4 && rateValue <= 5){
            myRate.setTextColor(Color.parseColor("#1faa00"));
        }

        myGenre.setText(mBooksList.get(position).getGenre());
        myAuthor.setText(mBooksList.get(position).getAuthor());

        deletebtn = row.findViewById(R.id.deletebtn);
        deletebtn.setTag(mBooksList.get(position).getId());
        deletebtn.setOnClickListener(new View.OnClickListener() {
          @Override
        public void onClick(View v) {
                SharedPreferences prefs = v.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE);
                String token = prefs.getString("token", "");

                Call<ResponseBody> deleteBook = RetrofitClient
                        .getInstance()
                        .getBookSpaceAPI()
                        .deleteBook("Bearer " + token, mBooksList.get(position).getId());

                deleteBook.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(mContext, "Book has been deleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

              mBooksList.remove(mBooksList.get(position));
              notifyDataSetChanged();

          }
        });

        changeStatus = row.findViewById(R.id.addbtn);
        changeStatus.setTag(mBooksList.get(position).getId());

        changeStatus.setOnClickListener(new View.OnClickListener() {
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
                                        .addBook("Bearer " + token, mBooksList.get(position).getId(), "DN");

                                addBook.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast toast = Toast.makeText(v.getContext(),"The status of book was changed on read",Toast.LENGTH_SHORT);
                                        toast.show();

                                        mBooksList.remove(mBooksList.get(position));
                                        notifyDataSetChanged();
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
                                        .addBook("Bearer " + token, mBooksList.get(position).getId(), "IP");

                                addBook2.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast toast = Toast.makeText(v.getContext(),"The status of book was changed on reading",Toast.LENGTH_SHORT);
                                        toast.show();
                                        mBooksList.remove(mBooksList.get(position));
                                        notifyDataSetChanged();

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
                                        .addBook("Bearer " + token, mBooksList.get(position).getId(), "WR");

                                addBook3.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast toast = Toast.makeText(v.getContext(),"The status of book was changed on will read",Toast.LENGTH_SHORT);
                                        toast.show();

                                        mBooksList.remove(mBooksList.get(position));
                                        notifyDataSetChanged();
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
        return row;
    }

}
