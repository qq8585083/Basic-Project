package com.hy.quickbasicproject.app;

import android.app.Application;

import com.hy.basicproject.BasicConfig;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 默认配置，适用于正式版本
         * 内部调用了: initDir() initLog(false) initExceptionHandler()三个方法
         */
        //BasicConfig.getInstance(this).init();

        /**
         * 自定义配置
         * initDir() 初始化SDCard缓存目录
         * initLog() 初始化日志打印
         * initExceptionHandler() 默认异常信息处理
         */
        BasicConfig.getInstance(this).init();

    }
}
