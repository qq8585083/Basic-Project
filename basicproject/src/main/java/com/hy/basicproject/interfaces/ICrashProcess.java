package com.hy.basicproject.interfaces;

/**
 * 崩溃日志接口协议
 *
 * @author qq8585083
 *
 */
public interface ICrashProcess {

    void onException(Thread thread, Throwable exception) throws Exception;
}
