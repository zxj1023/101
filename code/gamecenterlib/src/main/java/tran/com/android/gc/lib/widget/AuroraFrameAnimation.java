package tran.com.android.gc.lib.widget;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.view.View;

public class AuroraFrameAnimation {
	private static final int DEFAULT_DURATION = 300;
	private Handler handler;
	private View view;
	private int frameCount;
	private onAuroraFrameAnimationEndListener frameAnimationEndListener;

	private boolean isRun;
	private boolean fillAfter = true;
	private int currentFrame;
	private boolean mOneShot = true;
	private ArrayList<Map<String, Integer>> animContent = new ArrayList<Map<String,Integer>>();

	private static final String ANIMATION_LIST = "animation-list";
	private static final String ANIMATION_ITEM = "item";
	private static final String ANIMATION_ONESHOT = "oneshot";
	private static final String ANIMATION_DRAWABLE = "drawable";
	private static final String ANIMATION_DURATION = "duration";

	public AuroraFrameAnimation(View view, int[] animImages) {
		if(view == null) {
			throw new NullPointerException("target view is null 1");
		}

		if(animImages == null || animImages.length == 0){
			throw new NullPointerException("animImages is null 1");
		}

		int[] animDurations = new int[animImages.length];
		for(int i = 0; i < animDurations.length; i++ ) {
			animDurations[i] = DEFAULT_DURATION;
		}

		init(view, animImages, animDurations);
	}

	public AuroraFrameAnimation(View view, int[] animImages, int commonDuration) {
		if(view == null) {
			throw new NullPointerException("target view is null 2");
		}

		if(animImages == null || animImages.length == 0){
			throw new NullPointerException("animImages is null 2");
		}

		int[] animDurations = new int[animImages.length];
		for(int i = 0; i < animDurations.length; i++ ) {
			animDurations[i] = commonDuration;
		}

		init(view, animImages, animDurations);
	}

	public AuroraFrameAnimation(View view, int[] animImages, int[] animDurations) {
		if(view == null) {
			throw new NullPointerException("target view is null 3");
		}

		if(animImages == null || animImages.length == 0 || animDurations == null || animDurations.length == 0){
			throw new NullPointerException("animImages is null 3");
		}

		if( animImages.length != animDurations.length ) {
			throw new IllegalStateException("length not matched");
		}

		init(view, animImages, animDurations);
	}

	public AuroraFrameAnimation(View view, int drawableId) {
		if(view == null) {
			throw new NullPointerException("target view is null 1");
		}
		
		this.view = view;
		this.handler = new Handler();

		parseDrawable(drawableId);
		
		this.frameCount = animContent.size();
		this.isRun = false;
	}

	private void init(View view, int[] animImages, int[] animDurations) {
		this.view = view;
		this.handler = new Handler();

		for( int i = 0; i < animImages.length; i++ ) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("animImages", animImages[i]);
			map.put("animDurations", animDurations[i]);
			animContent.add(map);
		}

