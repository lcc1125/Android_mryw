package com.example.myapplication.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 用户本地数据库实体
 */
@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    private Long id;

    private String username;
    private String email;
    private String nickname;
    private String avatar;
    private String token;

    public UserEntity() {}

    public UserEntity(Long id, String username, String email, String nickname, String token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.token = token;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
