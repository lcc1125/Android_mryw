package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.data.local.entity.QuestionEntity;

import java.util.List;

/**
 * 题目数据访问对象
 */
@Dao
public interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuestionEntity question);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuestionEntity> questions);

    @Query("SELECT * FROM questions WHERE id = :questionId")
    QuestionEntity getQuestionById(Long questionId);

    @Query("SELECT * FROM questions ORDER BY cacheTime DESC LIMIT :limit")
    List<QuestionEntity> getRecentQuestions(int limit);

    @Query("SELECT * FROM questions WHERE difficulty = :difficulty ORDER BY cacheTime DESC")
    List<QuestionEntity> getQuestionsByDifficulty(String difficulty);

    @Query("SELECT * FROM questions WHERE categoryId = :categoryId")
    List<QuestionEntity> getQuestionsByCategory(Long categoryId);

    @Query("DELETE FROM questions WHERE cacheTime < :expireTime")
    void deleteExpiredQuestions(long expireTime);

    @Query("DELETE FROM questions")
    void deleteAllQuestions();
}
