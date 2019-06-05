package com.example.bookspace;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.statistics.StatisticsData;
import com.example.bookspace.model.statistics.StatisticsResponse;
import com.example.bookspace.model.statistics.UserPlan;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class StatisticYear extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistic_year, container, false);

        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");
        final TextView statsYearBooksRead = view.findViewById(R.id.statsYearBooksRead);
        final TextView statsYearFavGenre = view.findViewById(R.id.statsYearFavGenre);
        final TextView statsYearFavAuthor = view.findViewById(R.id.statsYearFavAuthor);
        final TextView statsYearWantToReadBooks = view.findViewById(R.id.statsYearWantToReadBooks);
        final TextView statsYearReadOutOf = view.findViewById(R.id.statsYearReadOutOf);
        final TextView statsYearCompleted = view.findViewById(R.id.statsYearCompleted);
        final TextView statsYearBooksOutOf = view.findViewById(R.id.statsYearBooksOutOf);


        Call<StatisticsResponse> getYearStats = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getStats("Bearer " + token, "year");

        getYearStats.enqueue(new Callback<StatisticsResponse>() {
            @Override
            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
                StatisticsResponse resp = response.body();
                StatisticsData statsData = resp.getInfo();
                UserPlan userPlan = resp.getPlan();

                statsYearBooksRead.setText(String.valueOf(statsData.getCount()));
                statsYearFavGenre.setText(statsData.getFav_genre());
                statsYearFavAuthor.setText(statsData.getFav_author());
                statsYearWantToReadBooks.setText(String.valueOf(userPlan.getPlan()));
                statsYearReadOutOf.setText(String.valueOf(userPlan.getCount()));
                statsYearBooksOutOf.setText(String.valueOf(userPlan.getPlan()));

                if(userPlan.getPercent().equals("no info provided")){
                    statsYearCompleted.setText("0");

                } else{
                    statsYearCompleted.setText(userPlan.getPercent());
                }
            }

            @Override
            public void onFailure(Call<StatisticsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return view;
    }
}