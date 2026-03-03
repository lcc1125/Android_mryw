package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.data.local.entity.AnswerRecordEntity;

import java.util.List;

/**
 * 答题记录数据访问对象
 */
@Dao
public interface AnswerRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AnswerRecordEntity record);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AnswerRecordEntity> records);

    @Query("SELECT * FROM answer_records WHERE userId = :userId ORDER BY createdAt DESC LIMIT :limit")
    List<AnswerRecordEntity> getRecentRecords(Long userId, int limit);

    @Query("SELECT * FROM answer_records WHERE userId = :userId ORDER BY createdAt DESC")
    List<AnswerRecordEntity> getAllRecords(Long userId);

    @Query("SELECT * FROM answer_records WHERE userId = :userId AND questionId = :questionId")
    List<AnswerRecordEntity> getRecordsByQuestion(Long userId, Long questionId);

    @Query("SELECT COUNT(*) FROM answer_records WHERE userId = :userId")
    int getTotalAnswerCount(Long userId);

    @Query("SELECT COUNT(*) FROM answer_records WHERE userId = :userId AND isCorrect = 1")
    int getCorrectAnswerCount(Long userId);

    @Query("SELECT AVG(score) FROM answer_records WHERE userId = :userId")
    double getAverageScore(Long userId);

    @Query("DELETE FROM answer_records WHERE userId = :userId")
    void deleteAllRecords(Long userId);
}
