package com.tesool.fitody.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.tesool.fitody.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 主栏目
 * Created by 骆巍 on 2015/8/13.
 */
public class MainAroundFragment extends IconFragment {
    @Bind(R.id.listView)
    ListView listView;
    private List<String> data = new ArrayList<>();

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
