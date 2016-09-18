package com.hy.basicproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hy.basicproject.exception.CrashHandler;
import com.hy.basicproject.exception.impl.DefaultCrashProcess;
import com.hy.basicproject.interfaces.ICrashProcess;
import com.hy.basicproject.log.LogLevel;
import com.hy.basicproject.log.LogTool;
import com.hy.basicproject.log.Logger;
import com.hy.basicproject.log.Settings;
import com.hy.basicproject.utils.SDcardUtil;


/**
 * 全局配置
 *
 * @author qq8585083
 *
 */
public final class BasicConfig {
    private static final String LOG_TAG = "qq8585083";

    private Context mContext;
    private BasicConfig(Context context){ this.mContext = context.getApplicationContext(); }
    private volatile static BasicConfig sBasicConfig;

    public static final BasicConfig getInstance(@NonNull Context context){
        if(null == sBasicConfig){
            synchronized (BasicConfig.class){
                if(null == sBasicConfig){
                    sBasicConfig = new BasicConfig(context);
                }
            }
        }
        return sBasicConfig;
    }

    /**
     * 默认配置
     *
     */
    public void init(){
        initDir();
        initLog(false);
        initExceptionHandler();
    }

    /**
     * 初始化SDCard缓存目录
     * <pre>默认根目录名称：当前包名</pre>
     * @return
     */
    public BasicConfig initDir(){
        initDir(mContext.getPackageName());
        return this;
    }

    /**
     * 初始化SDCard缓存目录
     * @param dirName 根目录名称
     * @return
     */
    public BasicConfig initDir(@NonNull String dirName){
        SDcardUtil.setRootDirName(dirName);
        SDcardUtil.initDir();
        return this;
    }

    /**
     * 默认异常信息处理
     * @return
     */
    public BasicConfig initExceptionHandler(){
        return initExceptionHandler(new DefaultCrashProcess(mContext));
    }
    /**
     * 自定义异常信息处理
     * @return
     */
    public BasicConfig initExceptionHandler(ICrashProcess crashProcess){
        CrashHandler.getInstance(crashProcess);
        return this;
    }

    /**
     * 日志打印参数配置
     * @param isDebug true:打印全部日志，false:不打印日志
     * @return
     */
    public BasicConfig initLog(boolean isDebug){
        initLog(LOG_TAG, null, true, null,isDebug);
        return this;
    }

    /**
     * 日志打印参数配置
     * @param tag 日志标示
     * @return
     */
    public BasicConfig initLog(String tag){
        initLog(tag,true);
        return this;
    }
    /**
     * 日志打印参数配置
     * @param tag 日志标示，可以为空
     * @param isDebug true:打印全部日志，false:不打印日志
     * @return
     */
    public BasicConfig initLog(String tag,boolean isDebug){
        initLog(tag,null,true,null,isDebug);
        return this;
    }

    /**
     * 日志打印参数配置
     * @param tag 日志标示，可以为空
     * @param methodCount 显示方法行数，默认为：2
     * @param isHideThreadInfo 是否显示线程信息，默认显示
     * @param logTool 自定义日志打印，可以为空
     * @param isDebug true:打印全部日志，false:不打印日志
     * @return
     */
    public BasicConfig initLog(String tag,Integer methodCount,
                               boolean isHideThreadInfo,LogTool logTool,
                               boolean isDebug){

        Settings settings = Logger.init(TextUtils.isEmpty(tag) ? LOG_TAG : tag);
        if(null != methodCount){
            settings.methodCount(methodCount);
        }
        if(isHideThreadInfo){
            settings.hideThreadInfo();
        }
        if(null != logTool){
            settings.logTool(logTool);
        }
        settings.logLevel(isDebug ? LogLevel.FULL : LogLevel.NONE);
        return this;
    }
}
