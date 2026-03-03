package com.example.myapplication.data.repository;

import android.app.Application;

import com.example.myapplication.data.local.AppDatabase;
import com.example.myapplication.data.local.dao.AnswerRecordDao;
import com.example.myapplication.data.local.entity.AnswerRecordEntity;
import com.example.myapplication.data.model.AnswerRecord;
import com.example.myapplication.data.model.ApiResponse;
import com.example.myapplication.data.model.SubmitAnswerRequest;
import com.example.myapplication.data.remote.ApiClient;
import com.example.myapplication.data.remote.AnswerApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 答题数据仓库
 */
public class AnswerRepository {

    private final AnswerRecordDao answerRecordDao;
    private final AnswerApiService answerApiService;

    public AnswerRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        answerRecordDao = database.answerRecordDao();
        answerApiService = ApiClient.getService(AnswerApiService.class);
    }

    /**
     * 提交答案并评分
     */
    public void submitAnswer(Long questionId, String userAnswer, String answerType,
                             AnswerCallback<AnswerRecord> callback) {
        SubmitAnswerRequest request = new SubmitAnswerRequest(questionId, userAnswer, answerType);

        answerApiService.submitAnswer(request).enqueue(new Callback<ApiResponse<AnswerRecord>>() {
            @Override
            public void onResponse(Call<ApiResponse<AnswerRecord>> call, Response<ApiResponse<AnswerRecord>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AnswerRecord> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        AnswerRecord record = apiResponse.getData();
                        // 保存到本地数据库
                        saveAnswerRecordLocally(record);
                        callback.onSuccess(record);
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("提交答案失败");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AnswerRecord>> call, Throwable t) {
                callback.onError("网络错误: " + t.getMessage());
            }
        });
    }

    /**
     * 获取答题历史
     */
    public void getAnswerHistory(int page, int size, AnswerCallback<List<AnswerRecord>> callback) {
        answerApiService.getAnswerHistory(page, size).enqueue(new Callback<ApiResponse<List<AnswerRecord>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<AnswerRecord>>> call, Response<ApiResponse<List<AnswerRecord>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<AnswerRecord>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        List<AnswerRecord> records = apiResponse.getData();
                        // 缓存到本地
                        cacheAnswerRecords(records);
                        callback.onSuccess(records);
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("获取历史记录失败");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<AnswerRecord>>> call, Throwable t) {
                callback.onError("网络错误: " + t.getMessage());
            }
        });
    }

    /**
     * 从本地获取最近的答题记录
     */
    public void getLocalAnswerRecords(Long userId, int limit, AnswerCallback<List<AnswerRecord>> callback) {
        new Thread(() -> {
            List<AnswerRecordEntity> entities = answerRecordDao.getRecentRecords(userId, limit);
            List<AnswerRecord> records = convertToAnswerRecordList(entities);
            callback.onSuccess(records);
        }).start();
    }

    /**
     * 保存答题记录到本地
     */
    private void saveAnswerRecordLocally(AnswerRecord record) {
        new Thread(() -> {
            AnswerRecordEntity entity = convertToAnswerRecordEntity(record);
            answerRecordDao.insert(entity);
        }).start();
    }

    /**
     * 批量缓存答题记录
     */
    private void cacheAnswerRecords(List<AnswerRecord> records) {
        new Thread(() -> {
            List<AnswerRecordEntity> entities = new ArrayList<>();
            for (AnswerRecord record : records) {
                entities.add(convertToAnswerRecordEntity(record));
            }
            answerRecordDao.insertAll(entities);
        }).start();
    }

    /**
     * 获取用户平均分数
     */
    public void getAverageScore(Long userId, AnswerCallback<Double> callback) {
        new Thread(() -> {
            double avgScore = answerRecordDao.getAverageScore(userId);
            callback.onSuccess(avgScore);
        }).start();
    }

    /**
     * 获取总答题数
     */
    public void getTotalAnswerCount(Long userId, AnswerCallback<Integer> callback) {
        new Thread(() -> {
            int count = answerRecordDao.getTotalAnswerCount(userId);
            callback.onSuccess(count);
        }).start();
    }

    // 转换方法
    private List<AnswerRecord> convertToAnswerRecordList(List<AnswerRecordEntity> entities) {
        List<AnswerRecord> records = new ArrayList<>();
        for (AnswerRecordEntity entity : entities) {
            records.add(convertToAnswerRecord(entity));
        }
        return records;
    }

    private AnswerRecord convertToAnswerRecord(AnswerRecordEntity entity) {
        AnswerRecord record = new AnswerRecord();
        record.setId(entity.getId());
        record.setUserId(entity.getUserId());
        record.setQuestionId(entity.getQuestionId());
        record.setUserAnswer(entity.getUserAnswer());
        record.setScore(entity.getScore());
        record.setIsCorrect(entity.getIsCorrect());
        record.setCreatedAt(entity.getCreatedAt());
        return record;
    }

    private AnswerRecordEntity convertToAnswerRecordEntity(AnswerRecord record) {
        AnswerRecordEntity entity = new AnswerRecordEntity();
        entity.setId(record.getId());
        entity.setUserId(record.getUserId());
        entity.setQuestionId(record.getQuestionId());
        entity.setUserAnswer(record.getUserAnswer());
        entity.setScore(record.getScore());
        entity.setIsCorrect(record.getIsCorrect());
        entity.setCreatedAt(record.getCreatedAt());
        // matchedKeywords可以转换为JSON字符串存储
        return entity;
    }

    /**
     * 回调接口
     */
    public interface AnswerCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }
}
