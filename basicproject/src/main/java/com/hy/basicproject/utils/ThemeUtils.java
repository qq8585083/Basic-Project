package com.hy.basicproject.utils;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Created by qq8585083 on 2016/4/6.
 */
public class ThemeUtils {
    public static int getThemeColor(Context context, int attrRes) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{attrRes});
        int color = typedArray.getColor(0, 0xffffff);
        typedArray.recycle();
        return color;
    }
}
