package com.example.myapplication.data.repository;

import android.app.Application;
import android.content.Context;

import com.example.myapplication.data.local.AppDatabase;
import com.example.myapplication.data.local.dao.UserDao;
import com.example.myapplication.data.local.entity.UserEntity;
import com.example.myapplication.data.model.ApiResponse;
import com.example.myapplication.data.model.LoginRequest;
import com.example.myapplication.data.model.RegisterRequest;
import com.example.myapplication.data.model.User;
import com.example.myapplication.data.remote.AuthApiService;
import com.example.myapplication.data.remote.ApiClient;
import com.example.myapplication.utils.SharedPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 认证数据仓库
 */
public class AuthRepository {

    private final UserDao userDao;
    private final AuthApiService authApiService;
    private final SharedPreferencesManager prefsManager;

    public AuthRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        authApiService = ApiClient.getService(AuthApiService.class);
        prefsManager = SharedPreferencesManager.getInstance(application);
    }

    /**
     * 用户注册
     */
    public void register(String username, String password, String email, String nickname,
                         AuthCallback<User> callback) {
        RegisterRequest request = new RegisterRequest(username, password, email, nickname);

        authApiService.register(request).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        User user = apiResponse.getData();
                        // 保存到本地数据库
                        saveUserLocally(user, apiResponse.getToken());
                        callback.onSuccess(user);
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("注册失败");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                callback.onError("网络错误: " + t.getMessage());
            }
        });
    }

    /**
     * 用户登录
     */
    public void login(String username, String password, AuthCallback<User> callback) {
        LoginRequest request = new LoginRequest(username, password);

        authApiService.login(request).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        User user = apiResponse.getData();
                        // 保存到本地数据库和SharedPreferences
                        saveUserLocally(user, apiResponse.getToken());
                        callback.onSuccess(user);
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("登录失败");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                callback.onError("网络错误: " + t.getMessage());
            }
        });
    }

    /**
     * 用户登出
     */
    public void logout(AuthCallback<Void> callback) {
        authApiService.logout().enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                // 无论后端是否成功，都清除本地数据
                clearLocalData();
                callback.onSuccess(null);
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                // 网络失败也清除本地数据
                clearLocalData();
                callback.onSuccess(null);
            }
        });
    }

    /**
     * 保存用户信息到本地
     */
    private void saveUserLocally(User user, String token) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setNickname(user.getNickname());
        entity.setAvatar(user.getAvatar());
        entity.setToken(token != null ? token : user.getToken());

        userDao.insert(entity);

        // 保存到SharedPreferences
        prefsManager.saveToken(token != null ? token : user.getToken());
        prefsManager.saveUserId(user.getId());
        prefsManager.saveUsername(user.getUsername());
        prefsManager.saveNickname(user.getNickname());
        prefsManager.setLoggedIn(true);
    }

    /**
     * 清除本地数据
     */
    private void clearLocalData() {
        prefsManager.clear();
        // 可选：删除本地数据库用户记录
        // userDao.deleteAllUsers();
    }

    /**
     * 检查是否已登录
     */
    public boolean isLoggedIn() {
        return prefsManager.isLoggedIn();
    }

    /**
     * 获取当前登录用户
     */
    public UserEntity getCurrentUser() {
        return userDao.getLoggedInUser();
    }

    /**
     * 回调接口
     */
    public interface AuthCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }
}
