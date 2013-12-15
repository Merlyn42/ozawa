package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import hexentities.AbstractCard;
import hexentities.Card;

import org.junit.Before;
import org.junit.Test;

import enums.Attribute;
import enums.CardType;
import enums.ColorFlag;
import ozawa.Filter;

public class FilterTest {
	Filter filter;
	@Before
	public void before(){
		filter = new Filter();
	}

	@Test
	public void testNewFilter() {
		assertTrue(filter.isActive(ColorFlag.BLOOD));
		assertTrue(filter.isActive(ColorFlag.COLORLESS));
		assertTrue(filter.isActive(ColorFlag.DIAMOND));
		assertTrue(filter.isActive(ColorFlag.RUBY));
		assertTrue(filter.isActive(ColorFlag.SAPPHIRE));
		assertTrue(filter.isActive(ColorFlag.WILD));
		
		assertFalse(filter.isActive(Attribute.UNKNOWN));
		assertFalse(filter.isActive(Attribute.FLIGHT));
		assertFalse(filter.isActive(Attribute.DEFENSIVE));
		assertFalse(filter.isActive(Attribute.JUGGERNAUGHT));
		assertFalse(filter.isActive(Attribute.FORCEATTACK));
		assertFalse(filter.isActive(Attribute.CANTREADYAUTOMATICALLY));
		assertFalse(filter.isActive(Attribute.CANTBLOCK));
		assertFalse(filter.isActive(Attribute.SPIRITDRAIN));
		assertFalse(filter.isActive(Attribute.ESCALATION));
		assertFalse(filter.isActive(Attribute.CANTATTACK));
		assertFalse(filter.isActive(Attribute.SPEED));
		assertFalse(filter.isActive(Attribute.STEADFAST));
		assertFalse(filter.isActive(Attribute.INSPIRE));
		assertFalse(filter.isActive(Attribute.FIRSTSTRIKE));
		assertFalse(filter.isActive(Attribute.SPELLSHIELD));
		assertFalse(filter.isActive(Attribute.IMMORTAL));
		assertFalse(filter.isActive(Attribute.ALLOWYARDINSPIRE));
		assertFalse(filter.isActive(Attribute.RAGE));
		assertFalse(filter.isActive(Attribute.PREVENTCOMBATDAMAGE));	
		assertFalse(filter.isActive(Attribute.CANTBEBLOCKED));
		
		assertTrue(filter.isActive(CardType.TROOP));
		assertTrue(filter.isActive(CardType.BASICACTION));
		assertTrue(filter.isActive(CardType.QUICKACTION));
		assertTrue(filter.isActive(CardType.CONSTANT));
		assertTrue(filter.isActive(CardType.ARTIFACT));
		assertTrue(filter.isActive(CardType.CHAMPION));
		assertTrue(filter.isActive(CardType.MERCENARY));
		assertTrue(filter.isActive(CardType.RESOURCE));
		
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

}
