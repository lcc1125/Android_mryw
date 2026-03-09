package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;

/**
 * 主活动 (Activity)
 *
 * 应用的入口活动，包含登录和注册Fragment容器
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Log.d(TAG, "MainActivity onCreate 开始");

            // 绑定布局
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            Log.d(TAG, "布局设置成功");

            // 检查登录状态，决定显示哪个页面
            checkLoginStatus();

            Log.d(TAG, "MainActivity onCreate 完成");
        } catch (Exception e) {
            Log.e(TAG, "MainActivity 启动失败", e);
            e.printStackTrace();
        }
    }

    private void checkLoginStatus() {
        try {
            // 检查用户是否已登录
            boolean isLoggedIn = com.example.myapplication.utils.SharedPreferencesManager
                    .getInstance(this).isLoggedIn();

            Log.d(TAG, "登录状态: " + isLoggedIn);

            if (!isLoggedIn) {
                // 未登录，导航到登录页
                // 注意：导航会在布局完全加载后自动进行，因为 nav_graph 的 startDestination 是 loginFragment
                Log.d(TAG, "用户未登录，将显示登录页面");
            } else {
                Log.d(TAG, "用户已登录");
            }
        } catch (Exception e) {
            Log.e(TAG, "检查登录状态失败", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
