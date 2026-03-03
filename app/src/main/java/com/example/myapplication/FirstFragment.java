//package com.example.myapplication;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.fragment.NavHostFragment;
//import com.example.myapplication.databinding.FragmentFirstBinding;
//
///**
// * 第一个 Fragment
// *
// * 这是应用的主界面 Fragment，包含：
// * - 一个文本显示区域，展示示例内容
// * - 一个"下一步"按钮，点击后导航到 SecondFragment
// */
//public class FirstFragment extends Fragment {
//
//    /** ViewBinding 对象，用于访问布局中的视图 */
//    private FragmentFirstBinding binding;
//
//    /**
//     * 创建 Fragment 视图的回调方法
//     *
//     * @param inflater 布局填充器，用于加载 XML 布局
//     * @param container 父容器视图
//     * @param savedInstanceState 保存的实例状态
//     * @return 创建的根视图
//     */
//    @Override
//    public View onCreateView(
//            @NonNull LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState
//    ) {
//
//        // 使用 ViewBinding 加载布局
//        binding = FragmentFirstBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//
//    }
//
//    /**
//     * 视图创建完成后的回调方法
//     * 在这里设置视图的交互逻辑
//     *
//     * @param view 创建的根视图
//     * @param savedInstanceState 保存的实例状态
//     */
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // 设置"下一步"按钮的点击事件
//        // 点击后导航到 SecondFragment
//        binding.buttonFirst.setOnClickListener(v ->
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment)
//        );
//    }
//
//    /**
//     * 视图销毁时的回调方法
//     * 清理 ViewBinding 对象以避免内存泄漏
//     */
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        // 将 binding 置空，释放视图引用，防止内存泄漏
//        binding = null;
//    }
//
//}