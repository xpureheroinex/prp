package com.example.bookspace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.adapters.ReviewsAdapter;
import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.reviews.GetReviewsResponse;
import com.example.bookspace.model.reviews.Review;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class BookReviewsFragment extends Fragment {
    public int bookId;
    public int i;
    ListView booksListView;
    ReviewsAdapter reviewsAdapter;
    List<ReviewsClass> reviewsClassesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_book_reviews, container, false);
        final String token = getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE)
                .getString("token", "token is null");

        if(getArguments() != null){
            bookId = getArguments().getInt("bookId");

            Call<GetReviewsResponse> call = RetrofitClient
                    .getInstance()
                    .getBookSpaceAPI()
                    .getReviews("Bearer " + token, bookId);

            call.enqueue(new Callback<GetReviewsResponse>() {
                @Override
                public void onResponse(Call<GetReviewsResponse> call, Response<GetReviewsResponse> response) {
                    Review[] info = response.body().getInfo();
                    if (info == null) {
//                    TextView txtView = view.findViewById(R.id.textNoReview);
//                    txtView.setText("No reviews about this book,\n" +
//                            "you can be the first");
//                    txtView.setVisibility(View.VISIBLE);
                    } else {
                        reviewsClassesList = new ArrayList<>();
                        for (i = 0; i < info.length; i++) {
                            reviewsClassesList.add(new ReviewsClass(i, info[i].getUsername(),
                                    "\n" + info[i].getText(), info[i].getCreated()));
                        }

                        booksListView = view.findViewById(R.id.listReviews);
                        reviewsAdapter = new ReviewsAdapter(getContext(), reviewsClassesList);
                        booksListView.setAdapter(reviewsAdapter);

//                    booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            //maybe new event for reviews
//                        }
//                    });
                    }
                }

                @Override
                public void onFailure(Call<GetReviewsResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Something went wrong, try again", Toast.LENGTH_LONG).show();
                }
            });

        }



        return view;
    }

//    public void onClickAddReview(View v) {
//        EditText editaddreview = getView().findViewById(R.id.editAddReview);
//
//        if(editaddreview.getText().toString().matches("")){
//            Toast.makeText(getContext(), "You must fill the review field", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        Call<ResponseBody> call7 = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .postReview("Bearer " + token, bookId, editaddreview.getText().toString());
//
//        call7.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(AddReviewActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        final RatingBar rating = findViewById(R.id.ratingBarOnAddReview);
//
//        Call<ResponseBody> call8 = RetrofitClient
//                .getInstance()
//                .getBookSpaceAPI()
//                .setRate("Bearer " + token, bookId, Math.round(rating.getRating()));
//
//        call8.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(AddReviewActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
//            }
//        });
//        hideKeyboard();
//        Intent inten = new Intent(getApplicationContext(), ReviewsActivity.class);
//        inten.putExtra("bookId", bookId);
//        startActivity(inten);
//    }

}
