package com.ozawa.hextcgdeckbuilder.hexentities;

import com.google.gson.annotations.SerializedName;

/**
 * Each entity should have a GUID. This class allows for an reusable GUID reference without repetition of code.
 * 
 * @author Chad Kinsella
 */
public class GlobalIdentifier {
	
	public GlobalIdentifier(String id){
		this.gUID = id;
	}
	@SerializedName("m_Guid")
	public String gUID;

}
