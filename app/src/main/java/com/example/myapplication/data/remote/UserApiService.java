package com.example.myapplication.data.remote;

import com.example.myapplication.data.model.ApiResponse;
import com.example.myapplication.data.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;

/**
 * 用户相关API接口
 */
public interface UserApiService {

    /**
     * 获取当前用户信息
     */
    @GET("users/profile")
    Call<ApiResponse<User>> getUserProfile();

    /**
     * 更新用户信息
     */
    @PUT("users/profile")
    Call<ApiResponse<User>> updateUserProfile(@retrofit2.http.Body User user);
}