		this.frameCount = animContent.size();
		this.isRun = false;
	}

	/**
	 * 解析xml
	 * 
	 * @param drawableId
	 */
	private void parseDrawable(int drawableId) {

		int index = 0;
		try {
			XmlResourceParser xpp = view.getResources().getXml(drawableId);
			//开始解析事件  
			int eventType = xpp.getEventType();  

			//处理事件，不碰到文档结束就一直处理  
			while (eventType != XmlPullParser.END_DOCUMENT) {   
				//因为定义了一堆静态常量，所以这里可以用switch  
				switch (eventType) {  
				case XmlPullParser.START_DOCUMENT:  
					// 不做任何操作或初开始化数据  
					break;  
				case XmlPullParser.START_TAG:  
					// 解析XML节点数据  
					// 获取当前标签名字
					// 通过getAttributeValue 和 netxText解析节点的属性值和节点值  
					startParseDrawableDataXml(xpp, index);
					break;  
				case XmlPullParser.END_TAG:  
					// 单节点完成，可往集合里边添加新的数据  
					break;  
				}  

				// 别忘了用next方法处理下一个事件，不然就会死循环  
				eventType = xpp.next();  
			}  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startParseDrawableDataXml( XmlResourceParser xpp, int index ) {
		//Log.e("111111", "--startParseDrawableDataXml -xpp.getName() = ---" + xpp.getName());
		if (ANIMATION_LIST.equals(xpp.getName())) {
			String oneShot = xpp.getAttributeValue(AuroraUtil.ANDROID_XMLNS,ANIMATION_ONESHOT);
			if (oneShot != null) {
				mOneShot = Boolean.valueOf(oneShot);
			}
			//Log.e("111111", "--startParseDrawableDataXml -oneShot = ---" + oneShot);
		} else if (ANIMATION_ITEM.equals(xpp.getName())){
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			String drawable = xpp.getAttributeValue(AuroraUtil.ANDROID_XMLNS,ANIMATION_DRAWABLE);
			if (drawable != null) {
				map.put("animImages", Integer.valueOf(drawable.replace("@", "")));
			}
			String duration = xpp.getAttributeValue(AuroraUtil.ANDROID_XMLNS,ANIMATION_DURATION);
			if ( duration != null ) {
				map.put("animDurations", Integer.valueOf(duration));
			}
			animContent.add(map);
			//Log.e("111111", "--startParseDrawableDataXml -drawable = ---" + drawable);
			//Log.e("111111", "--startParseDrawableDataXml -duration = ---" + duration);
		}
	}

	public void start() {
		if(isRun) {
			return;
		}      
		this.isRun = true;
		this.currentFrame = -1;
		curIndexOfDrawable = 0;
		if(drawableList == null){
			drawableList = new ArrayList<SoftReference<Drawable>>();
		}else{
			drawableList.clear();	
		}     
		startGetDrawable();
		nextFrame();
	}

	public void stop() {
		this.isRun = false;
	}

	/**
	 * Sets whether the animation should play once or repeat.
	 * 
	 * @param oneShot Pass true if the animation should only play once
	 */
	public void setOneShot(boolean oneShot) {
		this.mOneShot = oneShot;
	}

	/**
	 * @return True of the animation will play once, false otherwise
	 */
	public boolean isOneShot() {
		return this.mOneShot;
	}

	private void end() {
		if ( frameCount > 0 ) {
			if(!fillAfter) {
				view.setBackgroundResource(animContent.get(0).get("animImages"));
			} else {
				view.setBackgroundResource(animContent.get(frameCount - 1).get("animImages"));
			}
		}
		if(frameAnimationEndListener != null) {
			frameAnimationEndListener.onAuroraFrameAnimationEnd();
		}
		this.isRun = false;
	}

	private void nextFrame() {
		if(currentFrame == frameCount - 1) {
			this.isRun = false;
			if ( mOneShot ) {
				end();
			} else {
				start();
			}
			return;
		}

		currentFrame ++;      
		changeFrame(currentFrame); 

		handler.postDelayed(nextFrameRun, animContent.get(currentFrame).get("animDurations"));
	}

	private Runnable nextFrameRun = new Runnable() {
		public void run() {
			if(!isRun) {
				end();
				return;
			}
			nextFrame();
		}
	};

	private void changeFrame(int frameIndex) {
		long a = System.currentTimeMillis();

		view.setBackground(getDrawable());
		//view.setBackgroundResource(animContent.get(frameIndex).get("animImages"));
		long b = System.currentTimeMillis();
		//Log.e("111111", "----frameIndex = -------" + frameIndex + ",-----333333setBackground cost = ----" + String.valueOf(b-a));
	}

	public boolean isFillAfter() {
		return fillAfter;
	}

	public void setFillAfter(boolean fillAfter) {
		this.fillAfter = fillAfter;
	}

	public void setOnAuroraFrameAnimationEndListener(onAuroraFrameAnimationEndListener frameAnimationEndListener) {
		this.frameAnimationEndListener = frameAnimationEndListener;
	}

	/*************图片池预加载图片 begin********************/
	private final int MAX_DRAWABLE_NUM_IN_POOL = 5;
	private int mMaxDrawableNum = MAX_DRAWABLE_NUM_IN_POOL;
	private  List<SoftReference<Drawable>> drawableList = new ArrayList<SoftReference<Drawable>>();
	private  LoadDrawableThread loadDrawableThread = null;
	private int curIndexOfDrawable;

	private final class LoadDrawableThread extends Thread {
		@Override
		public void run() {
			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);          
			Resources curRes = view.getResources();
			curIndexOfDrawable = 0;
			drawableList.clear();
			while(isRun){
				if(curIndexOfDrawable >= animContent.size()){
					//Log.e("111111","LoadDrawableThread end");
					break;
				}          	
				synchronized (drawableList) {
					if (drawableList.size() >= mMaxDrawableNum) {
						try{
							//Log.e("111111","LoadDrawableThread wait 1");
							drawableList.wait();
							//Log.e("111111","LoadDrawableThread wait 2");                  		
						}catch(Exception e){
							e.printStackTrace();
						}               	
					}
				}

				long a = System.currentTimeMillis();
				Drawable drawable = ((BitmapDrawable)curRes.
						getDrawable(animContent.get(curIndexOfDrawable).get("animImages")));
				long b = System.currentTimeMillis();
				//Log.e("111111", "---curIndexOfDrawable = -------" + curIndexOfDrawable + ",-----getDrawable cost = ----" + String.valueOf(b-a));

				synchronized (drawableList) {
					drawableList.add(new SoftReference<Drawable>(drawable));  
					//Log.e("111111","---curIndexOfDrawable = -------" + curIndexOfDrawable + ", 333333getDrawable 11111111111drawable = " + drawable);
					curIndexOfDrawable++;
					//Log.e("111111","LoadDrawableThread curIndexOfDrawable = " + curIndexOfDrawable);
					//Log.e("111111","LoadDrawableThread drawableList.size() = " + drawableList.size());
					drawableList.notify();
				}                          
			}        
		}
	}

	private void startGetDrawable(){
		//if(loadDrawableThread == null){
		loadDrawableThread = new LoadDrawableThread();
		// }
		loadDrawableThread.start();
	}

	private synchronized Drawable getDrawable(){	    	   
		Drawable drawable;
		synchronized (drawableList) {		   
			if (drawableList.size() == 0) {
				if(curIndexOfDrawable >= animContent.size()){
					//Log.e("111111","getDrawable curIndexOfDrawable >= animContent.size()");
					return null; 
				}else{
					try{
						//Log.e("111111", "getDrawable wait 1");
						drawableList.wait();
						//Log.e("111111", "getDrawable wait 2");
					}catch(Exception e){
						e.printStackTrace();
					}
				}	        	 
			}

			drawable = drawableList.get(0).get();
			if ( drawable == null ) {
				drawable = ((BitmapDrawable)view.getResources().
						getDrawable(animContent.get(currentFrame).get("animImages")));
			}
			//Log.e("111111","getDrawable 22222222222333333drawable = " + drawable);
			drawableList.remove(0);
			drawableList.notify();
			//Log.e("111111","getDrawable drawableList.size() = " + drawableList.size());
		}
		return drawable; 	   
	}

	public void releaseObject(){

	}

	public void setMaxDrawableNumInPool( int maxNum ) {
		this.mMaxDrawableNum = maxNum;
	}

	public interface onAuroraFrameAnimationEndListener {
		public void onAuroraFrameAnimationEnd();	
	}
}
