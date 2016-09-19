package com.hy.basicproject.theme;

import android.content.res.Resources;
import android.view.View;

/**
 * 换肤接口
 * Created by qq8585083 on 15/6/8.
 */
public interface ColorUiInterface {


    View getView();

    void setTheme(Resources.Theme themeId);
}
