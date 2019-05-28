package com.example.bookspace.api;

import com.example.bookspace.model.CreateUserResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BookSpaceAPI {

    @FormUrlEncoded
    @POST("/register")
    public Call<CreateUserResponse> createUser(
            @Field("email") String email,
            @Field("password") String password
    );
}
