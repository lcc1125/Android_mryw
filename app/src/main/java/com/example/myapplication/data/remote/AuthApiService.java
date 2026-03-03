package com.example.myapplication.data.remote;

import com.example.myapplication.data.model.ApiResponse;
import com.example.myapplication.data.model.LoginRequest;
import com.example.myapplication.data.model.RegisterRequest;
import com.example.myapplication.data.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 认证相关API接口
 */
public interface AuthApiService {

    /**
     * 用户注册
     */
    @POST("auth/register")
    Call<ApiResponse<User>> register(@Body RegisterRequest request);

    /**
     * 用户登录
     */
    @POST("auth/login")
    Call<ApiResponse<User>> login(@Body LoginRequest request);

    /**
     * 用户登出
     */
    @POST("auth/logout")
    Call<ApiResponse<Void>> logout();
}
