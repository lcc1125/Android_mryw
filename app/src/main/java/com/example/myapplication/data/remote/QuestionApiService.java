package com.example.myapplication.data.remote;

import com.example.myapplication.data.model.ApiResponse;
import com.example.myapplication.data.model.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 题目相关API接口
 */
public interface QuestionApiService {

    /**
     * 获取今日题目
     */
    @GET("questions/daily")
    Call<ApiResponse<List<Question>>> getDailyQuestions();

    /**
     * 根据难度获取题目列表
     */
    @GET("questions/by-difficulty")
    Call<ApiResponse<List<Question>>> getQuestionsByDifficulty(@Query("difficulty") String difficulty);

    /**
     * 根据分类获取题目列表
     */
    @GET("questions/by-category")
    Call<ApiResponse<List<Question>>> getQuestionsByCategory(@Query("categoryId") Long categoryId);

    /**
     * 获取题目详情
     */
    @GET("questions/{id}")
    Call<ApiResponse<Question>> getQuestionDetail(@Path("id") Long questionId);

    /**
     * 获取随机题目
     */
    @GET("questions/random")
    Call<ApiResponse<Question>> getRandomQuestion();
}
