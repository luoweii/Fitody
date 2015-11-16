package com.tesool.fitody.fragment;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.view.View;

/**
 * 有图标和标题的fragment
 * 
 * @author 骆巍
 */
public abstract class IconFragment extends BaseFragment {
	@DrawableRes
	public abstract int getIcon();
	@DrawableRes
	public abstract int getAlphaIcon();

	protected IconIndicatorListener iconListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof IconIndicatorListener) {
			iconListener = (IconIndicatorListener) activity;
		}
	}

	/**
	 *
	 * @author 骆巍
	 */
	public interface IconIndicatorListener {
		/**
		 * 监听滚动组件的滚动
		 * 
		 * @param view
		 * @param firstVisibleItem
		 * @param visibleItemCount
		 * @param totalItemCount
		 */
		void onScroll(View view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
	}
}
