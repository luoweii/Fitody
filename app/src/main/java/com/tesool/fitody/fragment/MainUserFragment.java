package com.tesool.fitody.fragment;

import android.os.Bundle;

import com.tesool.fitody.R;
import com.tesool.fitody.activity.LoginActivity;
import com.tesool.fitody.utils.CommonUtil;

import butterknife.OnLongClick;

/**
 * 主栏目
 * Created by 骆巍 on 2015/8/13.
 */
public class MainUserFragment extends IconFragment {

    public static MainUserFragment newInstance() {
        MainUserFragment fragment = new MainUserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public int getIcon() {
        return R.drawable.ic_main_user;
    }

    @Override
    public int getAlphaIcon() {
        return R.drawable.ic_main_user1;
    }

    @Override
    public String getTitle() {
        return "我";
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_main_user;
    }

    @OnLongClick(R.id.llUser)
    public boolean userOnLongClick() {
        LoginActivity.startActivity(getContext());
        return true;
    }
}
