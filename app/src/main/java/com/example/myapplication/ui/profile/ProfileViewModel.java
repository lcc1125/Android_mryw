package com.example.myapplication.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.local.entity.UserEntity;
import com.example.myapplication.data.model.User;
import com.example.myapplication.data.repository.AuthRepository;

/**
 * 个人中心ViewModel
 */
public class ProfileViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;

    // LiveData for UI observation
    private final MutableLiveData<UserEntity> userProfileLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutSuccessLiveData = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    /**
     * 加载用户信息
     */
    public void loadUserProfile() {
        UserEntity user = authRepository.getCurrentUser();
        if (user != null) {
            userProfileLiveData.setValue(user);
        } else {
            errorLiveData.setValue("未登录或用户信息加载失败");
        }
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
                // 即使失败也认为登出成功
                logoutSuccessLiveData.setValue(true);
            }
        });
    }

    /**
     * 检查登录状态
     */
    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }

    // Getters for LiveData
    public MutableLiveData<UserEntity> getUserProfileLiveData() {
        return userProfileLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<Boolean> getLogoutSuccessLiveData() {
        return logoutSuccessLiveData;
    }
}
