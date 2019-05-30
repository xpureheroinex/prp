package com.example.bookspace.api;

import com.example.bookspace.model.CreateUserResponse;
import com.example.bookspace.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BookSpaceAPI {

    //регистрация
    @FormUrlEncoded
    @POST("/register")
    Call<CreateUserResponse> createUser(
            @Field("email") String email,
            @Field("password") String password
    );

    //логин
    @FormUrlEncoded
    @POST("/login")
    Call<LoginResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );


}
