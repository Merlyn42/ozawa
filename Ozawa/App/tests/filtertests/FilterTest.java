package filtertests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.ozawa.android.hexentities.AbstractCard;
import com.ozawa.android.hexentities.Card;

import org.junit.Before;
import org.junit.Test;

import com.ozawa.android.enums.Attribute;
import com.ozawa.android.enums.CardType;
import com.ozawa.android.enums.ColorFlag;
import ozawa.Filter;

public class FilterTest {
	Filter filter;
	@Before
	public void before(){
		filter = new Filter();
	}

	@Test
	public void testNewFilter() {
		for(ColorFlag color:EnumSet.allOf(ColorFlag.class)){
			assertTrue(filter.isActive(color));
		}
		
		for(Attribute attribute:EnumSet.allOf(Attribute.class)){
			assertFalse(filter.isActive(attribute));
		}
		
		for(CardType type:EnumSet.allOf(CardType.class)){
			assertTrue(filter.isActive(type));
		}
		
		assertTrue(filter.getFilterString()==null||filter.getFilterString().length()==0);
		
		
	}

	@Test
	public void testFilterString() {
		String string = new String("This is a string");
		filter.setFilterString(string);
		assertEquals(string,filter.getFilterString());
	}

	@Test
	public void testAddAttribute() {
		filter.addAttribute(Attribute.FLIGHT);
		assertTrue(filter.isActive(Attribute.FLIGHT));
	}

	@Test
	public void testRemoveAttribute() {
		filter.addAttribute(Attribute.FLIGHT);
		filter.removeAttribute(Attribute.FLIGHT);
		assertFalse(filter.isActive(Attribute.FLIGHT));
	}

	@Test
	public void testAddColor() {
		filter.addColor(ColorFlag.BLOOD);
		assertTrue(filter.isActive(ColorFlag.BLOOD));
	}

	@Test
	public void testRemoveColor() {
		filter.addColor(ColorFlag.BLOOD);
		filter.removeColor(ColorFlag.BLOOD);
		assertFalse(filter.isActive(ColorFlag.BLOOD));
	}

	@Test
	public void testAddType() {
		filter.addType(CardType.TROOP);
		assertTrue(filter.isActive(CardType.TROOP));
	}

	@Test
	public void testRemoveType() {
		filter.addType(CardType.TROOP);
		filter.removeType(CardType.TROOP);
		assertFalse(filter.isActive(CardType.TROOP));
	}

