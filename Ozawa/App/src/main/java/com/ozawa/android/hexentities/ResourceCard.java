package com.ozawa.android.hexentities;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

/**
 * A Resource Card.
 * 
 * @author Chad Kinsella
 */
public class ResourceCard extends AbstractCard {
	
	@SerializedName("m_ResourceThresholdGranted")
	public String resourceThresholdGranted;
	@SerializedName("m_CurrentResourcesGranted")
	public int currentResourcesGranted;
	@SerializedName("m_MaxResourcesGranted")
	public int maxResourcesGranted;

    @Override
    public Bitmap getCardBitmap(Context context) {
        return null;
    }
}
