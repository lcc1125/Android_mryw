package com.example.myapplication;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * 主活动 (Activity)
 *
 * 应用的入口活动，包含底部导航栏和Fragment容器
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 设置底部导航栏
        setupBottomNavigation();

        // 检查登录状态，决定显示哪个页面
        checkLoginStatus();
    }

    private void setupBottomNavigation() {
        bottomNavigationView = binding.bottomNavigation;

        // 获取NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // 配置顶部级别的目的地
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.statisticsFragment,
                R.id.profileFragment
        ).build();

        // 设置底部导航与NavController联动
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // 监听导航变化，控制底部导航栏显示
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.loginFragment ||
                    destination.getId() == R.id.registerFragment) {
                // 登录/注册页面隐藏底部导航
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                // 其他页面显示底部导航
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkLoginStatus() {
        // 检查用户是否已登录
        boolean isLoggedIn = com.example.myapplication.utils.SharedPreferencesManager
                .getInstance(this).isLoggedIn();

        if (!isLoggedIn) {
            // 未登录，隐藏底部导航并导航到登录页
            setBottomNavigationVisible(false);
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            navController.navigate(R.id.loginFragment);
        }
    }

    /**
     * 设置底部导航栏可见性
     */
    public void setBottomNavigationVisible(boolean visible) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}