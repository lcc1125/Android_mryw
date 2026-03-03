package com.example.myapplication.data.remote;

import com.example.myapplication.data.model.ApiResponse;
import com.example.myapplication.data.model.AnswerRecord;
import com.example.myapplication.data.model.SubmitAnswerRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 答题相关API接口
 */
public interface AnswerApiService {

    /**
     * 提交答案并评分
     */
    @POST("answers/submit")
    Call<ApiResponse<AnswerRecord>> submitAnswer(@Body SubmitAnswerRequest request);

    /**
     * 获取用户答题历史
     */
    @GET("answers/history")
    Call<ApiResponse<List<AnswerRecord>>> getAnswerHistory(
            @Query("page") int page,
            @Query("size") int size
    );

    /**
     * 获取某道题的答题记录
     */
    @GET("answers/question/{questionId}")
    Call<ApiResponse<List<AnswerRecord>>> getAnswersByQuestion(@Path("questionId") Long questionId);
}
