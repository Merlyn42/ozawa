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

	@Override
	public Fragment getItem(int index) {
		Fragment fragment = null;
		switch(index){
			case 0:{
				CustomDeckFragment customDeckFragment = new CustomDeckFragment();				
				fragment = customDeckFragment;
				fragmentRefMap.put(index, fragment);
				break;
			}
			case 1:{
				MasterDeckFragment masterDeckFragment = new MasterDeckFragment();
				fragment = masterDeckFragment;
				fragmentRefMap.put(index, fragment);
				break;
			}
		}
		return fragment;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		fragmentRefMap.remove(position);
	}

	@Override
	public int getCount() {
		return tabCount;
	}
	
	public CustomDeckFragment getCustomDeckFragment(){
		return (CustomDeckFragment) fragmentRefMap.get(0);
	}
	
	public MasterDeckFragment getMasterDeckFragment(){
		return (MasterDeckFragment) fragmentRefMap.get(1);
	}
}
