package com.ozawa.hextcgdeckbuilder.hexentities;

import com.google.gson.annotations.SerializedName;
import com.ozawa.hextcgdeckbuilder.enums.ItemType;

/**
 * Abstract class for Items
 */
abstract class Item {

	@SerializedName("m_Id")
	public GlobalIdentifier	id;
	@SerializedName("m_Name")
	public String			name;
	@SerializedName("m_Type")
	public ItemType			type;
	@SerializedName("m_Description")
	public String			description;
}
