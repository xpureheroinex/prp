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

public class StatisticWeek extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.statistic_week, container, false);

        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");
        final TextView statsWeekBooksRead = view.findViewById(R.id.statsWeekBooksRead);
        final TextView statsWeekFavGenre = view.findViewById(R.id.statsWeekFavGenre);
        final TextView statsWeekFavAuthor = view.findViewById(R.id.statsWeekFavAuthor);
        final TextView statsWeekWantToReadBooks = view.findViewById(R.id.statsWeekWantToReadBooks);
        final TextView statsWeekReadOutOf = view.findViewById(R.id.statsWeekReadOutOf);
        final TextView statsWeekCompleted = view.findViewById(R.id.statsWeekCompleted);
        final TextView statsWeekBooksOutOf = view.findViewById(R.id.statsWeekBooksOutOf);


        Call<StatisticsResponse> getWeekStats = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getStats("Bearer " + token, "week");

        getWeekStats.enqueue(new Callback<StatisticsResponse>() {
            @Override
            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
                StatisticsResponse resp = response.body();
                StatisticsData statsData = resp.getInfo();
                UserPlan userPlan = resp.getPlan();

                statsWeekBooksRead.setText(String.valueOf(statsData.getCount()));
                statsWeekFavGenre.setText(statsData.getFav_genre());
                statsWeekFavAuthor.setText(statsData.getFav_author());
                statsWeekWantToReadBooks.setText(String.valueOf(userPlan.getPlan()));
                statsWeekReadOutOf.setText(String.valueOf(userPlan.getCount()));
                statsWeekBooksOutOf.setText(String.valueOf(userPlan.getPlan()));

                if(userPlan.getPercent().equals("no info provided")){
                    statsWeekCompleted.setText("0");

                } else{
                    statsWeekCompleted.setText(userPlan.getPercent());

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