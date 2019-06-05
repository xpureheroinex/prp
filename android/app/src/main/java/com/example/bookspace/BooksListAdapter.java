package com.example.bookspace;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
        View row = View.inflate(mContext,R.layout.row, null);

        TextView myTitle = row.findViewById(R.id.statusBookTitle);
        TextView myRate = row.findViewById(R.id.statusBookRate);
        TextView myGenre = row.findViewById(R.id.statusBookGenre);
        TextView myAuthor = row.findViewById(R.id.statusBookAuthor);

        myTitle.setText(mBooksList.get(position).getTitle());
       // myRate.setText(String.valueOf(mBooksList.get(position).getRate()));
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
        return row;
    }
}
