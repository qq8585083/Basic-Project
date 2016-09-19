package com.hy.quickbasicproject.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.hy.basicproject.event.SkinChangeEvent;
import com.hy.basicproject.log.Logger;
import com.hy.basicproject.theme.Theme;
import com.hy.basicproject.utils.DoubleClickExitHelper;
import com.hy.basicproject.utils.SDcardUtil;
import com.hy.basicproject.utils.ThemeUtils;
import com.hy.basicproject.utils.ToastUtil;
import com.hy.commonadapter.BaseAdapterHelper;
import com.hy.commonadapter.CommonRecyclerAdapter;
import com.hy.quickbasicproject.R;
import com.hy.quickbasicproject.model.Demo;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;

/**
 * 通用适配器示例By RecyclerView
 */
public class MainActivity extends AppBaseActivity implements ColorChooserDialog.ColorCallback {
    @Bind(R.id.main_rv)
    RecyclerView mRecyclerView;

    private List<Demo> mDemos;
    private DoubleClickExitHelper mDoubleClickExitHelper;
    private DemoAdapter mAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void onFirst() {
        super.onFirst();
        Logger.d("onFirst只有第一次才会执行");
        //这里可以做一些界面功能引导

    }

    /**
     * 方法执行顺序：
     * initData() --> initView() --> register()
     */
    @Override
    public void initData() {
        super.initData();
        mDemos = Demo.getDemos();
        //双击退出应用工具类使用方法，别忘了重写onKeyDown方法（见底部）
        mDoubleClickExitHelper = new DoubleClickExitHelper(this);
        //mDoubleClickExitHelper = new DoubleClickExitHelper(this)
        //.setTimeInterval(3000)
        //.setToastContent("再按一次退出demo");
    }

