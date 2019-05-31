package com.example.bookspace.api;

import com.example.bookspace.model.CreateUserResponse;
import com.example.bookspace.model.LoginResponse;
import com.example.bookspace.model.ProfileResponse;
import com.example.bookspace.model.SetPlanResponse;
import com.example.bookspace.model.StatisticsResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

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

    //профиль
    //получаем информацию о пользователе
    @GET("/profile")
    Call<ProfileResponse> getProfileInfo(@Header("Authorization") String token);
    //запрос для изменения юзернейма или пароля
    @PUT("/profile")
    Call<ResponseBody> changeProfile(@Header("Authorization") String token,
                                     @Query("username") String username,
                                     @Query("password") String password);

    //запрос для получения статистики пользователя
    //при вызове запроса, в range можно передавать "week", "month" или "year"
    @GET("/stats")
    Call<StatisticsResponse> getStats(@Header("Authorization") String token,
                                      @Query("range") String range);
    //запрос для установки плана
    //при вызове запроса, в range можно передавать "week", "month" или "year"
    //соответственно значению range, нужно передать параметр
    //если range = week, передаём например week = 10 (план книг на неделю)
    //в остальных параметрах передаём null
    @PUT("/stats")
    Call<SetPlanResponse> setPlan(@Header("Authorization") String token,
                                  @Query("range") String range,
                                  @Query("week") Integer week,
                                  @Query("month") Integer month,
                                  @Query("year") Integer year);
}
