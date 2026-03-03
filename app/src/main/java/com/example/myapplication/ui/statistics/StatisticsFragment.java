package com.example.myapplication.ui.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.data.model.AnswerRecord;
import com.example.myapplication.data.model.StatisticsOverview;
import com.example.myapplication.databinding.FragmentStatisticsBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计页面Fragment
 */
public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;
    private StatisticsViewModel viewModel;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);

        setupViews();
        observeViewModel();
        loadData();
    }

    private void setupViews() {
        // 设置最近答题列表
        binding.rvRecentRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO: 设置适配器
    }

    private void observeViewModel() {
        // 观察统计数据
        viewModel.getStatisticsLiveData().observe(getViewLifecycleOwner(), this::displayStatistics);

        // 观察连续天数
        viewModel.getConsecutiveDaysLiveData().observe(getViewLifecycleOwner(), days -> {
            if (days != null) {
                binding.tvConsecutiveDays.setText(String.valueOf(days));
            }
        });

        // 观察加载状态
        viewModel.getLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            // 可以显示进度条
        });

        // 观察错误
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            // 显示错误提示
        });
    }

    private void loadData() {
        viewModel.loadStatisticsOverview();
        viewModel.loadConsecutiveDays();
    }

    private void displayStatistics(StatisticsOverview statistics) {
        // 显示统计数据
        if (statistics.getTotalQuestions() != null) {
            binding.tvTotalQuestions.setText(String.valueOf(statistics.getTotalQuestions()));
        }

        if (statistics.getTotalCorrect() != null) {
            binding.tvCorrectCount.setText(String.valueOf(statistics.getTotalCorrect()));
        }

        // 显示正确率
        double accuracyRate = statistics.getAccuracyRate();
        binding.tvAccuracyRate.setText(String.format("%.1f%%", accuracyRate));

        // 显示图表
        displayChart(statistics);

        // 显示最近答题记录
        List<AnswerRecord> recentRecords = statistics.getRecentRecords();
        if (recentRecords != null && !recentRecords.isEmpty()) {
            binding.llEmpty.setVisibility(View.GONE);
            binding.rvRecentRecords.setVisibility(View.VISIBLE);
            // TODO: 更新适配器数据
        } else {
            binding.llEmpty.setVisibility(View.VISIBLE);
            binding.rvRecentRecords.setVisibility(View.GONE);
        }
    }

    private void displayChart(StatisticsOverview statistics) {
        LineChart chart = binding.lineChart;

        // 准备数据
        List<StatisticsOverview.DailyStat> dailyStats = statistics.getDailyStats();
        ArrayList<Entry> entries = new ArrayList<>();

        if (dailyStats != null) {
            for (int i = 0; i < dailyStats.size(); i++) {
                StatisticsOverview.DailyStat stat = dailyStats.get(i);
                entries.add(new Entry(i, stat.getTotalQuestions()));
            }
        }

        // 创建数据集
        LineDataSet dataSet = new LineDataSet(entries, "每日答题数");
        dataSet.setColor(Color.parseColor("#6200EE"));
        dataSet.setCircleColor(Color.parseColor("#6200EE"));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextSize(10f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.animateX(1000);
        chart.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavigationVisible(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