    /**
     * 方法执行顺序：
     * initData() --> initView() --> register()
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);


        mRecyclerView.setOnClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new DemoAdapter(activity, R.layout.activity_main_item, mDemos);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
                itemClick(mDemos.get(position));
            }
        });
    }


    private class DemoAdapter extends CommonRecyclerAdapter<Demo> {

        public DemoAdapter(Context context, int layoutResId, List<Demo> data) {
            super(context, layoutResId, data);
        }

        @Override
        public void onUpdate(BaseAdapterHelper helper, Demo item, int position) {
            final CardView cardView = helper.getView(R.id.main_item_cardview);
            cardView.setCardBackgroundColor(ThemeUtils.getThemeColor(MainActivity.this, R.attr.colorPrimary));
            helper.setText(R.id.main_item_tv, item.title);
        }
    }

    @Override
    public void register() {
        super.register();
        //这里可以注册一些广播、服务
    }

    @Override
    public void unRegister() {
        super.unRegister();
        //注销广播、服务
    }

    private void itemClick(Demo demo) {
        switch (demo.type) {
            case Demo.TYPE_ADAPTER:
                startActivity(MainActivity.this, ListViewActivity.class);
                break;
            case Demo.TYPE_CRASH:
                crashTest();
                break;
            case Demo.TYPE_EVENT:
                startActivity(MainActivity.this, EventBusActivity.class);
                break;
            case Demo.TYPE_SPLASH:
                startActivity(MainActivity.this, SplashActivity.class);
                break;
            case Demo.TYPE_FRAGMENT:
                startActivity(MainActivity.this, FragmentActivity.class);
                break;
            case Demo.TYPE_NIGHT:
                if (mSharedPreferencesUtil.getBooleanValue(INNIGHT, false)) {
                    mSharedPreferencesUtil.putBooleanValue(INNIGHT, false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    recreate();
                } else {
                    mSharedPreferencesUtil.putBooleanValue(INNIGHT, true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    recreate();
                }
                break;
            case Demo.TPYE_THEME:
                new ColorChooserDialog.Builder(MainActivity.this, R.string.theme)
                        .customColors(R.array.colors, null)
                        .doneButton(R.string.done)
                        .cancelButton(R.string.cancel)
                        .allowUserColorInput(false)
                        .allowUserColorInputAlpha(false)
                        .show();

                break;
        }
    }

    private void crashTest() {
        ToastUtil.showLongToast(this, "程序即将崩溃，崩溃日志请查看：" + SDcardUtil.getLogDirPath());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                throw new NullPointerException("666");
            }
        }, 3000);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDoubleClickExitHelper.onKeyDown(keyCode, event);
    }

    @Override
    public void onColorSelection(ColorChooserDialog dialog, int selectedColor) {
        if (selectedColor == ThemeUtils.getThemeColor(this, R.attr.colorPrimary))
            return;
        EventBus.getDefault().post(new SkinChangeEvent());
        if (selectedColor == getResources().getColor(R.color.colorBluePrimary)) {
            setTheme(R.style.BlueTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.Blue);

        } else if (selectedColor == getResources().getColor(R.color.colorRedPrimary)) {
            setTheme(R.style.RedTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.Red);

        } else if (selectedColor == getResources().getColor(R.color.colorBrownPrimary)) {
            setTheme(R.style.BrownTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.Brown);

        } else if (selectedColor == getResources().getColor(R.color.colorGreenPrimary)) {
            setTheme(R.style.GreenTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.Green);

        } else if (selectedColor == getResources().getColor(R.color.colorPurplePrimary)) {
            setTheme(R.style.PurpleTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.Purple);

        } else if (selectedColor == getResources().getColor(R.color.colorTealPrimary)) {
            setTheme(R.style.TealTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.Teal);

        } else if (selectedColor == getResources().getColor(R.color.colorPinkPrimary)) {
            setTheme(R.style.PinkTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.Pink);

        } else if (selectedColor == getResources().getColor(R.color.colorDeepPurplePrimary)) {
            setTheme(R.style.DeepPurpleTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.DeepPurple);

        } else if (selectedColor == getResources().getColor(R.color.colorOrangePrimary)) {
            setTheme(R.style.OrangeTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.Orange);

        } else if (selectedColor == getResources().getColor(R.color.colorIndigoPrimary)) {
            setTheme(R.style.IndigoTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.Indigo);

        } else if (selectedColor == getResources().getColor(R.color.colorLightGreenPrimary)) {
            setTheme(R.style.LightGreenTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.LightGreen);

        } else if (selectedColor == getResources().getColor(R.color.colorDeepOrangePrimary)) {
            setTheme(R.style.DeepOrangeTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.DeepOrange);

        } else if (selectedColor == getResources().getColor(R.color.colorLimePrimary)) {
            setTheme(R.style.LimeTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.Lime);

        } else if (selectedColor == getResources().getColor(R.color.colorBlueGreyPrimary)) {
            setTheme(R.style.BlueGreyTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.BlueGrey);

        } else if (selectedColor == getResources().getColor(R.color.colorCyanPrimary)) {
            setTheme(R.style.CyanTheme);
            mSharedPreferencesUtil.setCurrentTheme(Theme.Cyan);
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(selectedColor));
        mAdapter.notifyDataSetChanged();




//        ToastUtil.showToast(MainActivity.this, selectedColor + "");
//        final View rootView = getWindow().getDecorView();
//        rootView.setDrawingCacheEnabled(true);
//        rootView.buildDrawingCache(true);
//
//        final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
//        rootView.setDrawingCacheEnabled(false);
//        if (null != localBitmap && rootView instanceof ViewGroup) {
//            final View tmpView = new View(getApplicationContext());
//            tmpView.setBackgroundDrawable(new BitmapDrawable(getResources(), localBitmap));
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            ((ViewGroup) rootView).addView(tmpView, params);
//            tmpView.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    ColorUiUtil.changeTheme(rootView, getTheme());
//                    System.gc();
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    ((ViewGroup) rootView).removeView(tmpView);
//                    localBitmap.recycle();
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            }).start();
        }
    }
