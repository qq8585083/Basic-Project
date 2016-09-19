package com.hy.quickbasicproject.model;

import android.graphics.Color;

import com.hy.basicproject.utils.DataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qq8585083
 *
 */
public class Demo implements Serializable {
    public static final int TYPE_SPLASH = 0x00;
    public static final int TYPE_ADAPTER = 0x01;
    public static final int TYPE_EVENT = 0x02;
    public static final int TYPE_CRASH = 0x03;
    public static final int TYPE_FRAGMENT = 0x04;
    public static  final int TYPE_NIGHT=0x05;
    public static  final int TPYE_THEME=0x06;

    public String title;
    public int bgColor;
    public int type;

    public Demo() {
    }

    public Demo(String title, int bgColor, int type) {
        this.title = title;
        this.bgColor = bgColor;
        this.type = type;
    }

    private static List<Demo> demos;

    public static List<Demo> getDemos() {
        if (DataUtil.isEmpty(demos)) {
            demos = new ArrayList<>();
            demos.add(new Demo("启动页", Color.parseColor("#00bcd4"), TYPE_SPLASH));
            demos.add(new Demo("Fragment", Color.parseColor("#ff4081"), TYPE_FRAGMENT));
            demos.add(new Demo("通用适配器", Color.parseColor("#9c27b0"), TYPE_ADAPTER));
            demos.add(new Demo("异常日志收集", Color.parseColor("#e51c23"), TYPE_CRASH));
            demos.add(new Demo("AndroidEventBus", Color.parseColor("#ff9800"), TYPE_EVENT));
            demos.add(new Demo("夜间模式", Color.parseColor("#3F51B5"), TYPE_NIGHT));
            demos.add(new Demo("更换主题", Color.parseColor("#8BC34A"), TPYE_THEME));
        }
        return demos;
    }
}
