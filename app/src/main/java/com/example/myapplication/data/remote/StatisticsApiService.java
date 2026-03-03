package com.example.myapplication.data.remote;

import com.example.myapplication.data.model.ApiResponse;
import com.example.myapplication.data.model.StatisticsOverview;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 统计相关API接口
 */
public interface StatisticsApiService {

    /**
     * 获取学习统计概览
     */
    @GET("statistics/overview")
    Call<ApiResponse<StatisticsOverview>> getOverview();

    /**
     * 获取指定日期范围的统计数据
     */
    @GET("statistics/range")
    Call<ApiResponse<StatisticsOverview>> getStatisticsByRange(
            @Query("startDate") String startDate,
            @Query("endDate") String endDate
    );

    /**
     * 获取连续学习天数
     */
    @GET("statistics/consecutive-days")
    Call<ApiResponse<Integer>> getConsecutiveDays();
}
