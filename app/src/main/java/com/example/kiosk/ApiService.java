package com.example.kiosk;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Field;

public interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    Call<ApiResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<ApiResponse> register(
            @Field("email") String email,
            @Field("password") String password
    );
}