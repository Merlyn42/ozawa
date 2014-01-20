package com.ozawa.hextcgdeckbuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.ozawa.hextcgdeckbuilder.UI.SymbolTemplate;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html;

public class HtmlImageGetter implements Html.ImageGetter{

	private Context context;
	private int textSize;
	
	public HtmlImageGetter(Context context, int textSize) {
		super();
		this.context = context;
		this.textSize = textSize;
	}

	@Override
	public Drawable getDrawable(String arg0) {
		SymbolTemplate template;

		template = SymbolTemplate.findSymbolTemplate(arg0, SymbolTemplate.getAllTemplates(context));
		if(template==null){
			System.err.println("Unable to find drawable for:"+arg0);
			return null;
		}
		Drawable d=context.getResources().getDrawable(template.templateId);
		
		
		d.setBounds(0,0,(int) Math.round(textSize*template.sizeRatio),textSize);
		return d;
	}

}
