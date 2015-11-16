package com.tesool.fitody.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tesool.fitody.fragment.IconFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 骆巍
 * @date 2015-2-3
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
	private List<IconFragment> fragments = new ArrayList<>();

	public MainFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public void addFragment(IconFragment fragment) {
		fragments.add(fragment);
	}

	@Override
	public IconFragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("title",fragments.get(position).getTitle());
			jo.put("icon",fragments.get(position).getIcon());
			jo.put("alphaIcon",fragments.get(position).getAlphaIcon());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo.toString();
	}
}
