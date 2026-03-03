package com.example.myapplication.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentLoginBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * 登录页面
 */
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthViewModel viewModel;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // 检查是否已登录
        if (viewModel.isLoggedIn()) {
            navigateToHome();
            return;
        }

        setupViews();
        observeViewModel();
    }

    private void setupViews() {
        // 登录按钮点击事件
        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (validateInput(username, password)) {
                viewModel.login(username, password);
            }
        });

        // 注册链接点击事件
        binding.tvRegister.setOnClickListener(v -> {
            navController.navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }

    private void observeViewModel() {
        // 观察登录结果
        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            Toast.makeText(getContext(), "登录成功！欢迎 " + user.getNickname(),
                    Toast.LENGTH_SHORT).show();
            navigateToHome();
        });

        // 观察错误信息
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG).show();
        });

        // 观察加载状态
        viewModel.getLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.progressIndicator.setVisibility(View.VISIBLE);
                binding.btnLogin.setEnabled(false);
            } else {
                binding.progressIndicator.setVisibility(View.GONE);
                binding.btnLogin.setEnabled(true);
            }
        });
    }

    private boolean validateInput(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            binding.tilUsername.setError("请输入用户名");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("请输入密码");
            return false;
        }

        if (password.length() < 6) {
            binding.tilPassword.setError("密码长度不能少于6位");
            return false;
        }

        return true;
    }

    private void navigateToHome() {
        // 设置主Activity的底部导航可见
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavigationVisible(true);
        }
        navController.navigate(R.id.action_loginFragment_to_homeFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
