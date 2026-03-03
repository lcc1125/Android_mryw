package com.example.myapplication.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.model.Question;
import com.example.myapplication.data.repository.QuestionRepository;

import java.util.List;

/**
 * 首页ViewModel
 */
public class HomeViewModel extends AndroidViewModel {

    private final QuestionRepository questionRepository;

    // LiveData for UI observation
    private final MutableLiveData<List<Question>> questionsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        questionRepository = new QuestionRepository(application);
    }

    /**
     * 加载今日题目
     */
    public void loadDailyQuestions() {
        loadingLiveData.setValue(true);

        questionRepository.getDailyQuestions(new QuestionRepository.QuestionCallback<List<Question>>() {
            @Override
            public void onSuccess(List<Question> data) {
                loadingLiveData.setValue(false);
                questionsLiveData.setValue(data);
            }

            @Override
            public void onError(String message) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(message);
            }
        });
    }

    /**
     * 刷新题目
     */
    public void refreshQuestions() {
        loadDailyQuestions();
    }

    // Getters for LiveData
    public MutableLiveData<List<Question>> getQuestionsLiveData() {
        return questionsLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
