package com.tesool.fitody.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
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
public class MainUserFragment extends IconFragment {
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
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
                convertView = inflater.inflate(R.layout.list_item_main_message, null);
            }
            return convertView;
        }
    };

    public static MainUserFragment newInstance() {
        MainUserFragment fragment = new MainUserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getData() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setAdapter(adapter);
//        swipeRefreshLayout.setColorSchemeResources(R.color.font_theme);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
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
        return R.layout.fragment_main_message;
    }

}
