package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.data.model.Question;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 首页Fragment - 显示今日推荐题目
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private QuestionAdapter questionAdapter;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setupViews();
        observeViewModel();
        loadQuestions();
    }

    private void setupViews() {
        // 设置日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        binding.tvDate.setText(dateFormat.format(new Date()));

        // 设置题目列表
        questionAdapter = new QuestionAdapter();
        binding.rvQuestions.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvQuestions.setAdapter(questionAdapter);

        // 题目点击事件
        questionAdapter.setOnQuestionClickListener((question, position) -> {
            navigateToPractice(question.getId());
        });

        // 下拉刷新
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.refreshQuestions();
        });

        // 随机题目按钮
        binding.fabRandom.setOnClickListener(v -> {
            navigateToPractice(null); // null表示随机题目
        });

        // 重试按钮
        binding.btnRetry.setOnClickListener(v -> {
            loadQuestions();
        });
    }

    private void observeViewModel() {
        // 观察题目列表
        viewModel.getQuestionsLiveData().observe(getViewLifecycleOwner(), questions -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            binding.progressIndicator.setVisibility(View.GONE);

            if (questions == null || questions.isEmpty()) {
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.rvQuestions.setVisibility(View.GONE);
            } else {
                binding.llEmpty.setVisibility(View.GONE);
                binding.rvQuestions.setVisibility(View.VISIBLE);
                questionAdapter.setQuestions(questions);
            }
        });

        // 观察错误信息
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            binding.progressIndicator.setVisibility(View.GONE);
            Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG).show();
        });

        // 观察加载状态
        viewModel.getLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading && binding.rvQuestions.getAdapter() == null) {
                binding.progressIndicator.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadQuestions() {
        // 设置欢迎语
        String nickname = com.example.myapplication.utils.SharedPreferencesManager
                .getInstance(getContext()).getNickname();
        if (nickname != null && !nickname.isEmpty()) {
            binding.tvGreeting.setText("你好，" + nickname + "！");
        }

        viewModel.loadDailyQuestions();
    }

    private void navigateToPractice(Long questionId) {
        Bundle bundle = new Bundle();
        if (questionId != null) {
            bundle.putLong("questionId", questionId);
        }
        navController.navigate(R.id.action_homeFragment_to_practiceFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 显示底部导航
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavigationVisible(true);
        }
    }
}
