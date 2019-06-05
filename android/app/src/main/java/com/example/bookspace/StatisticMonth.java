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

public class StatisticMonth extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistic_month, container, false);

        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");
        final TextView statsMonthBooksRead = view.findViewById(R.id.statsMonthBooksRead);
        final TextView statsMonthFavGenre = view.findViewById(R.id.statsMonthFavGenre);
        final TextView statsMonthFavAuthor = view.findViewById(R.id.statsMonthFavAuthor);
        final TextView statsMonthWantToReadBooks = view.findViewById(R.id.statsMonthWantToReadBooks);
        final TextView statsMonthReadOutOf = view.findViewById(R.id.statsMonthReadOutOf);
        final TextView statsMonthCompleted = view.findViewById(R.id.statsMonthCompleted);
        final TextView statsMonthBooksOutOf = view.findViewById(R.id.statsMonthBooksOutOf);


        Call<StatisticsResponse> getMonthStats = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getStats("Bearer " + token, "month");

        getMonthStats.enqueue(new Callback<StatisticsResponse>() {
            @Override
            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
                StatisticsResponse resp = response.body();
                StatisticsData statsData = resp.getInfo();
                UserPlan userPlan = resp.getPlan();

                statsMonthBooksRead.setText(String.valueOf(statsData.getCount()));
                statsMonthFavGenre.setText(statsData.getFav_genre());
                statsMonthFavAuthor.setText(statsData.getFav_author());
                statsMonthWantToReadBooks.setText(String.valueOf(userPlan.getPlan()));
                statsMonthReadOutOf.setText(String.valueOf(userPlan.getCount()));
                statsMonthBooksOutOf.setText(String.valueOf(userPlan.getPlan()));

                if(userPlan.getPercent().equals("no info provided")){
                    statsMonthCompleted.setText("0");

                } else{
                    statsMonthCompleted.setText(userPlan.getPercent());

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
