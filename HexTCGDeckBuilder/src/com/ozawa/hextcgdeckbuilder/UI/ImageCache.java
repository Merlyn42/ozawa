package com.ozawa.hextcgdeckbuilder.UI;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;

public class ImageCache {
	private static ConcurrentLinkedQueue<AbstractCard> queue = new ConcurrentLinkedQueue<AbstractCard>();
	private static int approxSize =0;
	final static int MAX_CACHE_SIZE = 256;
	
	public static void addToCache(AbstractCard card){
		queue.add(card);
		if(++approxSize>MAX_CACHE_SIZE){
			AbstractCard removal = queue.remove();
			removal.clearImageCache();
			approxSize--;
		}
		
	}
}
