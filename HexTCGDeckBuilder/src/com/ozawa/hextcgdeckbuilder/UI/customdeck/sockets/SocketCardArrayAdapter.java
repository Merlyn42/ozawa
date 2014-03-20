package com.ozawa.hextcgdeckbuilder.UI.customdeck.sockets;

import java.util.List;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.hexentities.Gem;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SocketCardArrayAdapter extends ArrayAdapter<Gem> {

	private List<Gem>	gems;

	public SocketCardArrayAdapter(Context context, int resource, List<Gem> gems) {
		super(context, resource, gems);

		this.gems = gems;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.row_socket_card, null);
		}

		Gem gem = gems.get(position);

		if (gem != null) {
			TextView gemName = (TextView) view.findViewById(R.id.gem_name);
			gemName.setText(gem.name);
			TextView gemText = (TextView) view.findViewById(R.id.gemtext);
			HexUtil.populateTextViewWithHexHtml(gemText, gem.description);
		}

		setBackgroundResource(view, gem);

		return view;
	}

	/**
	 * Set the list background color
	 * 
	 * @param view
	 * @param gem
	 */
	@SuppressLint("DefaultLocale")
	private void setBackgroundResource(View view, Gem gem) {
		int backgroundId = R.drawable.list_selector_colorless;
		if (gem != null) {
			String gemColour = gem.gemType.toString().toLowerCase();

			if (gemColour.contains("blood")) {
				backgroundId = R.drawable.list_selector_blood;
			} else if (gemColour.contains("diamond")) {
				backgroundId = R.drawable.list_selector_diamond;
			} else if (gemColour.contains("ruby")) {
				backgroundId = R.drawable.list_selector_ruby;
			} else if (gemColour.contains("sapphire")) {
				backgroundId = R.drawable.list_selector_sapphire;
			} else if (gemColour.contains("wild")) {
				backgroundId = R.drawable.list_selector_wild;
			}
		}

		view.setBackgroundResource(backgroundId);
	}

}
