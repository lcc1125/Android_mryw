package com.example.myapplication.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * 学习统计数据模型
 */
public class LearningStatistics {
    @SerializedName("id")
    private Long id;

    @SerializedName("user_id")
    private Long userId;

    @SerializedName("date")
    private String date;

    @SerializedName("total_questions")
    private Integer totalQuestions;

    @SerializedName("correct_count")
    private Integer correctCount;

    @SerializedName("consecutive_days")
    private Integer consecutiveDays;

    @SerializedName("total_study_time")
    private Long totalStudyTime;

    @SerializedName("created_at")
    private String createdAt;

    // Constructors
    public LearningStatistics() {}

    public LearningStatistics(String date, Integer totalQuestions, Integer correctCount) {
        this.date = date;
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    public Integer getConsecutiveDays() {
        return consecutiveDays;
    }

    public void setConsecutiveDays(Integer consecutiveDays) {
        this.consecutiveDays = consecutiveDays;
    }

    public Long getTotalStudyTime() {
        return totalStudyTime;
    }

    public void setTotalStudyTime(Long totalStudyTime) {
        this.totalStudyTime = totalStudyTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 计算正确率
     */
    public double getAccuracyRate() {
        if (totalQuestions == null || totalQuestions == 0) {
            return 0.0;
        }
        return (double) correctCount / totalQuestions * 100;
    }

    /**
     * 格式化学习时间（分钟）
     */
    public String getFormattedStudyTime() {
        if (totalStudyTime == null || totalStudyTime == 0) {
            return "0分钟";
        }
        long minutes = totalStudyTime / 60;
        long seconds = totalStudyTime % 60;
        if (minutes > 0) {
            return minutes + "分钟" + seconds + "秒";
        }
        return seconds + "秒";
    }
}
