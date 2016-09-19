package com.hy.basicproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.hy.basicproject.fragment.BaseFragment;
import com.hy.basicproject.interfaces.IActivity;
import com.hy.basicproject.interfaces.IRegister;
import com.hy.basicproject.utils.KeyBoardUtil;
import com.hy.basicproject.utils.SharedPreferencesUtil;

/**
 * Activity父类
 *
 * @author qq8585083
 *
 */
public abstract class BaseActivity extends AppCompatActivity
        implements View.OnClickListener, IActivity, IRegister {
    private static final String SP_NAME = "firstConfig";
    protected static  final String INNIGHT="isNight";
    /** Activity状态 */
    public int activityState = DESTROY;
    protected Activity activity;
    protected BaseFragment currentFragment;
    private boolean isNight;
    public SharedPreferencesUtil mSharedPreferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        initPre();

        BaseActivityStack.getInstance().addActivity(this);
        setContentView(getLayoutResId());
        mSharedPreferencesUtil = new SharedPreferencesUtil(this, SP_NAME);
        final String simpleName = this.getClass().getSimpleName();
        if (mSharedPreferencesUtil.getBooleanValue(simpleName, true)) {
            onFirst();
            mSharedPreferencesUtil.putBooleanValue(simpleName, false);
        }
        isNight=mSharedPreferencesUtil.getBooleanValue(INNIGHT,false);
        initData();
        initView(savedInstanceState);
        register();
    }


    @Override
    public void onFirst() { }
    @Override
    public void initPre() { }
    @Override
    public void initData() { }
    @Override
    public void initView(Bundle savedInstanceState) { }
    @Override
    public void showProgress() { }
    @Override
    public void hideProgress() { }
    @Override
    public void register() { }
    @Override
    public void unRegister() { }


    @Override
    public void onClick(View v) {
        viewClick(v);
    }


    @Override
    public void viewClick(View v) { }


    @Override
    public void skipActivity(Activity aty, Class<?> cls) {
        startActivity(aty, cls);
        aty.finish();
    }


    @Override
    public void skipActivity(Activity aty, Intent it) {
        startActivity(aty, it);
        aty.finish();
    }


    @Override
    public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
        startActivity(aty, cls, extras);
        aty.finish();
    }


    @Override
    public void startActivity(Activity aty, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }


    @Override
    public void startActivity(Activity aty, Intent it) {
        aty.startActivity(it);
    }


    @Override
    public void startActivity(Activity aty, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }


    /**
     * 用Fragment替换视图
     *
     * @param resView 将要被替换掉的视图
     * @param targetFragment 用来替换的Fragment
     */
    public void changeFragment(int resView, BaseFragment targetFragment) {
        if (targetFragment.equals(currentFragment)) {
            return;
        }
        android.support.v4.app.FragmentTransaction transaction
                = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(resView, targetFragment, targetFragment.getClass().getName());
        }
        if (targetFragment.isHidden()) {
            transaction.show(targetFragment);
            targetFragment.onChange();
        }
        if (currentFragment != null && currentFragment.isVisible()) {
            transaction.hide(currentFragment);
            currentFragment.onHidden();
        }
        currentFragment = targetFragment;
        transaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        activityState = RESUME;
        if (mSharedPreferencesUtil.getBooleanValue(INNIGHT,false) != isNight) {
            if (mSharedPreferencesUtil.getBooleanValue(INNIGHT,false)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        activityState = PAUSE;
    }


    @Override
    protected void onStop() {
        super.onStop();
        activityState = STOP;
    }


    @Override
    protected void onDestroy() {
        unRegister();
        super.onDestroy();
        activityState = DESTROY;
//        BaseActivityStack.getInstance().finishActivity(this);
    }


    @Override
    public void finish() {
        KeyBoardUtil.hide(getWindow().getDecorView());
        super.finish();
    }
}
