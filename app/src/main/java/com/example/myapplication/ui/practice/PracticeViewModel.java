package com.example.myapplication.ui.practice;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.model.AnswerRecord;
import com.example.myapplication.data.model.Question;
import com.example.myapplication.data.repository.AnswerRepository;
import com.example.myapplication.data.repository.QuestionRepository;

/**
 * 答题页面ViewModel
 */
public class PracticeViewModel extends AndroidViewModel {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    // LiveData for UI observation
    private final MutableLiveData<Question> questionLiveData = new MutableLiveData<>();
    private final MutableLiveData<AnswerRecord> answerResultLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> submittingLiveData = new MutableLiveData<>();

    public PracticeViewModel(@NonNull Application application) {
        super(application);
        questionRepository = new QuestionRepository(application);
        answerRepository = new AnswerRepository(application);
    }

    /**
     * 加载题目详情
     */
    public void loadQuestion(Long questionId) {
        loadingLiveData.setValue(true);

        questionRepository.getQuestionDetail(questionId, new QuestionRepository.QuestionCallback<Question>() {
            @Override
            public void onSuccess(Question data) {
                loadingLiveData.setValue(false);
                questionLiveData.setValue(data);
            }

            @Override
            public void onError(String message) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(message);
            }
        });
    }

    /**
     * 获取随机题目
     */
    public void loadRandomQuestion() {
        loadingLiveData.setValue(true);

        questionRepository.getRandomQuestion(new QuestionRepository.QuestionCallback<Question>() {
            @Override
            public void onSuccess(Question data) {
                loadingLiveData.setValue(false);
                questionLiveData.setValue(data);
            }

            @Override
            public void onError(String message) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(message);
            }
        });
    }

    /**
     * 提交答案（文本）
     */
    public void submitTextAnswer(Long questionId, String userAnswer) {
        submittingLiveData.setValue(true);

        answerRepository.submitAnswer(questionId, userAnswer, "TEXT",
                new AnswerRepository.AnswerCallback<AnswerRecord>() {
                    @Override
                    public void onSuccess(AnswerRecord data) {
                        submittingLiveData.setValue(false);
                        answerResultLiveData.setValue(data);
                    }

                    @Override
                    public void onError(String message) {
                        submittingLiveData.setValue(false);
                        errorLiveData.setValue(message);
                    }
                });
    }

    /**
     * 提交答案（语音）
     */
    public void submitVoiceAnswer(Long questionId, String voiceText) {
        submittingLiveData.setValue(true);

        answerRepository.submitAnswer(questionId, voiceText, "VOICE",
                new AnswerRepository.AnswerCallback<AnswerRecord>() {
                    @Override
                    public void onSuccess(AnswerRecord data) {
                        submittingLiveData.setValue(false);
                        answerResultLiveData.setValue(data);
                    }

                    @Override
                    public void onError(String message) {
                        submittingLiveData.setValue(false);
                        errorLiveData.setValue(message);
                    }
                });
    }

    // Getters for LiveData
    public MutableLiveData<Question> getQuestionLiveData() {
        return questionLiveData;
    }

    public MutableLiveData<AnswerRecord> getAnswerResultLiveData() {
        return answerResultLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public MutableLiveData<Boolean> getSubmittingLiveData() {
        return submittingLiveData;
    }
}
