package com.tesool.fitody.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tesool.fitody.utils.CommonUtil;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public abstract class BaseFragment extends Fragment {
    protected EventBus eventBus;
    public View contentView;
    public LayoutInflater inflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        inflater = getLayoutInflater(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this,contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (eventBus.isRegistered(this))
            eventBus.unregister(this);
    }

    public void onEvent(String msg){
        CommonUtil.showToast(msg);
    }

    @StringRes
    public abstract String getTitle();

    @LayoutRes
    public abstract int getLayout();
}
