package com.example.myapplication.data.repository;

import android.app.Application;

import com.example.myapplication.data.local.AppDatabase;
import com.example.myapplication.data.local.dao.QuestionDao;
import com.example.myapplication.data.local.entity.QuestionEntity;
import com.example.myapplication.data.model.ApiResponse;
import com.example.myapplication.data.model.Question;
import com.example.myapplication.data.remote.ApiClient;
import com.example.myapplication.data.remote.QuestionApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 题目数据仓库
 */
public class QuestionRepository {

    private final QuestionDao questionDao;
    private final QuestionApiService questionApiService;

    // 缓存过期时间：24小时
    private static final long CACHE_EXPIRE_TIME = TimeUnit.HOURS.toMillis(24);

    public QuestionRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        questionDao = database.questionDao();
        questionApiService = ApiClient.getService(QuestionApiService.class);
    }

    /**
     * 获取今日题目（优先从缓存获取）
     */
    public void getDailyQuestions(QuestionCallback<List<Question>> callback) {
        // 先从本地缓存获取
        new Thread(() -> {
            List<QuestionEntity> cachedQuestions = questionDao.getRecentQuestions(5);
            long currentTime = System.currentTimeMillis();

            // 检查缓存是否有效
            if (cachedQuestions != null && !cachedQuestions.isEmpty()) {
                boolean isCacheValid = true;
                for (QuestionEntity entity : cachedQuestions) {
                    if (entity.getCacheTime() == null ||
                            (currentTime - entity.getCacheTime()) > CACHE_EXPIRE_TIME) {
                        isCacheValid = false;
                        break;
                    }
                }

                if (isCacheValid) {
                    List<Question> questions = convertToQuestionList(cachedQuestions);
                    callback.onSuccess(questions);
                    return;
                }
            }

            // 缓存无效或为空，从服务器获取
            fetchDailyQuestionsFromServer(callback);
        }).start();
    }

    /**
     * 从服务器获取今日题目
     */
    private void fetchDailyQuestionsFromServer(QuestionCallback<List<Question>> callback) {
        questionApiService.getDailyQuestions().enqueue(new Callback<ApiResponse<List<Question>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Question>>> call, Response<ApiResponse<List<Question>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Question>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        List<Question> questions = apiResponse.getData();
                        // 缓存到本地数据库
                        cacheQuestions(questions);
                        callback.onSuccess(questions);
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("获取题目失败");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Question>>> call, Throwable t) {
                callback.onError("网络错误: " + t.getMessage());
            }
        });
    }

    /**
     * 根据ID获取题目详情
     */
    public void getQuestionDetail(Long questionId, QuestionCallback<Question> callback) {
        // 先从本地获取
        new Thread(() -> {
            QuestionEntity cached = questionDao.getQuestionById(questionId);
            if (cached != null) {
                callback.onSuccess(convertToQuestion(cached));
                return;
            }

            // 从服务器获取
            questionApiService.getQuestionDetail(questionId).enqueue(new Callback<ApiResponse<Question>>() {
                @Override
                public void onResponse(Call<ApiResponse<Question>> call, Response<ApiResponse<Question>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<Question> apiResponse = response.body();
                        if (apiResponse.isSuccess()) {
                            Question question = apiResponse.getData();
                            // 缓存到本地
                            cacheQuestion(question);
                            callback.onSuccess(question);
                        } else {
                            callback.onError(apiResponse.getMessage());
                        }
                    } else {
                        callback.onError("获取题目详情失败");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Question>> call, Throwable t) {
                    callback.onError("网络错误: " + t.getMessage());
                }
            });
        }).start();
    }

    /**
     * 获取随机题目
     */
    public void getRandomQuestion(QuestionCallback<Question> callback) {
        questionApiService.getRandomQuestion().enqueue(new Callback<ApiResponse<Question>>() {
            @Override
            public void onResponse(Call<ApiResponse<Question>> call, Response<ApiResponse<Question>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Question> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Question question = apiResponse.getData();
                        cacheQuestion(question);
                        callback.onSuccess(question);
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("获取随机题目失败");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Question>> call, Throwable t) {
                callback.onError("网络错误: " + t.getMessage());
            }
        });
    }

    /**
     * 缓存题目列表
     */
    private void cacheQuestions(List<Question> questions) {
        new Thread(() -> {
            List<QuestionEntity> entities = new ArrayList<>();
            long currentTime = System.currentTimeMillis();

            for (Question question : questions) {
                QuestionEntity entity = convertToQuestionEntity(question);
                entity.setCacheTime(currentTime);
                entities.add(entity);
            }

            questionDao.insertAll(entities);
        }).start();
    }

    /**
     * 缓存单个题目
     */
    private void cacheQuestion(Question question) {
        new Thread(() -> {
            QuestionEntity entity = convertToQuestionEntity(question);
            entity.setCacheTime(System.currentTimeMillis());
            questionDao.insert(entity);
        }).start();
    }

    /**
     * 清理过期缓存
     */
    public void clearExpiredCache() {
        new Thread(() -> {
            long expireTime = System.currentTimeMillis() - CACHE_EXPIRE_TIME;
            questionDao.deleteExpiredQuestions(expireTime);
        }).start();
    }

    // 转换方法
    private List<Question> convertToQuestionList(List<QuestionEntity> entities) {
        List<Question> questions = new ArrayList<>();
        for (QuestionEntity entity : entities) {
            questions.add(convertToQuestion(entity));
        }
        return questions;
    }

    private Question convertToQuestion(QuestionEntity entity) {
        Question question = new Question();
        question.setId(entity.getId());
        question.setContent(entity.getContent());
        question.setType(entity.getType());
        question.setDifficulty(entity.getDifficulty());
        question.setCategoryId(entity.getCategoryId());
        question.setCategoryName(entity.getCategoryName());
        question.setStandardAnswer(entity.getStandardAnswer());
        question.setCreatedAt(entity.getCreatedAt());
        return question;
    }

    private QuestionEntity convertToQuestionEntity(Question question) {
        QuestionEntity entity = new QuestionEntity();
        entity.setId(question.getId());
        entity.setContent(question.getContent());
        entity.setType(question.getType());
        entity.setDifficulty(question.getDifficulty());
        entity.setCategoryId(question.getCategoryId());
        entity.setCategoryName(question.getCategoryName());
        entity.setStandardAnswer(question.getStandardAnswer());
        entity.setCreatedAt(question.getCreatedAt());
        return entity;
    }

    /**
     * 回调接口
     */
    public interface QuestionCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }
}
