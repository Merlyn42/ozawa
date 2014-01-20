package com.ozawa.hextcgdeckbuilder.UI;

import com.ozawa.hextcgdeckbuilder.CustomDeckFragment;
import com.ozawa.hextcgdeckbuilder.MasterDeckFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter{
	
	private static final int tabCount = 2;
	public final CustomDeckFragment customDeckFragment;
	public final MasterDeckFragment masterDeckFragment;
	
	public TabPagerAdapter(FragmentManager fm) {
        super(fm);
        customDeckFragment = new CustomDeckFragment();
        masterDeckFragment = new MasterDeckFragment();
    }

	@Override
	public Fragment getItem(int index) {
		switch(index){
			case 0:{
				return customDeckFragment;
			}
			case 1:{
				return masterDeckFragment;
			}
		}
		return null;
	}

	@Override
	public int getCount() {
		return tabCount;
	}
}
