package com.example.myapplication.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * 提交答案请求模型
 */
public class SubmitAnswerRequest {
    @SerializedName("question_id")
    private Long questionId;

    @SerializedName("user_answer")
    private String userAnswer;

    @SerializedName("answer_type")
    private String answerType; // "TEXT" or "VOICE"

    public SubmitAnswerRequest(Long questionId, String userAnswer, String answerType) {
        this.questionId = questionId;
        this.userAnswer = userAnswer;
        this.answerType = answerType;
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

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }
}
