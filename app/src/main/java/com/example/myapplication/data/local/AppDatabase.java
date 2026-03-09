package com.example.myapplication.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.data.local.dao.UserDao;
import com.example.myapplication.data.local.entity.UserEntity;

/**
 * 应用本地数据库
 */
@Database(
    entities = {
        UserEntity.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    // 数据库名称
    private static final String DATABASE_NAME = "daily_practice_db";

    /**
     * 获取数据库实例（单例模式）
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME
            )
            .fallbackToDestructiveMigration() // 开发阶段使用，生产环境应提供Migration
            .build();
        }
        return INSTANCE;
    }

    /**
     * 关闭数据库连接
     */
    public static void destroyInstance() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }

    // 获取DAO
    public abstract UserDao userDao();
}
