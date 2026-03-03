package com.example.myapplication.ui.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.data.model.AnswerRecord;
import com.example.myapplication.databinding.FragmentResultBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * 答题结果Fragment
 */
public class ResultFragment extends Fragment {

    private FragmentResultBinding binding;
    private NavController navController;
    private AnswerRecord answerRecord;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        // 获取答题记录
        Bundle args = getArguments();
        if (args != null) {
            answerRecord = (AnswerRecord) args.getSerializable("answerRecord");
            if (answerRecord != null) {
                displayResult(answerRecord);
            }
        }

        setupViews();
    }

    private void setupViews() {
        // 返回首页按钮
        binding.btnBackHome.setOnClickListener(v -> {
            navController.navigate(R.id.action_resultFragment_to_homeFragment);
        });

        // 下一题按钮
        binding.btnNextQuestion.setOnClickListener(v -> {
            navController.navigate(R.id.action_resultFragment_to_practiceFragment);
        });
    }

    private void displayResult(AnswerRecord record) {
        // 显示分数
        double score = record.getScore() != null ? record.getScore() : 0.0;
        binding.tvScore.setText(String.format("%.0f", score));

        // 显示分数等级
        String scoreLabel;
        if (score >= 90) {
            scoreLabel = "优秀";
        } else if (score >= 80) {
            scoreLabel = "良好";
        } else if (score >= 60) {
            scoreLabel = "及格";
        } else {
            scoreLabel = "需要努力";
        }
        binding.tvScoreLabel.setText(scoreLabel);

        // 显示用户答案
        binding.tvUserAnswer.setText(record.getUserAnswer());

        // 显示匹配的关键词
        List<AnswerRecord.MatchedKeyword> keywords = record.getMatchedKeywords();
        if (keywords != null && !keywords.isEmpty()) {
            binding.cgKeywords.removeAllViews();
            for (AnswerRecord.MatchedKeyword keyword : keywords) {
                Chip chip = new Chip(requireContext());
                chip.setText(keyword.getKeyword());
                chip.setCheckable(false);
                chip.setClickable(false);

                // 根据是否必答设置样式
                if (keyword.getIsRequired() != null && keyword.getIsRequired()) {
                    chip.setChipBackgroundColorResource(R.color.purple_500);
                    chip.setTextColor(getResources().getColor(android.R.color.white));
                } else {
                    chip.setChipBackgroundColorResource(R.color.purple_100);
                }

                binding.cgKeywords.addView(chip);
            }
            binding.cvKeywords.setVisibility(View.VISIBLE);
        } else {
            binding.cvKeywords.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
