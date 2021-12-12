package com.iwbfly.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @auther: pangyajun
 * @create: 2021/12/5 19:29
 **/
public class User implements Cloneable {
    @JSONField(ordinal = 1)
    private String username;
    @JSONField(ordinal = 2)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User clone() throws CloneNotSupportedException {
        return (User)super.clone();
    }
}
