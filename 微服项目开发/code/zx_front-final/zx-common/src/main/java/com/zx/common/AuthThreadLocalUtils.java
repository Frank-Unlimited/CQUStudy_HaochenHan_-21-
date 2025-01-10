package com.zx.common;


import com.zx.domain.entity.user.UserInfo;

public class AuthThreadLocalUtils {
    private static ThreadLocal<UserInfo> threadLocal = new ThreadLocal();

    public static UserInfo getThreadLocal() {
        return threadLocal.get();
    }

    public static void setThreadLocal(UserInfo token) {
        threadLocal.set(token);
    }
}
