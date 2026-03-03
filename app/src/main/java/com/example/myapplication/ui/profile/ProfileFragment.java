package com.example.myapplication.ui.profile;

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

import com.example.myapplication.MainActivity;
import com.example.myapplication.data.local.entity.UserEntity;
import com.example.myapplication.databinding.FragmentProfileBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * 个人中心Fragment
 */
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        setupViews();
        observeViewModel();
        loadUserProfile();
    }

    private void setupViews() {
        // 编辑资料
        binding.llEditProfile.setOnClickListener(v -> {
            Toast.makeText(getContext(), "编辑资料功能开发中", Toast.LENGTH_SHORT).show();
        });

        // 设置
        binding.llSettings.setOnClickListener(v -> {
            Toast.makeText(getContext(), "设置功能开发中", Toast.LENGTH_SHORT).show();
        });

        // 关于
        binding.llAbout.setOnClickListener(v -> {
            showAboutDialog();
        });

        // 退出登录
        binding.btnLogout.setOnClickListener(v -> {
            showLogoutDialog();
        });
    }

    private void observeViewModel() {
        // 观察用户信息
        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), this::displayUserProfile);

        // 观察登出结果
        viewModel.getLogoutSuccessLiveData().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                navigateToLogin();
            }
        });
    }

    private void loadUserProfile() {
        viewModel.loadUserProfile();
    }

    private void displayUserProfile(UserEntity user) {
        if (user != null) {
            // 显示昵称首字母作为头像
            String nickname = user.getNickname();
            if (nickname != null && !nickname.isEmpty()) {
                binding.tvAvatar.setText(String.valueOf(nickname.charAt(0)));
                binding.tvNickname.setText(nickname);
            } else {
                binding.tvAvatar.setText("U");
                binding.tvNickname.setText("用户");
            }

            // 显示邮箱
            binding.tvEmail.setText(user.getEmail() != null ? user.getEmail() : "未设置邮箱");
        }
    }

    private void showAboutDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("关于")
                .setMessage("每日一练 v1.0\n\n基于语音识别的简答题刷题应用")
                .setPositiveButton("确定", null)
                .show();
    }

    private void showLogoutDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("退出登录")
                .setMessage("确定要退出登录吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    viewModel.logout();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void navigateToLogin() {
        // 隐藏底部导航
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavigationVisible(false);
        }
        navController.navigate(R.id.action_profileFragment_to_loginFragment);
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
