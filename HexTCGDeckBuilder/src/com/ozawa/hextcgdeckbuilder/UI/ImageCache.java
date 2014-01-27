/*******************************************************************************
 * Hex TCG Deck Builder
 *     Copyright ( C ) 2014  Chad Kinsella, Dave Kerr and Laurence Reading
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.ozawa.hextcgdeckbuilder.UI;

import java.util.EnumMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;

/**
 * Queue to control caching behaviour of card images.
 * @author lreading
 *
 */
public class ImageCache {
	public enum ImageType {
		WithoutTemplate,WithTemplate
	}
	
	public enum CacheType {
		ListView,GridView
	}

	private ConcurrentLinkedQueue<QueueEntry> queue = new ConcurrentLinkedQueue<QueueEntry>();
	private int approxSize =0;
	private Integer maxCacheSize = 100000;
	private static EnumMap<CacheType,ImageCache> instances = new EnumMap<CacheType,ImageCache>(CacheType.class);
	
	public static ImageCache getInstance(CacheType cacheType){
		ImageCache result  = instances.get(cacheType);
		if(result==null){
			result = new ImageCache();
			instances.put(cacheType, result);
		}
		return result;
	}
	
	
	private class QueueEntry{
		public QueueEntry(AbstractCard card, ImageType imageType) {
			super();
			this.card = card;
			this.imageType = imageType;
		}
		public AbstractCard card;
		public ImageType imageType;
		
	}
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
	
	public void removeFromCache(){
		QueueEntry removal = queue.remove();
		removal.card.clearImageCache(removal.imageType);
		--approxSize;
	}
	
	public static void emergencyDump(CacheType type){
		ImageCache cache = getInstance(type);
		for( int i =0;i<10;i++){
			cache.removeFromCache();
		}
		cache.maxCacheSize=cache.approxSize;
		System.gc();
		
	}
	
	/**
	 * Adds a card to the queue, when the queue grows over maxCacheSize cards will start to be removed and have clearImageCache called on them.
	 * @param context Used to determine a maxCacheSize the first time this method is called.
	 * @param card The card to add to the queue.
	 */
	public void queueForRemovalFromCache(Context context,AbstractCard card,ImageType imageType){
		if(maxCacheSize==null){
			maxCacheSize=getMaxCacheSize(context);
		}
		QueueEntry entry = new QueueEntry(card, imageType);
		queue.add(entry);
		if(++approxSize>maxCacheSize){
			removeFromCache();
		}
	}
}
