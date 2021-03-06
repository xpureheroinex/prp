package com.example.bookspace.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.R;
import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.books.SearchBook;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchBookAdapter extends BaseAdapter {
    private Context mContextTop;
    private List<SearchBook> mBooksListTop;
    ImageButton addButton;

    public SearchBookAdapter(Context mContext, List<SearchBook> mBooksList){
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
        View row2 = View.inflate(mContextTop, R.layout.row2, null);

        TextView myTitle = row2.findViewById(R.id.topBookTitle);
        TextView myAuthor = row2.findViewById(R.id.topBookAuthor);
        TextView myGenre = row2.findViewById(R.id.topBookGenre);


        myTitle.setText(mBooksListTop.get(position).getTitle());
        myAuthor.setText(mBooksListTop.get(position).getAuthor());
        myGenre.setText(mBooksListTop.get(position).getGenre());

        addButton = row2.findViewById(R.id.addbtn1);
        addButton.setTag(mBooksListTop.get(position).getId());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                SharedPreferences prefs = v.getContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
                final String token = prefs.getString("token", "");

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.bookChoose);

                final Resources res = mContextTop.getResources();

                String[] items= {res.getString(R.string.bookRead), res.getString(R.string.bookReading),
                        res.getString(R.string.bookWillRead)};
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
                                        Toast toast = Toast.makeText(v.getContext(),res.getString(R.string.statusToRead),Toast.LENGTH_SHORT);
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
                                        Toast toast = Toast.makeText(v.getContext(),res.getString(R.string.statusToReading),Toast.LENGTH_SHORT);
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
                                        Toast toast = Toast.makeText(v.getContext(),res.getString(R.string.statusToWillRead),Toast.LENGTH_SHORT);
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

        row2.setTag(mBooksListTop.get(position).getId());
        return row2;
    }

    @Override
    public boolean isEnabled(int position){
        return true;
    }
}