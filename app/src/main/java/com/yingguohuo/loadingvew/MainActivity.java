package com.yingguohuo.loadingvew;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.yingguohuo.loadingvew.widget.LoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.loadingview)
    LoadingView mLoadingView;

    private ObjectAnimator mObjectAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initAnim();
    }

    public void initAnim() {
        //radius对应LoadingView中到setRadius方法
        mObjectAnimator = ObjectAnimator.ofInt(mLoadingView, "radius", 0, 360);
        mObjectAnimator.setDuration(1500);
        mObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
        mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }

    @OnClick({R.id.start_tv, R.id.stop_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_tv:
                mObjectAnimator.start();
                break;
            case R.id.stop_tv:
                mObjectAnimator.cancel();
                break;
        }
    }
}
