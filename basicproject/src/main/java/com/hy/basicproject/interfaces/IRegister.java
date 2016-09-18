package com.hy.basicproject.interfaces;

/**
 * 规范注册的接口协议
 *
 * @author qq8585083
 *
 */
public interface IRegister {
    /**
     * 注册广播、服务
     */
    void register();

    /**
     * 注销广播、服务
     */
    void unRegister();
}
