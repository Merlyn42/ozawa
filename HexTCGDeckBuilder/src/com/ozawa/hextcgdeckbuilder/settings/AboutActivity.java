package com.ozawa.hextcgdeckbuilder.settings;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends ActionBarActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_page);

		ImageView appLogo = (ImageView) findViewById(R.id.ivHexAppLogo);
		int logoDimension = HexUtil.getScreenWidth(this) / 2;
		LayoutParams lp = new LayoutParams(logoDimension, logoDimension);
		lp.setMargins((logoDimension / 2), 0, (logoDimension / 2), 0);
		appLogo.setLayoutParams(lp);

		setOfficialSitesText();
		setCommuinityContentText();
	}

	/**
	 * Set the links for the official hex content
	 */
	private void setOfficialSitesText() {
		TextView hexTCG = (TextView) findViewById(R.id.tvHexTCG);
		hexTCG.setMovementMethod(LinkMovementMethod.getInstance());

		TextView hexTCGForum = (TextView) findViewById(R.id.tvHexTCGForum);
		hexTCGForum.setMovementMethod(LinkMovementMethod.getInstance());
	}

	/**
	 * Set the links for the community based hex content
	 */
	private void setCommuinityContentText() {
		TextView hexTCGPodcast = (TextView) findViewById(R.id.tvHexPodcast);
		hexTCGPodcast.setMovementMethod(LinkMovementMethod.getInstance());

		TextView hexTCGPro = (TextView) findViewById(R.id.tvHexTCGPro);
		hexTCGPro.setMovementMethod(LinkMovementMethod.getInstance());

		TextView hexTCGReddit = (TextView) findViewById(R.id.tvHexTCGReddit);
		hexTCGReddit.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
