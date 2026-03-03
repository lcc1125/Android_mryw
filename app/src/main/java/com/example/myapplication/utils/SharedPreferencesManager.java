package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences管理工具类
 */
public class SharedPreferencesManager {

    private static final String PREF_NAME = "daily_practice_prefs";

    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private static SharedPreferences sharedPreferences;
    private static SharedPreferencesManager instance;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取单例实例
     */
    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }

    /**
     * 保存Token
     */
    public void saveToken(String token) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply();
    }

    /**
     * 获取Token
     */
    public static String getToken() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(KEY_TOKEN, null);
        }
        return null;
    }

    /**
     * 保存用户ID
     */
    public void saveUserId(Long userId) {
        sharedPreferences.edit().putLong(KEY_USER_ID, userId != null ? userId : -1).apply();
    }

    /**
     * 获取用户ID
     */
    public Long getUserId() {
        long userId = sharedPreferences.getLong(KEY_USER_ID, -1);
        return userId == -1 ? null : userId;
    }

    /**
     * 保存用户名
     */
    public void saveUsername(String username) {
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply();
    }

    /**
     * 获取用户名
     */
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    /**
     * 保存昵称
     */
    public void saveNickname(String nickname) {
        sharedPreferences.edit().putString(KEY_NICKNAME, nickname).apply();
    }

    /**
     * 获取昵称
     */
    public String getNickname() {
        return sharedPreferences.getString(KEY_NICKNAME, null);
    }

    /**
     * 设置登录状态
     */
    public void setLoggedIn(boolean isLoggedIn) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    /**
     * 检查是否已登录
     */
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
