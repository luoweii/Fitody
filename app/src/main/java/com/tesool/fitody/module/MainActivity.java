package com.tesool.fitody.module;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.tesool.fitody.R;
import com.tesool.fitody.component.slidingtab.SlidingTabLayout;
import com.tesool.fitody.utils.CommonUtil;

import java.util.Hashtable;

import butterknife.Bind;

/**
 * Created by luowei on 2015/11/7.
 */
public class MainActivity extends BaseActivity {
    private long exitTime;//退出时间
    @Bind(R.id.viewPager)
    private ViewPager viewPager;
    @Bind()
    private SlidingTabLayout slidingTabs;
    private MainFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(MainHomeFragment.newInstance());
        pagerAdapter.addFragment(MainSearchFragment.newInstance());
        pagerAdapter.addFragment(MainMeFragment.newInstance());
        // viewPager.setPageTransformer(true, new FadeInOutPageTransformer());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);

        slidingTabs = (com.karakal.ringtonemanager.widget.slidingtab.SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabs.setCustomTabIcon(R.layout.tab_indicator_navigation, R.id.iv_main, R.id.iv_main_alpha, R.id.tv_main);
        slidingTabs.setSelectedIndicatorColors(getResources().getColor(R.color.navigation_indicator_green));
        slidingTabs.setSelectedIndicatorThickness(CommonUtil.dp2px(this, 3.3f));
        slidingTabs.setDistributeEvenly(true);
        slidingTabs.setViewPager(viewPager);// 最后设置viewpager
        slidingTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//查看手机app内存情况
//        int max = (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);
//        int use = (int) (Runtime.getRuntime().totalMemory()/1024/1024);
//        int free = (int) (Runtime.getRuntime().freeMemory() / 1024 / 1024);
//        LogUtils.d("------------------\n"+"max: "+max+" use "+use+" free "+free);

        //咪咕SDK初始化
        InitCmmInterface.initSDK(this);
        //咪咕用户初始化
        new Thread() {
            @Override
            public void run() {
                LogUtils.w("-----------------start initCmmEnv---------------------------");
                Hashtable<String, String> hashtable = InitCmmInterface.initCmmEnv(MainActivity.this);
                LogUtils.w("-----------------initCmmEnv result-----------------------\n" + hashtable.toString());
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime < 3000) {
            super.onBackPressed();
        } else {
            exitTime = System.currentTimeMillis();
            CommonUtil.showToast("再点一次返回键退出程序");
        }
    }
}
