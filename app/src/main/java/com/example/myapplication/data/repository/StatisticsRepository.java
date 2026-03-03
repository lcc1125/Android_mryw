package com.example.myapplication.data.repository;

import android.app.Application;

import com.example.myapplication.data.model.ApiResponse;
import com.example.myapplication.data.model.StatisticsOverview;
import com.example.myapplication.data.remote.ApiClient;
import com.example.myapplication.data.remote.StatisticsApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 统计数据仓库
 */
public class StatisticsRepository {

    private final StatisticsApiService statisticsApiService;

    public StatisticsRepository(Application application) {
        statisticsApiService = ApiClient.getService(StatisticsApiService.class);
    }

    /**
     * 获取统计概览
     */
    public void getStatisticsOverview(StatisticsCallback<StatisticsOverview> callback) {
        statisticsApiService.getOverview().enqueue(new Callback<ApiResponse<StatisticsOverview>>() {
            @Override
            public void onResponse(Call<ApiResponse<StatisticsOverview>> call, Response<ApiResponse<StatisticsOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<StatisticsOverview> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("获取统计数据失败");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<StatisticsOverview>> call, Throwable t) {
                callback.onError("网络错误: " + t.getMessage());
            }
        });
    }

    /**
     * 获取连续学习天数
     */
    public void getConsecutiveDays(StatisticsCallback<Integer> callback) {
        statisticsApiService.getConsecutiveDays().enqueue(new Callback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Integer> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("获取连续天数失败");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                callback.onError("网络错误: " + t.getMessage());
            }
        });
    }

    /**
     * 回调接口
     */
    public interface StatisticsCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }
}
