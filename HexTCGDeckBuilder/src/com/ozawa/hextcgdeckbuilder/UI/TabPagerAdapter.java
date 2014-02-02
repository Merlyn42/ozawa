/*******************************************************************************
 * Hex TCG Deck Builder
 *     Copyright ( C ) 2014  Chad Kinsella, Dave Kerr and Laurence Reading
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.ozawa.hextcgdeckbuilder.UI;

import java.util.HashMap;

import com.ozawa.hextcgdeckbuilder.MasterDeckFragment;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.CustomDeckFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class TabPagerAdapter extends FragmentStatePagerAdapter{
	
	private static final int tabCount = 2;
	private HashMap<Integer, Fragment> fragmentRefMap = new HashMap<Integer, Fragment>();
	
	public TabPagerAdapter(FragmentManager fm) {
        super(fm);        
    }
	
	public TabPagerAdapter(FragmentManager fm, CustomDeckFragment customDeckFragment, MasterDeckFragment masterDeckFragment){
		super(fm);
		if(customDeckFragment != null){
			fragmentRefMap.put(Integer.valueOf(0), customDeckFragment);
		}
		if(masterDeckFragment != null){
			fragmentRefMap.put(Integer.valueOf(1), masterDeckFragment);
		}
	}

	@Override
	public Fragment getItem(int index) {
		if(index == 0){
			CustomDeckFragment customDeckFragment = new CustomDeckFragment();		
			fragmentRefMap.put(Integer.valueOf(index), customDeckFragment);
			return customDeckFragment;
		}else{
			MasterDeckFragment masterDeckFragment = new MasterDeckFragment();
			fragmentRefMap.put(Integer.valueOf(index), masterDeckFragment);
			return masterDeckFragment;
		}
		/*Fragment fragment = null;
		switch(index){
			case 0:{
				CustomDeckFragment customDeckFragment = CustomDeckFragment.newInstance();				
				fragment = customDeckFragment;
				fragmentRefMap.put(Integer.valueOf(index), fragment);
				break;
			}
			case 1:{
				MasterDeckFragment masterDeckFragment = MasterDeckFragment.newInstance();
				fragment = masterDeckFragment;
				fragmentRefMap.put(Integer.valueOf(index), fragment);
				break;
			}
		}
		return fragment;*/
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		fragmentRefMap.remove(Integer.valueOf(position));
	}

	@Override
	public int getCount() {
		return tabCount;
	}
	
	public CustomDeckFragment getCustomDeckFragment(FragmentManager fm, int viewId){
		CustomDeckFragment fragment = (CustomDeckFragment) fragmentRefMap.get(0);
		if(fragment == null){
			fragment = (CustomDeckFragment) fm.findFragmentByTag(makeFragmentName(viewId, 0));
		}
		return fragment;
	}
	
	public MasterDeckFragment getMasterDeckFragment(FragmentManager fm, int viewId){
		MasterDeckFragment fragment = (MasterDeckFragment) fragmentRefMap.get(1);
		if(fragment == null){
			fragment = (MasterDeckFragment) fm.findFragmentByTag(makeFragmentName(viewId, 1));
		}
		return fragment;
	}
	
	private static String makeFragmentName(int viewId, int position)
	{
	     return "android:switcher:" + viewId + ":" + position;
	}
}