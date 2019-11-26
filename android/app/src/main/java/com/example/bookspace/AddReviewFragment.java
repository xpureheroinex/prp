package com.example.bookspace;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import com.example.bookspace.model.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.content.Context.MODE_PRIVATE;

public class AddReviewFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_review, container, false);

        Button addReview = view.findViewById(R.id.buttonAddReview);
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddReview();
                //TODO: make a resource string
                Toast.makeText(getContext(), "Your review has been added", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                BookReviewsFragment bookReviewsFragment = new BookReviewsFragment();
                bookReviewsFragment.setArguments(getArguments());
                fragmentManager.beginTransaction().replace(R.id.bookPageFragmentContainer, bookReviewsFragment).commit();
            }
        });
        return view;
    }


    public void onClickAddReview() {
        EditText editaddreview = getActivity().findViewById(R.id.editAddReview);
        final String token = getActivity().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "token is null");
        final int bookId = getArguments().getInt("bookId");

        if(editaddreview.getText().toString().matches("")){
            Toast.makeText(getContext(), getText(R.string.fillField), Toast.LENGTH_LONG).show();
            return;
        }

        Call<ResponseBody> call7 = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .postReview("Bearer " + token, bookId, editaddreview.getText().toString());

        call7.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), getText(R.string.wrongRes), Toast.LENGTH_LONG).show();
            }
        });

        final RatingBar rating = getActivity().findViewById(R.id.ratingBarOnAddReview);

        Call<ResponseBody> call8 = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .setRate("Bearer " + token, bookId, Math.round(rating.getRating()));

        call8.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), getText(R.string.wrongRes), Toast.LENGTH_LONG).show();
            }
        });
    }
}
