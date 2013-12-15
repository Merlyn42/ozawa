package ozawa;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import ozawa.Card.Attribute;

public class Filter {
	private String filterString;
	private final EnumSet<Attribute> attributes;
	
	Filter(){
		attributes= EnumSet.noneOf(Attribute.class);
	}
	
	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	public void addAttribute(Attribute attribute) {
		attributes.add(attribute);
	}
	
	public void removeAttribute(Attribute attribute) {
		attributes.remove(attribute);
	}
	
	public boolean isActive(Attribute attribute){
		return attributes.contains(attribute);
	}
	
	public ArrayList<Card> filter(ArrayList<Card> cards){
		ArrayList<Card> result = new ArrayList<Card>();
		for(Card card : cards){
			boolean include = true;
			for(Attribute att :attributes){
				if(!card.attributeFlags.contains(att)){
					include=false;
					break;
				}
			}
			if(include){
				result.add(card);
			}
			
		}
		return result;
	}

}
