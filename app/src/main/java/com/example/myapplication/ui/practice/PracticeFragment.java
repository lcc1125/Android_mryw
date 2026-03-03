package com.example.myapplication.ui.practice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.data.model.AnswerRecord;
import com.example.myapplication.data.model.Question;
import com.example.myapplication.databinding.FragmentPracticeBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * 答题页面Fragment
 */
public class PracticeFragment extends Fragment implements SpeechRecognitionHelper.RecognitionCallback {

    private FragmentPracticeBinding binding;
    private PracticeViewModel viewModel;
    private NavController navController;

    private SpeechRecognitionHelper speechHelper;
    private Question currentQuestion;
    private boolean isVoiceRecording = false;

    // 权限请求
    private ActivityResultLauncher<String> audioPermissionLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPracticeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(PracticeViewModel.class);

        setupPermissionLauncher();
        setupViews();
        setupSpeechRecognition();
        observeViewModel();
        loadQuestion();
    }

    private void setupPermissionLauncher() {
        audioPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        startVoiceRecognition();
                    } else {
                        Snackbar.make(binding.getRoot(), "需要麦克风权限才能使用语音输入",
                                Snackbar.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void setupViews() {
        // 语音输入按钮
        binding.btnVoiceInput.setOnClickListener(v -> {
            if (isVoiceRecording) {
                stopVoiceRecognition();
            } else {
                checkPermissionAndStartVoice();
            }
        });

        // 提交答案按钮
        binding.btnSubmit.setOnClickListener(v -> {
            String answer = binding.etAnswer.getText().toString().trim();
            if (TextUtils.isEmpty(answer)) {
                Snackbar.make(binding.getRoot(), "请输入答案", Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (currentQuestion != null) {
                viewModel.submitTextAnswer(currentQuestion.getId(), answer);
            }
        });
    }

    private void setupSpeechRecognition() {
        speechHelper = new SpeechRecognitionHelper(requireContext());
        speechHelper.setCallback(this);
        speechHelper.createSpeechRecognizer();
    }

    private void observeViewModel() {
        // 观察题目数据
        viewModel.getQuestionLiveData().observe(getViewLifecycleOwner(), question -> {
            currentQuestion = question;
            displayQuestion(question);
        });

        // 观察答题结果
        viewModel.getAnswerResultLiveData().observe(getViewLifecycleOwner(), result -> {
            // 导航到结果页面
            Bundle bundle = new Bundle();
            bundle.putSerializable("answerRecord", result);
            navController.navigate(R.id.action_practiceFragment_to_resultFragment, bundle);
        });

        // 观察错误信息
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG).show();
        });

        // 观察加载状态
        viewModel.getLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // 观察提交状态
        viewModel.getSubmittingLiveData().observe(getViewLifecycleOwner(), isSubmitting -> {
            binding.btnSubmit.setEnabled(!isSubmitting);
            if (isSubmitting) {
                binding.btnSubmit.setText("提交中...");
            } else {
                binding.btnSubmit.setText("提交答案");
            }
        });
    }

    private void loadQuestion() {
        Bundle args = getArguments();
        if (args != null && args.containsKey("questionId")) {
            Long questionId = args.getLong("questionId");
            viewModel.loadQuestion(questionId);
        } else {
            // 加载随机题目
            viewModel.loadRandomQuestion();
        }
    }

    private void displayQuestion(Question question) {
        binding.tvQuestionContent.setText(question.getContent());
        binding.tvCategory.setText(question.getCategoryName() != null ? question.getCategoryName() : "未分类");
        binding.tvDifficulty.setText(question.getDifficultyDisplayName());

        // 设置难度背景色
        int difficultyColor = question.getDifficultyColor();
        binding.tvDifficulty.setBackgroundColor((int) difficultyColor);
    }

    private void checkPermissionAndStartVoice() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecognition();
        } else {
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    private void startVoiceRecognition() {
        if (speechHelper != null) {
            speechHelper.startListening();
            isVoiceRecording = true;
            binding.btnVoiceInput.setText("停止录音");
            binding.llVoiceIndicator.setVisibility(View.VISIBLE);
            startVoicePulseAnimation();
        }
    }

    private void stopVoiceRecognition() {
        if (speechHelper != null) {
            speechHelper.stopListening();
            isVoiceRecording = false;
            binding.btnVoiceInput.setText("语音输入");
            binding.llVoiceIndicator.setVisibility(View.GONE);
            stopVoicePulseAnimation();
        }
    }

    private void startVoicePulseAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.5f,
                1.0f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(500);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        binding.viewVoicePulse.startAnimation(scaleAnimation);
    }

    private void stopVoicePulseAnimation() {
        binding.viewVoicePulse.clearAnimation();
    }

    // SpeechRecognitionHelper.RecognitionCallback 实现
    @Override
    public void onResult(String text) {
        isVoiceRecording = false;
        binding.btnVoiceInput.setText("语音输入");
        binding.llVoiceIndicator.setVisibility(View.GONE);
        stopVoicePulseAnimation();

        // 将识别结果添加到输入框
        String currentText = binding.etAnswer.getText().toString();
        if (TextUtils.isEmpty(currentText)) {
            binding.etAnswer.setText(text);
        } else {
            binding.etAnswer.append(" " + text);
        }

        Snackbar.make(binding.getRoot(), "语音识别成功", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onPartialResult(String text) {
        binding.tvPartialResult.setVisibility(View.VISIBLE);
        binding.tvPartialResult.setText("识别中: " + text);
    }

    @Override
    public void onReadyForSpeech() {
        // 可以在这里显示提示
    }

    @Override
    public void onBeginningOfSpeech() {
        binding.tvPartialResult.setVisibility(View.VISIBLE);
        binding.tvPartialResult.setText("正在聆听...");
    }

    @Override
    public void onEndOfSpeech() {
        binding.tvPartialResult.setText("处理中...");
    }

    @Override
    public void onVolumeChanged(float rmsdB) {
        // 可以根据音量调整UI
    }

    @Override
    public void onError(String error, int errorCode) {
        isVoiceRecording = false;
        binding.btnVoiceInput.setText("语音输入");
        binding.llVoiceIndicator.setVisibility(View.GONE);
        stopVoicePulseAnimation();
        binding.tvPartialResult.setVisibility(View.GONE);

        Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        isVoiceRecording = false;
        binding.btnVoiceInput.setText("语音输入");
        binding.llVoiceIndicator.setVisibility(View.GONE);
        stopVoicePulseAnimation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (speechHelper != null) {
            speechHelper.destroy();
        }
        binding = null;
    }
}
