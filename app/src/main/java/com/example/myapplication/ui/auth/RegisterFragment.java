package com.example.myapplication.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentRegisterBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * 注册页面
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private AuthViewModel viewModel;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupViews();
        observeViewModel();
    }

    private void setupViews() {
        // 注册按钮点击事件
        binding.btnRegister.setOnClickListener(v -> {
            if (validateInput()) {
                String username = binding.etUsername.getText().toString().trim();
                String email = binding.etEmail.getText().toString().trim();
                String nickname = binding.etNickname.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();

                viewModel.register(username, password, email, nickname);
            }
        });

        // 登录链接点击事件
        binding.tvLogin.setOnClickListener(v -> {
            navController.navigate(R.id.action_registerFragment_to_loginFragment);
        });
    }

    private void observeViewModel() {
        // 观察注册结果
        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            Toast.makeText(getContext(), "注册成功！欢迎 " + user.getNickname(),
                    Toast.LENGTH_SHORT).show();
            // 注册成功后跳转到登录页
            navController.navigate(R.id.action_registerFragment_to_loginFragment);
        });

        // 观察错误信息
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG).show();
        });

        // 观察加载状态
        viewModel.getLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.progressIndicator.setVisibility(View.VISIBLE);
                binding.btnRegister.setEnabled(false);
            } else {
                binding.progressIndicator.setVisibility(View.GONE);
                binding.btnRegister.setEnabled(true);
            }
        });
    }

    private boolean validateInput() {
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String nickname = binding.etNickname.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        boolean isValid = true;

        // 验证用户名
        if (TextUtils.isEmpty(username)) {
            binding.tilUsername.setError("请输入用户名");
            isValid = false;
        } else if (username.length() < 3) {
            binding.tilUsername.setError("用户名长度不能少于3位");
            isValid = false;
        } else {
            binding.tilUsername.setError(null);
        }

        // 验证邮箱
        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError("请输入邮箱");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("请输入有效的邮箱地址");
            isValid = false;
        } else {
            binding.tilEmail.setError(null);
        }

        // 验证昵称
        if (TextUtils.isEmpty(nickname)) {
            binding.tilNickname.setError("请输入昵称");
            isValid = false;
        } else {
            binding.tilNickname.setError(null);
        }

        // 验证密码
        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("请输入密码");
            isValid = false;
        } else if (password.length() < 6) {
            binding.tilPassword.setError("密码长度不能少于6位");
            isValid = false;
        } else {
            binding.tilPassword.setError(null);
        }

        // 验证确认密码
        if (TextUtils.isEmpty(confirmPassword)) {
            binding.tilConfirmPassword.setError("请确认密码");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            binding.tilConfirmPassword.setError("两次输入的密码不一致");
            isValid = false;
        } else {
            binding.tilConfirmPassword.setError(null);
        }

        return isValid;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
