package com.example.myapplication.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 统计概览数据模型
 */
public class StatisticsOverview {
    @SerializedName("total_questions")
    private Integer totalQuestions;

    @SerializedName("total_correct")
    private Integer totalCorrect;

    @SerializedName("consecutive_days")
    private Integer consecutiveDays;

    @SerializedName("total_study_time")
    private Long totalStudyTime;

    @SerializedName("recent_records")
    private List<AnswerRecord> recentRecords;

    @SerializedName("daily_stats")
    private List<DailyStat> dailyStats;

    /**
     * 每日统计数据
     */
    public static class DailyStat {
        @SerializedName("date")
        private String date;

        @SerializedName("total_questions")
        private Integer totalQuestions;

        @SerializedName("correct_count")
        private Integer correctCount;

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
    }

    // Constructors
    public StatisticsOverview() {}

    // Getters and Setters
    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(Integer totalCorrect) {
        this.totalCorrect = totalCorrect;
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

    public List<AnswerRecord> getRecentRecords() {
        return recentRecords;
    }

    public void setRecentRecords(List<AnswerRecord> recentRecords) {
        this.recentRecords = recentRecords;
    }

    public List<DailyStat> getDailyStats() {
        return dailyStats;
    }

    public void setDailyStats(List<DailyStat> dailyStats) {
        this.dailyStats = dailyStats;
    }

    /**
     * 计算总体正确率
     */
    public double getAccuracyRate() {
        if (totalQuestions == null || totalQuestions == 0) {
            return 0.0;
        }
        return (double) totalCorrect / totalQuestions * 100;
    }
}
