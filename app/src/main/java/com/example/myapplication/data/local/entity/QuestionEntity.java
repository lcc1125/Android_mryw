package com.example.myapplication.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 题目本地数据库实体
 */
@Entity(tableName = "questions")
public class QuestionEntity {
    @PrimaryKey
    private Long id;

    private String content;
    private String type;
    private String difficulty;
    private Long categoryId;
    private String categoryName;
    private String standardAnswer;
    private String createdAt;
    private Long cacheTime; // 缓存时间戳

    public QuestionEntity() {}

    public QuestionEntity(Long id, String content, String type, String difficulty, Long categoryId) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.difficulty = difficulty;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStandardAnswer() {
        return standardAnswer;
    }

    public void setStandardAnswer(String standardAnswer) {
        this.standardAnswer = standardAnswer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(Long cacheTime) {
        this.cacheTime = cacheTime;
    }
}
