package com.example.myapplication.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 答题记录数据模型
 */
public class AnswerRecord {
    @SerializedName("id")
    private Long id;

    @SerializedName("user_id")
    private Long userId;

    @SerializedName("question_id")
    private Long questionId;

    @SerializedName("user_answer")
    private String userAnswer;

    @SerializedName("score")
    private Double score;

    @SerializedName("matched_keywords")
    private List<MatchedKeyword> matchedKeywords;

    @SerializedName("is_correct")
    private Boolean isCorrect;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("question")
    private Question question;

    /**
     * 匹配的关键词
     */
    public static class MatchedKeyword {
        @SerializedName("keyword")
        private String keyword;

        @SerializedName("weight")
        private Double weight;

        @SerializedName("is_required")
        private Boolean isRequired;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }

        public Boolean getIsRequired() {
            return isRequired;
        }

        public void setIsRequired(Boolean isRequired) {
            this.isRequired = isRequired;
        }
    }

    // Constructors
    public AnswerRecord() {}

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

    public List<MatchedKeyword> getMatchedKeywords() {
        return matchedKeywords;
    }

    public void setMatchedKeywords(List<MatchedKeyword> matchedKeywords) {
        this.matchedKeywords = matchedKeywords;
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
