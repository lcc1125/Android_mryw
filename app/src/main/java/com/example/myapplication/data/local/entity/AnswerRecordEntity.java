package com.example.myapplication.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 答题记录本地数据库实体
 */
@Entity(tableName = "answer_records")
public class AnswerRecordEntity {
    @PrimaryKey
    private Long id;

    private Long userId;
    private Long questionId;
    private String userAnswer;
    private Double score;
    private Boolean isCorrect;
    private String createdAt;

    // JSON存储匹配的关键词
    private String matchedKeywordsJson;

    public AnswerRecordEntity() {}

    public AnswerRecordEntity(Long id, Long userId, Long questionId, String userAnswer, Double score) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.userAnswer = userAnswer;
        this.score = score;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getMatchedKeywordsJson() {
        return matchedKeywordsJson;
    }

    public void setMatchedKeywordsJson(String matchedKeywordsJson) {
        this.matchedKeywordsJson = matchedKeywordsJson;
    }
}
