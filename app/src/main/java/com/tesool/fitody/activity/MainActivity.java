package com.tesool.fitody.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;

import com.tesool.fitody.R;
import com.tesool.fitody.adapter.MainFragmentPagerAdapter;
import com.tesool.fitody.component.blur.BlurringView;
import com.tesool.fitody.component.slidingtab.SlidingTabLayout;
import com.tesool.fitody.fragment.IconFragment;
import com.tesool.fitody.fragment.MainAroundFragment;
import com.tesool.fitody.fragment.MainDiscoverFragment;
import com.tesool.fitody.fragment.MainMessageFragment;
import com.tesool.fitody.fragment.MainUserFragment;
import com.tesool.fitody.utils.CommonUtil;

import butterknife.Bind;

/**
 * Created by luowei on 2015/11/7.
 */
public class MainActivity extends BaseActivity implements IconFragment.IconIndicatorListener{
    private long exitTime;//退出时间
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.slidingTabs)
    SlidingTabLayout slidingTabs;
    private MainFragmentPagerAdapter pagerAdapter;
    @Bind(R.id.blurView)
    BlurringView blurringView;
    @Bind(R.id.flViewPager)
    FrameLayout flViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(MainMessageFragment.newInstance());
        pagerAdapter.addFragment(MainAroundFragment.newInstance());
        pagerAdapter.addFragment(MainDiscoverFragment.newInstance());
        pagerAdapter.addFragment(MainUserFragment.newInstance());
        // viewPager.setPageTransformer(true, new FadeInOutPageTransformer());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);

        slidingTabs.setCustomTabIcon(R.layout.tab_indicator_navigation, R.id.iv_main, R.id.iv_main_alpha, R.id.tv_main);
        slidingTabs.setDistributeEvenly(true);
        slidingTabs.setSelectedIndicatorThickness(0);
        slidingTabs.setTopBorderThickness(CommonUtil.dp2px(0.5f));
        slidingTabs.setViewPager(viewPager);// 最后设置viewpager
        slidingTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                blurringView.invalidate();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        blurringView.setBlurredView(flViewPager);
        blurringView.post(new Runnable() {
            @Override
            public void run() {
                blurringView.invalidate();
            }
        });

//查看手机app内存情况
//        int max = (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);
//        int use = (int) (Runtime.getRuntime().totalMemory()/1024/1024);
//        int free = (int) (Runtime.getRuntime().freeMemory() / 1024 / 1024);
//        LogUtils.d("------------------\n"+"max: "+max+" use "+use+" free "+free);

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

    @Override
    public void onScroll(View view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

//        try {
//            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory()+"/download/temp.png");
//            Bitmap b = Bitmap.createBitmap(flViewPager.getWidth()/10, flViewPager.getHeight()/10, Bitmap.Config.ARGB_8888);
//            Canvas c = new Canvas(b);
//            c.scale(0.1f, 0.1f);
//            flViewPager.draw(c);
//            b.compress(Bitmap.CompressFormat.PNG, 90, fos);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        blurringView.invalidate();
    }
}
