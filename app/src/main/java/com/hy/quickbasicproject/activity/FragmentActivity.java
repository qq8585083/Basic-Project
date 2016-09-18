package com.hy.quickbasicproject.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.hy.quickbasicproject.R;
import com.hy.quickbasicproject.fragment.Image2Fragment;
import com.hy.quickbasicproject.fragment.ImageFragment;

import butterknife.Bind;

public class FragmentActivity extends AppBaseActivity {

    @Bind(R.id.fragment_layout)
    RelativeLayout mFragmentLayout;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    private ImageFragment mImageFragment;
    private Image2Fragment mImage2Fragment;

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mFragmentLayout.setOnClickListener(this);
        //这里偷懒，使用默认的。实际项目中建议使用ToolBar
        getSupportActionBar().setTitle("Fragment示例");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mImageFragment = new ImageFragment();
        mImage2Fragment = new Image2Fragment();
        /**
         * 参数1：被替换为Fragment的视图id
         * 参数2：BaseFragment对象
         */
        changeFragment(R.id.fragment_layout, mImageFragment);
    }

    private boolean isImageFragment = true;

    @Override
    public void viewClick(View v) {
        if (v.getId() == R.id.fragment_layout) {
            isImageFragment = !isImageFragment;
            changeFragment(R.id.fragment_layout, isImageFragment ? mImageFragment : mImage2Fragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
