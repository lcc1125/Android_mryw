package com.example.myapplication.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * 题目数据模型
 */
public class Question {
    @SerializedName("id")
    private Long id;

    @SerializedName("content")
    private String content;

    @SerializedName("type")
    private String type;

    @SerializedName("difficulty")
    private String difficulty;

    @SerializedName("category_id")
    private Long categoryId;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("standard_answer")
    private String standardAnswer;

    @SerializedName("created_at")
    private String createdAt;

    // Constructors
    public Question() {}

    public Question(String content, String type, String difficulty, Long categoryId) {
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

    /**
     * 获取难度显示名称
     */
    public String getDifficultyDisplayName() {
        if (difficulty == null) return "未知";
        switch (difficulty.toUpperCase()) {
            case "EASY": return "简单";
            case "MEDIUM": return "中等";
            case "HARD": return "困难";
            default: return difficulty;
        }
    }

    /**
     * 获取难度颜色资源ID
     */
    public int getDifficultyColor() {
        if (difficulty == null) return 0xFF757575; // Gray
        switch (difficulty.toUpperCase()) {
            case "EASY": return 0xFF4CAF50; // Green
            case "MEDIUM": return 0xFFFF9800; // Orange
            case "HARD": return 0xFFF44336; // Red
            default: return 0xFF757575; // Gray
        }
    }
}
