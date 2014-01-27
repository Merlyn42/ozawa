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

import java.util.List;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.hexentities.Champion;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SelectChampionArrayAdapter extends ArrayAdapter<Champion> {
	
	private List<Champion> champions;
	
	public SelectChampionArrayAdapter(Context context, int resource, List<Champion> champions) {
		super(context, resource, champions);
		
		this.champions = champions;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.select_champion_list_row, null);
		}
		
		Champion champion = champions.get(position);
		
		if(champion != null){
			TextView championName = (TextView) view.findViewById(R.id.tvChampionName_Row);
			championName.setText(champion.name);
			TextView championGameText = (TextView) view.findViewById(R.id.tvChampionGameText_Row);
			HexUtil.populateTextViewWithHexHtml(championGameText, champion.gameText);
		}
		
		return view;
	}
}
