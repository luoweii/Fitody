/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tesool.fitody.component.slidingtab;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * To be used with ViewPager to provide a tab indicator component which give
 * constant feedback as to the user's scroll progress.
 * <p/>
 * To use the component, simply add it to your view hierarchy. Then in your
 * {@link android.app.Activity} or {@link android.support.v4.app.Fragment} call
 * {@link #setViewPager(ViewPager)} providing it the
 * ViewPager this layout is being used for.
 * <p/>
 * The colors can be customized in two ways. The first and simplest is to
 * provide an array of colors via {@link #setSelectedIndicatorColors(int...)}.
 * The alternative is via the
 * {@link com.google.samples.apps.iosched.ui.widget.SlidingTabLayout.TabColorizer}
 * interface which provides you complete control over which color is used for
 * any individual position.
 * <p/>
 * The views used as tabs can be customized by calling
 * {@link #setCustomTabView(int, int)}, providing the layout ID of your custom
 * layout.
 */
public class SlidingTabLayout extends HorizontalScrollView {
    /**
     * Allows complete control over the colors drawn in the tab layout. Set with
     * {@link #setCustomTabColorizer(com.google.samples.apps.iosched.ui.widget.SlidingTabLayout.TabColorizer)}
     * .
     */
    public interface TabColorizer {

        /**
         * @return return the color of the indicator used when {@code position}
         * is selected.
         */
        int getIndicatorColor(int position);

    }

    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;

    private static final int INDICATOR_TYPE_TEXT = 1;
    private static final int INDICATOR_TYPE_IMAGE = 2;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private boolean mDistributeEvenly;

    private int mTabViewImageViewId;
    private int mTabViewImageViewAlphaId;

    private ViewPager mViewPager;
    private boolean smoothScroll = true;
    private SparseArray<String> mContentDescriptions = new SparseArray<String>();
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private final SlidingTabStrip mTabStrip;

    private int mIndictorType;// 指示器类型:图标或文字

    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new SlidingTabStrip(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    /**
     * Set the custom
     * {@link com.google.samples.apps.iosched.ui.widget.SlidingTabLayout.TabColorizer}
     * to be used.
     * <p/>
     * If you only require simple custmisation then you can use
     * {@link #setSelectedIndicatorColors(int...)} to achieve similar effects.
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    public void setDistributeEvenly(boolean distributeEvenly) {
        mDistributeEvenly = distributeEvenly;
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors
     * are treated as a circular array. Providing one color will mean that all
     * tabs are indicated with the same color.
     */
    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}.
     * When using
     * {@link com.google.samples.apps.iosched.ui.widget.SlidingTabLayout} you
     * are required to set any
     * {@link ViewPager.OnPageChangeListener} through
     * this method. This is so that the layout can update it's scroll position
     * correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId  id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {
        mIndictorType = INDICATOR_TYPE_TEXT;
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
    }

    /**
     * 设置图标指示器
     *
     * @param layoutResId 图标的资源Id
     * @param imageViewId 选中的资源Id
     */
    public void setCustomTabIcon(int layoutResId, int imageViewId, int imageViewAlpha, int textViewId) {
        mIndictorType = INDICATOR_TYPE_IMAGE;
        mTabViewLayoutId = layoutResId;
        mTabViewImageViewId = imageViewId;
        mTabViewImageViewAlphaId = imageViewAlpha;
        mTabViewTextViewId = textViewId;
    }

    /**
     * 设置文本和图标指示器
     */
    public void setCustomTabTextIcon(int layoutResId, int imageViewId, int imageViewAlpha) {
        mIndictorType = INDICATOR_TYPE_TEXT | INDICATOR_TYPE_IMAGE;
        mTabViewLayoutId = layoutResId;
        mTabViewImageViewId = imageViewId;
        mTabViewImageViewAlphaId = imageViewAlpha;
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the
     * pager content (number of tabs and tab titles) does not change after this
     * call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab
     * view is not set via {@link #setCustomTabView(int, int)}.
     */
    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        textView.setBackgroundResource(outValue.resourceId);
        textView.setAllCaps(true);

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleText = null;
            ImageView tabTitleImage = null;

            if (mTabViewLayoutId != 0) {
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip, false);
                try {
                    JSONObject jo = new JSONObject(adapter.getPageTitle(i).toString());
                    tabTitleImage = (ImageView) tabView.findViewById(mTabViewImageViewId);
                    if (tabTitleImage != null)
                        tabTitleImage.setImageResource(Integer.parseInt(jo.getString("icon")));

                    ImageView ivAlpha = (ImageView) tabView.findViewById(mTabViewImageViewAlphaId);
                    if (ivAlpha != null)
                        ivAlpha.setImageResource(Integer.parseInt(jo.getString("alphaIcon")));

                    tabTitleText = (TextView) tabView.findViewById(mTabViewTextViewId);
                    if (tabTitleText != null)
                        tabTitleText.setText(jo.getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }

            if (mDistributeEvenly) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.width = 0;
                lp.weight = 1;
            }

            tabView.setOnClickListener(tabClickListener);
            String desc = mContentDescriptions.get(i, null);
            if (desc != null) {
                tabView.setContentDescription(desc);
            }

            mTabStrip.addView(tabView);
            if (i == mViewPager.getCurrentItem()) {
                tabView.setSelected(true);
            }
        }
    }

    /**
     * 设置page标题
     *
     * @param index
     * @param title
     */
    public void setPageTitle(int index, String title) {
        TextView tvTitle = ((TextView) mTabStrip.getChildAt(index).findViewById(mTabViewTextViewId));
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    /**
     * 设置提示点是否显示
     *
     * @param index 需要设置的位置
     * @param Id    提示点的资源Id
     */
    public void setBadgeVisible(int index, int Id, int visibility) {
        mTabStrip.getChildAt(index).findViewById(Id).setVisibility(visibility);
    }

    public void setContentDescription(int i, String desc) {
        mContentDescriptions.put(i, desc);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure
                // we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);

            // 如果是图片
            if ((mIndictorType & INDICATOR_TYPE_IMAGE) == INDICATOR_TYPE_IMAGE) {
                // 改变标题透明度
                selectedTitle.findViewById(mTabViewImageViewAlphaId).setAlpha(1 - positionOffset);
                selectedTitle.findViewById(mTabViewImageViewId).setAlpha(positionOffset);
                if (position + 1 < mViewPager.getChildCount()) {
                    mTabStrip.getChildAt(position + 1).findViewById(mTabViewImageViewAlphaId).setAlpha(positionOffset);
                    mTabStrip.getChildAt(position + 1).findViewById(mTabViewImageViewId).setAlpha(1 - positionOffset);
                }
            }

            int extraOffset = (selectedTitle != null) ? (int) (positionOffset * selectedTitle.getWidth()) : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            switch (state) {
                // 滑动完成
                case ViewPager.SCROLL_STATE_IDLE:
                    // 如果是图片
                    if ((mIndictorType & INDICATOR_TYPE_IMAGE) == INDICATOR_TYPE_IMAGE) {
                        for (int i = 0; i < mViewPager.getChildCount(); i++) {
                            // 改变标题透明度
                            mTabStrip.getChildAt(i).findViewById(mTabViewImageViewAlphaId)
                                    .setAlpha(mViewPager.getCurrentItem() == i ? 1.0f : 0.0f);
                            mTabStrip.getChildAt(i).findViewById(mTabViewImageViewId)
                                    .setAlpha(mViewPager.getCurrentItem() == i ? 0.0f : 1.0f);
                        }
                        invalidate();//更新界面
//					((Activity) getContext()).invalidateOptionsMenu();// 更新界面
                    }
                    break;
            }

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
                // 如果是图片
                if ((mIndictorType & INDICATOR_TYPE_IMAGE) == INDICATOR_TYPE_IMAGE) {
                    for (int i = 0; i < mViewPager.getChildCount(); i++) {
                        // 改变标题透明度
                        mTabStrip.getChildAt(i).findViewById(mTabViewImageViewAlphaId)
                                .setAlpha(mViewPager.getCurrentItem() == i ? 1.0f : 0.0f);
                        mTabStrip.getChildAt(i).findViewById(mTabViewImageViewId)
                                .setAlpha(mViewPager.getCurrentItem() == i ? 0.0f : 1.0f);
                    }
                    invalidate();//更新界面
//					((Activity) getContext()).invalidateOptionsMenu();// 更新界面
                }
            }
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                mTabStrip.getChildAt(i).setSelected(position == i);
            }
            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // 如果使用MaterialRippleLayout
            int selectedItem = mTabStrip.indexOfChild(v);
//			int currentItem = mViewPager.getCurrentItem();
//			if(Math.abs(currentItem - selectedItem) > 1){
//				mViewPager.setCurrentItem(selectedItem, false);
//			}else {
//				mViewPager.setCurrentItem(selectedItem, true);
//			}
            mViewPager.setCurrentItem(selectedItem, smoothScroll);
            // for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            // if (v == mTabStrip.getChildAt(i)) {
            // mViewPager.setCurrentItem(i);
            // return;
            // }
            // }
        }
    }

    /**
     * 设置选中的指示器高度
     * 单位px
     *
     * @param mSelectedIndicatorThickness
     */
    public void setSelectedIndicatorThickness(int mSelectedIndicatorThickness) {
        mTabStrip.setmSelectedIndicatorThickness(mSelectedIndicatorThickness);
    }

    /**
     * 设置指示器下边框的高度
     * 单位px
     *
     * @param mBottomBorderThickness
     */
    public void setmBottomBorderThickness(int mBottomBorderThickness) {
        mTabStrip.setmBottomBorderThickness(mBottomBorderThickness);
    }

    /**
     * 设置指示器上边框的高度
     * 单位px
     *
     * @param mTopBorderThickness
     */
    public void setTopBorderThickness(int mTopBorderThickness) {
        mTabStrip.setmTopBorderThickness(mTopBorderThickness);
    }

    /**
     * 设置指示器下边框的颜色
     *
     * @param mDefaultBottomBorderColor
     */
    public void setmDefaultBottomBorderColor(int mDefaultBottomBorderColor) {
        mTabStrip.setmDefaultBottomBorderColor(mDefaultBottomBorderColor);
    }

    public boolean isSmoothScroll() {
        return smoothScroll;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        this.smoothScroll = smoothScroll;
    }
}
