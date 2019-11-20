package com.example.bookspace.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.content.SharedPreferences;


import com.example.bookspace.R;
import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.profile.ProfileResponse;
import com.example.bookspace.model.profile.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class SetTargetsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settarget, container, false);

        final EditText weekTarget = view.findViewById(R.id.editTextTargetForAWeek);
        final EditText monthTarget = view.findViewById(R.id.editTextTargetForAMonth);
        final EditText yearTarget = view.findViewById(R.id.editTextTargetForAYear);
        final String token = this.getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "");
        Call<ProfileResponse> getProfileInfo = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getProfileInfo("Bearer " + token);

        getProfileInfo.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                ProfileResponse resp = response.body();
                User user = resp.getUser();

                weekTarget.setText(user.getWeek().toString());
                monthTarget.setText(user.getMonth().toString());
                yearTarget.setText(user.getYear().toString());

            }
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return view;
    }
}
