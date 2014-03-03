package hexentities;

import com.google.gson.annotations.SerializedName;
import enums.ItemType;

/**
 * Abstract class for Items
 */
abstract class Item {

	@SerializedName("m_Id")
	public M_Id		id;
	@SerializedName("m_Name")
	public String	name;
	@SerializedName("m_Type")
	public ItemType	type;
	@SerializedName("m_Description")
	public String	description;
}
