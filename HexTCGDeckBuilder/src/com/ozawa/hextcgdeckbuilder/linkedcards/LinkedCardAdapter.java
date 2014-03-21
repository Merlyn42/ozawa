package com.ozawa.hextcgdeckbuilder.linkedcards;

import java.util.ArrayList;

import com.ozawa.hextcgdeckbuilder.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LinkedCardAdapter extends BaseAdapter {

	private Context					mContext;

	private LayoutInflater			mLayoutInflater;

	private ArrayList<LinkedCards>	linkedCards	= new ArrayList<LinkedCards>();

	public LinkedCardAdapter(Context context, ArrayList<LinkedCards> linkedCards) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.linkedCards = linkedCards;
	}

	@Override
	public int getCount() {
		return linkedCards.size();
	}

	@Override
	public Object getItem(int position) {
		return linkedCards.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView;

		if (convertView == null) {
			textView = (TextView) mLayoutInflater.inflate(R.layout.linked_card_list_item, parent, false);
		} else {
			textView = (TextView) convertView;
		}

		textView.setText(linkedCards.get(position).card.name);
		return textView;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}
}
