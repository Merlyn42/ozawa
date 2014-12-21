package com.ozawa.hextcgdeckbuilder.enums;

public enum CardSet {

	ALLSETS("All sets"),
	SHARDSOFFATE("Shards of Fate"),
	SHATTEREDDESTINY("Shattered Destiny");
	
	private String setName;
	
	private CardSet(String name){
		this.setName = name;
	}
	
	@Override
	public String toString(){
		return getSetName();
	}
	
	public String getSetName(){
		return setName;
	}
}
