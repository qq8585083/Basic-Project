package com.hy.quickbasicproject.activity;

import android.os.Bundle;

import com.hy.basicproject.activity.BaseActivity;
import com.hy.quickbasicproject.event.EventUtil;

import butterknife.ButterKnife;

public abstract class AppBaseActivity extends BaseActivity {

    public boolean configEventBus() {
        return false;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    public void register() {
        if (configEventBus()) {
            EventUtil.registerEventBus(this);
        }
    }

    @Override
    public void unRegister() {
        ButterKnife.unbind(this);
        if (configEventBus()) {
            EventUtil.unRegisterEventBus(this);
        }
    }
}
