package com.example.myapplication.ui.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.local.entity.UserEntity;
import com.example.myapplication.data.model.User;
import com.example.myapplication.data.repository.AuthRepository;

/**
 * 认证ViewModel
 */
public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;

    // LiveData for UI observation
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutSuccessLiveData = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    /**
     * 用户注册
     */
    public void register(String username, String password, String email, String nickname) {
        loadingLiveData.setValue(true);

        authRepository.register(username, password, email, nickname,
                new AuthRepository.AuthCallback<User>() {
                    @Override
                    public void onSuccess(User data) {
                        loadingLiveData.setValue(false);
                        userLiveData.setValue(data);
                    }

                    @Override
                    public void onError(String message) {
                        loadingLiveData.setValue(false);
                        errorLiveData.setValue(message);
                    }
                });
    }

    /**
     * 用户登录
     */
    public void login(String username, String password) {
        loadingLiveData.setValue(true);

        authRepository.login(username, password, new AuthRepository.AuthCallback<User>() {
            @Override
            public void onSuccess(User data) {
                loadingLiveData.setValue(false);
                userLiveData.setValue(data);
            }

            @Override
            public void onError(String message) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(message);
            }
        });
    }

    /**
     * 用户登出
     */
    public void logout() {
        authRepository.logout(new AuthRepository.AuthCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                logoutSuccessLiveData.setValue(true);
            }

            @Override
            public void onError(String message) {
                // 即使失败也认为登出成功（因为本地数据已清除）
                logoutSuccessLiveData.setValue(true);
            }
        });
    }

    /**
     * 检查是否已登录
     */
    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }

    /**
     * 获取当前登录用户
     */
    public UserEntity getCurrentUser() {
        return authRepository.getCurrentUser();
    }

    // Getters for LiveData
    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public MutableLiveData<Boolean> getLogoutSuccessLiveData() {
        return logoutSuccessLiveData;
    }
}
