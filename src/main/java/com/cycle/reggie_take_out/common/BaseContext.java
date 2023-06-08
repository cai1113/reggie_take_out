package com.cycle.reggie_take_out.common;
/**
 * 基于ThreadLocal的封装工具类
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 将用户id存入ThreadLocal
     * @param userId
     */
    public static void setCurrentUserId(Long userId){
        threadLocal.set(userId);
    }

    /**
     * 获取当前线程的用户id
     * @return
     */
    public static Long getCurrentUserId(){
        return threadLocal.get();
    }
}
