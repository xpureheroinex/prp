package com.example.bookspace.api;

import com.example.bookspace.model.books.GetBookResponse;
import com.example.bookspace.model.notes.GetNotesResponse;
import com.example.bookspace.model.registration.CreateUserResponse;
import com.example.bookspace.model.login.LoginResponse;
import com.example.bookspace.model.profile.ProfileResponse;
import com.example.bookspace.model.reviews.GetReviewsResponse;
import com.example.bookspace.model.statistics.SetPlanResponse;
import com.example.bookspace.model.statistics.StatisticsResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookSpaceAPI {

    //регистрация
    @FormUrlEncoded
    @POST("/register")
    Call<CreateUserResponse> createUser(
            @Field("email") String email,
            @Field("password") String password
    );

    //googlereg
    @FormUrlEncoded
    @POST("/google/register")
    Call<CreateUserResponse> createUserGoogle(
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

    //googleauth
    @FormUrlEncoded
    @POST("google/login")
    Call<LoginResponse> loginUserGoogle(
            @Field("email") String email
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


    //логаут
    @POST("/logout")
    Call<ResponseBody> logout(@Header("Authorization") String token);

    //восстановление пароля
    @FormUrlEncoded
    @POST("/login/restore")
    Call<ResponseBody> restorePassword(@Field("email") String email);

    //запросы для книг
    //получение информации о конкретной книге
    //todo поменять rate в Book на Integer
    @GET("/books/{id}")
    Call<GetBookResponse> getBook(@Header("Authorization") String token,
                                  @Path("id") Integer bookId);

    //выставление оценки книге
    @FormUrlEncoded
    @POST("/books/{id}")
    Call<ResponseBody> setRate(@Header("Authorization") String token,
                                 @Path("id") Integer bookId,
                                 @Field("rate") Integer rate);

    //статус книги
    @FormUrlEncoded
    @PUT("/books/{id}")
    Call<ResponseBody> setStatus(@Header("Authorization") String token,
                               @Path("id") Integer bookId,
                               @Field("status") String status);

    //отзывы
    //получение
    //range = week || month || year
    @GET("/books/{id}/reviews")
    Call<GetReviewsResponse> getReviews(@Header("Authorization") String token,
                                        @Path("id") Integer bookId,
                                        @Query("range") String range);
    //добавление
    @FormUrlEncoded
    @POST("/books/{id}/reviews")
    Call<ResponseBody> postReview(@Header("Authorization") String token,
                                  @Path("id") Integer bookId,
                                  @Field("text") String text);

    //заметки
    //получение заметок для книги
    @GET("/books/{id}/notes")
    Call<GetNotesResponse> getNotes(@Header("Authorization") String token,
                                    @Path("id") Integer bookId);

    //написание заметки
    @FormUrlEncoded
    @POST("/books/{id}/notes")
    Call<ResponseBody> postNote(@Header("Authorization") String token,
                                @Path("id") Integer bookId,
                                @Field("title") String title,
                                @Field("text") String text);


    //редактирование заметки
    //text = null если редактируем только название
    //и наоборот
    @PUT("/books/notes/{id}")
    Call<ResponseBody> editNote(@Header("Authorization") String token,
                                @Path("id") Integer noteId,
                                @Query("title") String title,
                                @Query("text") String text);

    //удаление заметки
    @DELETE("/books/notes/{id}")
    Call<ResponseBody> deleteNote(@Header("Authorization") String token,
                                  @Path("id") Integer noteId);
}