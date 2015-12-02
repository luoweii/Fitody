package com.tesool.fitody.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.tesool.fitody.R;
import com.tesool.fitody.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * 主栏目
 * Created by 骆巍 on 2015/8/13.
 */
public class MainAroundFragment extends IconFragment {
    @Bind(R.id.listView)
    ListView listView;
    private List<String> data = new ArrayList<>();
    @Bind(R.id.ptrFrameLayout)
    PtrFrameLayout ptrFrameLayout;

    private BaseAdapter adapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_main_around, null);
            }
            return convertView;
        }
    };

    public static MainAroundFragment newInstance() {
        MainAroundFragment fragment = new MainAroundFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getData() {
        for (int i = 0; i < 50; i++) {
            data.add("附近");
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setTextColor(Color.RED);
        header.setPadding(0, CommonUtil.dp2px(16), 0, CommonUtil.dp2px(16));
        header.initWithString("luo wei");
        ptrFrameLayout.setDurationToCloseHeader(500);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1500);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                iconListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
        listView.setAdapter(adapter);
        getData();
    }


    @Override
    public int getIcon() {
        return R.drawable.ic_main_around;
    }

    @Override
    public int getAlphaIcon() {
        return R.drawable.ic_main_around1;
    }

    @Override
    public String getTitle() {
        return "附近";
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_main_around;
    }

}
