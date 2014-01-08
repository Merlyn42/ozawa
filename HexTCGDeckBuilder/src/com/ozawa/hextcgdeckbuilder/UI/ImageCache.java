package com.ozawa.hextcgdeckbuilder.UI;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;

/**
 * Queue to control caching behaviour of card images.
 * @author lreading
 *
 */
public class ImageCache {
	private static ConcurrentLinkedQueue<AbstractCard> queue = new ConcurrentLinkedQueue<AbstractCard>();
	private static int approxSize =0;
	private static Integer maxCacheSize = null;
	
	/**
	 * Determines the maximum number of images to be cached.
	 * @param context Used to get information about the system's heap size.
	 * @return
	 */
	private static int getMaxCacheSize(Context context){
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		long availableMegs = mi.availMem / 1048576L;
		int result = Long.valueOf(availableMegs).intValue();
		System.out.println("Setting image cache to size:" +result+" As large memory class is"+availableMegs+"MB");
		return result;
	}
	
	/**
	 * Adds a card to the queue, when the queue grows over maxCacheSize cards will start to be removed and have clearImageCache called on them.
	 * @param context Used to determine a maxCacheSize the first time this method is called.
	 * @param card The card to add to the queue.
	 */
	public static void queueForRemovalFromCache(Context context,AbstractCard card){
		if(maxCacheSize==null){
			maxCacheSize=getMaxCacheSize(context);
		}
		queue.add(card);
		if(++approxSize>maxCacheSize){
			AbstractCard removal = queue.remove();
			removal.clearImageCache();
			approxSize--;
		}
	}
}
