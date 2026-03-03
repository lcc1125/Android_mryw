package com.example.myapplication.ui.statistics;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.model.StatisticsOverview;
import com.example.myapplication.data.repository.StatisticsRepository;

/**
 * 统计页面ViewModel
 */
public class StatisticsViewModel extends AndroidViewModel {

    private final StatisticsRepository statisticsRepository;

    // LiveData for UI observation
    private final MutableLiveData<StatisticsOverview> statisticsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> consecutiveDaysLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    public StatisticsViewModel(@NonNull Application application) {
        super(application);
        statisticsRepository = new StatisticsRepository(application);
    }

    /**
     * 加载统计概览
     */
    public void loadStatisticsOverview() {
        loadingLiveData.setValue(true);

        statisticsRepository.getStatisticsOverview(new StatisticsRepository.StatisticsCallback<StatisticsOverview>() {
            @Override
            public void onSuccess(StatisticsOverview data) {
                loadingLiveData.setValue(false);
                statisticsLiveData.setValue(data);
            }

            @Override
            public void onError(String message) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(message);
            }
        });
    }

    /**
     * 加载连续学习天数
     */
    public void loadConsecutiveDays() {
        statisticsRepository.getConsecutiveDays(new StatisticsRepository.StatisticsCallback<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                consecutiveDaysLiveData.setValue(data);
            }

            @Override
            public void onError(String message) {
                errorLiveData.setValue(message);
            }
        });
    }

    /**
     * 刷新统计数据
     */
    public void refreshStatistics() {
        loadStatisticsOverview();
        loadConsecutiveDays();
    }

    // Getters for LiveData
    public MutableLiveData<StatisticsOverview> getStatisticsLiveData() {
        return statisticsLiveData;
    }

    public MutableLiveData<Integer> getConsecutiveDaysLiveData() {
        return consecutiveDaysLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
