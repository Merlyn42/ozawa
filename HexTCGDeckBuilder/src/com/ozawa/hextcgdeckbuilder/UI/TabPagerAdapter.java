package com.ozawa.hextcgdeckbuilder.UI;

import com.ozawa.hextcgdeckbuilder.CustomDeckFragment;
import com.ozawa.hextcgdeckbuilder.MasterDeckFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter{
	
	private static final int tabCount = 2;
	public CustomDeckFragment customDeckFragment;
	public MasterDeckFragment masterDeckFragment;
	
	public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

	@Override
	public Fragment getItem(int index) {
		switch(index){
			case 0:{
				if(customDeckFragment == null){
					customDeckFragment = new CustomDeckFragment();
				}
				return customDeckFragment;
			}
			case 1:{
				if(masterDeckFragment == null){
					masterDeckFragment = new MasterDeckFragment();
				}
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
