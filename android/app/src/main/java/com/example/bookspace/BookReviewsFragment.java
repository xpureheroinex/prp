package com.example.bookspace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bookspace.adapters.ReviewsAdapter;
import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.reviews.GetReviewsResponse;
import com.example.bookspace.model.reviews.Review;
import java.util.ArrayList;
import java.util.List;
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
    boolean canWrite = true;


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
                    if(!response.body().getReview()){
                        canWrite = false;

                    }

                        reviewsClassesList = new ArrayList<>();
                        for (i = 0; i < info.length; i++) {
                            reviewsClassesList.add(new ReviewsClass(i, info[i].getUsername(),
                                    info[i].getText(), info[i].getCreated()));
                        }

                        booksListView = view.findViewById(R.id.listReviews);
                        reviewsAdapter = new ReviewsAdapter(getContext(), reviewsClassesList);
                        booksListView.setAdapter(reviewsAdapter);

                }

                @Override
                public void onFailure(Call<GetReviewsResponse> call, Throwable t) {
                    Toast.makeText(getContext(), getText(R.string.wrongRes), Toast.LENGTH_LONG).show();
                }
            });

        }

        Button openAddReviewPage = view.findViewById(R.id.buttonOpenAddReviewPage);
        openAddReviewPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(canWrite){
                    AddReviewFragment addReviewFragment = new AddReviewFragment();
                    addReviewFragment.setArguments(getArguments());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.bookPageFragmentContainer, addReviewFragment).commit();
                }
                else{
                    Toast.makeText(getContext(), "You have already written a review on this book", Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }
}
