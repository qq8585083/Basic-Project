package com.hy.quickbasicproject.fragment;

import android.os.Bundle;
import android.view.View;

import com.hy.basicproject.fragment.BaseFragment;
import com.hy.quickbasicproject.event.EventUtil;

import butterknife.ButterKnife;

public abstract class AppBaseFragment extends BaseFragment {

    public boolean configEventBus() {
        return false;
    }

    @Override
    public void initView(View parentView, Bundle savedInstanceState) {
        ButterKnife.bind(this, parentView);
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