	@Test
	public void testFilterBySingleAttribute() {
		Card card1 = new Card();
		Card card2 = new Card();
		Card card3 = new Card();
		Card card4 = new Card();
		Attribute[] atts1 = {Attribute.FLIGHT};
		Attribute[] atts2 = {Attribute.STEADFAST};
		Attribute[] atts3 = {Attribute.FLIGHT,Attribute.STEADFAST,Attribute.RAGE};
		Attribute[] atts4 = {Attribute.STEADFAST,Attribute.CANTBLOCK};
		card1.attributeFlags=atts1;
		card2.attributeFlags=atts2;
		card3.attributeFlags=atts3;
		card4.attributeFlags=atts4;
		List<AbstractCard> cards= new ArrayList<AbstractCard>();
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);
		
		
		filter.addAttribute(Attribute.FLIGHT);
		ArrayList<AbstractCard> result = filter.filter(cards);
		assertEquals("wrong number of cards returned",2,result.size());
		assertTrue(result.contains(card1));
		assertTrue(result.contains(card3));
	}
	
	@Test
	public void testFilterByMultipleAttributes() {
		Card card1 = new Card();
		Card card2 = new Card();
		Card card3 = new Card();
		Card card4 = new Card();
		Attribute[] atts1 = {Attribute.FLIGHT};
		Attribute[] atts2 = {Attribute.STEADFAST};
		Attribute[] atts3 = {Attribute.FLIGHT,Attribute.STEADFAST,Attribute.RAGE};
		Attribute[] atts4 = {Attribute.STEADFAST,Attribute.CANTBLOCK};
		card1.attributeFlags=atts1;
		card2.attributeFlags=atts2;
		card3.attributeFlags=atts3;
		card4.attributeFlags=atts4;
		List<AbstractCard> cards= new ArrayList<AbstractCard>();
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);
		
		
		filter.addAttribute(Attribute.FLIGHT);
		filter.addAttribute(Attribute.STEADFAST);
		ArrayList<AbstractCard> result = filter.filter(cards);
		assertEquals("wrong number of cards returned",1,result.size());
		assertTrue(result.contains(card3));
	}
	
	@Test
	public void testFilterBySingleColor() {
		Card card1 = new Card();
		Card card2 = new Card();
		Card card3 = new Card();
		Card card4 = new Card();
		ColorFlag[] colors1 = {ColorFlag.BLOOD};
		ColorFlag[] colors2 = {ColorFlag.DIAMOND};
		ColorFlag[] colors3 = {ColorFlag.BLOOD,ColorFlag.SAPPHIRE};
		ColorFlag[] colors4 = {ColorFlag.SAPPHIRE,ColorFlag.DIAMOND};
		card1.colorFlags=colors1;
		card2.colorFlags=colors2;
		card3.colorFlags=colors3;
		card4.colorFlags=colors4;
		List<AbstractCard> cards= new ArrayList<AbstractCard>();
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);
		
		
		filter.removeColor(ColorFlag.COLORLESS);
		filter.removeColor(ColorFlag.DIAMOND);
		filter.removeColor(ColorFlag.RUBY);
		filter.removeColor(ColorFlag.SAPPHIRE);
		filter.removeColor(ColorFlag.WILD);
		ArrayList<AbstractCard> result = filter.filter(cards);
		assertEquals("wrong number of cards returned",2,result.size());
		assertTrue(result.contains(card3));
		assertTrue(result.contains(card1));
	}
	
	@Test
	public void testFilterByMultipleColors() {
		Card card1 = new Card();
		Card card2 = new Card();
		Card card3 = new Card();
		Card card4 = new Card();
		ColorFlag[] colors1 = {ColorFlag.BLOOD};
		ColorFlag[] colors2 = {ColorFlag.DIAMOND};
		ColorFlag[] colors3 = {ColorFlag.BLOOD,ColorFlag.SAPPHIRE};
		ColorFlag[] colors4 = {ColorFlag.SAPPHIRE,ColorFlag.DIAMOND};
		card1.colorFlags=colors1;
		card2.colorFlags=colors2;
		card3.colorFlags=colors3;
		card4.colorFlags=colors4;
		List<AbstractCard> cards= new ArrayList<AbstractCard>();
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);
		
		filter.removeColor(ColorFlag.COLORLESS);
		filter.removeColor(ColorFlag.DIAMOND);
		filter.removeColor(ColorFlag.RUBY);
		filter.removeColor(ColorFlag.WILD);
		ArrayList<AbstractCard> result = filter.filter(cards);
		assertEquals("wrong number of cards returned",3,result.size());
		assertTrue(result.contains(card3));
		assertTrue(result.contains(card1));
		assertTrue(result.contains(card4));
	}
	
	@Test
	public void testFilterBySingleType() {
		Card card1 = new Card();
		Card card2 = new Card();
		Card card3 = new Card();
		Card card4 = new Card();
		CardType[] types1 = {CardType.ARTIFACT};
		CardType[] types2 = {CardType.TROOP};
		CardType[] types3 = {CardType.ARTIFACT,CardType.TROOP};
		CardType[] types4 = {CardType.CHAMPION,CardType.BASICACTION};
		card1.cardType=types1;
		card2.cardType=types2;
		card3.cardType=types3;
		card4.cardType=types4;
		
		List<AbstractCard> cards= new ArrayList<AbstractCard>();
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);
		
		EnumSet<CardType> allTypes = EnumSet.allOf(CardType.class);
		allTypes.remove(CardType.ARTIFACT);
		for(CardType type : allTypes){
			filter.removeType(type);
		}
		ArrayList<AbstractCard> result = filter.filter(cards);
		assertEquals("wrong number of cards returned",2,result.size());
		assertTrue(result.contains(card3));
		assertTrue(result.contains(card1));
	}
	
	@Test
	public void testFilterByMultipleTypes() {
		Card card1 = new Card();
		Card card2 = new Card();
		Card card3 = new Card();
		Card card4 = new Card();
		CardType[] types1 = {CardType.ARTIFACT};
		CardType[] types2 = {CardType.TROOP};
		CardType[] types3 = {CardType.ARTIFACT,CardType.TROOP};
		CardType[] types4 = {CardType.CHAMPION,CardType.BASICACTION};
		card1.cardType=types1;
		card2.cardType=types2;
		card3.cardType=types3;
		card4.cardType=types4;
		
		List<AbstractCard> cards= new ArrayList<AbstractCard>();
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);
		
		EnumSet<CardType> allTypes = EnumSet.allOf(CardType.class);
		allTypes.remove(CardType.ARTIFACT);
		allTypes.remove(CardType.CHAMPION);
		for(CardType type : allTypes){
			filter.removeType(type);
		}
		ArrayList<AbstractCard> result = filter.filter(cards);
		assertEquals("wrong number of cards returned",3,result.size());
		assertTrue(result.contains(card3));
		assertTrue(result.contains(card1));
		assertTrue(result.contains(card4));
	}
	
	@Test
	public void testFilterByString(){
		Card card1 = new Card();
		Card card2 = new Card();
		Card card3 = new Card();
		Card card4 = new Card();
		
		card1.name="test";
		card2.name="Name2";
		card3.name="Name3";
		card4.name="Name4";
		
		card1.gameText="text1";
		card2.gameText="test";
		card3.gameText="tetestxt3";
		card4.gameText="text4";
		
		List<AbstractCard> cards= new ArrayList<AbstractCard>();
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);
		
		filter.setFilterString("test");
		ArrayList<AbstractCard> result = filter.filter(cards);
		assertEquals("wrong number of cards returned",3,result.size());
		assertTrue(result.contains(card1));
		assertTrue(result.contains(card2));
		assertTrue(result.contains(card3));
		
	}
	
	
}
