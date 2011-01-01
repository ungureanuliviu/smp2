package com.liviu.smp2.services;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.liviu.smp2.controller.interfaces.OnSmpPlayerCompletetionListener;
import com.liviu.smp2.controller.interfaces.OnSmpPlayerProgressChangedListener;
import com.liviu.smp2.data.Song;

public class SmpPlayer extends MediaPlayer{
 
	 // constants
	private final String			TAG				= "SmpPlayer";
	public static final int			COMMAND_PLAY	= 1;
	public static final int			COMMAND_PAUSE	= 2;
	public static final int			COMMAND_NEXT	= 3;
	public static final int			COMMAND_PREV	= 4;
	public static final int 		COMMAND_SEEK_TO = 5;
	
	
	// states
	private 	  final int			STATUS_IDLE			= 1;
	private 	  final int			STATUS_END			= 2;
	private 	  final int			STATUS_INITIALIZED 	= 3;
	private 	  final int			STATUS_PREPARED		= 4;
	private 	  final int 		STATUS_STARTED		= 5;
	private		  final int			STATUS_PAUSED		= 6;
	private 	  final int		    STATUS_STOPPED		= 7;
	private 	  final int			STATUS_COMPLETED	= 8;
	
	// messages
	private 	  final int			MSG_UPDATE_PROGRESS = 1;
	
	// data
	private Context 				context;
	private int 					state;
	private Song					currentSong;
	private int						elapsedSeconds;
	private int						duration;
	private Thread					progressThread;
	private Handler					handler;
	
	// flags
	private boolean 				FLAG_IS_DATASOURCE_LOADED   = false;
	private boolean					FLAG_KEEP_THREAD_ALIVE	    = true;
	private boolean					FLAG_SHOULD_UPDATE_PROGRESS = true;
	private boolean 				FLAG_NEW_THREAD				= true;
	private boolean					FLAG_PAUSE_PROGRESS_UPDATE  = false;
	
	// listeners
	private OnSmpPlayerProgressChangedListener	onSmpPlayerProgressChanged;
	
	
	public SmpPlayer(Context context_) {
		super();
		Log.e(TAG, "constructor");
		
		context 			= context_;
		state   			= STATUS_IDLE;
		elapsedSeconds 		= 0;
		duration			= 0;
		handler				= new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case MSG_UPDATE_PROGRESS:																				
										Log.e(TAG, "current position is: " + elapsedSeconds + "/" + duration + " listener: " + onSmpPlayerProgressChanged);
										if(onSmpPlayerProgressChanged != null)
											onSmpPlayerProgressChanged.onProgressChanged(SmpPlayer.this, elapsedSeconds, duration);
										break;

				default:
					break;
				}
			};
		};
		
	}
	
	@Override
	public void setDataSource(String path) throws IOException,
			IllegalArgumentException, IllegalStateException {	
		super.setDataSource(path);
		
		state = STATUS_INITIALIZED;
		Log.e(TAG, "state: " + state);
	}
	
	@Override
	public void prepare() throws IOException, IllegalStateException {
		super.prepare();
		
		state = STATUS_PREPARED;
		Log.e(TAG, "state: " + state);
	}
	
	@Override
	public void start() throws IllegalStateException {	
		super.start();
		
		Log.e(TAG, "state: " + state);
		state = STATUS_STARTED;
		
		if(FLAG_NEW_THREAD){
			progressThread = new Thread(new Runnable() {
				@Override
				public void run() {
					Log.e(TAG, "in thread");
					while(FLAG_KEEP_THREAD_ALIVE){
						try {
							Thread.sleep(1000);
							if(FLAG_SHOULD_UPDATE_PROGRESS){

								if(!FLAG_PAUSE_PROGRESS_UPDATE){
									try{
										elapsedSeconds 	= getCurrentPosition() / 1000;
										duration 		= getDuration() / 1000;
									}
									catch (IllegalStateException e) {
										elapsedSeconds = 0;
										duration = 0;
									}
									handler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
								}
							}
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			progressThread.start();
		}
		else{
			Log.e(TAG, "thread is dead");
		}
	}
	
	@Override
	public void stop() throws IllegalStateException {
		super.stop();
		
		Log.e(TAG, "state: " + state);
		state = STATUS_STOPPED;
	}
	
	@Override
	public void pause() throws IllegalStateException {
		super.pause();
		
		Log.e(TAG, "state: " + state);
		state = STATUS_PAUSED;
		FLAG_KEEP_THREAD_ALIVE 		= true;
		FLAG_SHOULD_UPDATE_PROGRESS = false;
	}
		

	public void play(Song song) {
		Log.e(TAG, "play: " + song);
		
		if(song == null)
			return;
		
		currentSong = song;
		
		if(isPlaying() && currentSong.getId() == song.getId()){
			Log.e(TAG, "this song is playing");
			return;
		}
		
		try {
			FLAG_KEEP_THREAD_ALIVE 		= true;
			FLAG_NEW_THREAD 			= true;
			FLAG_SHOULD_UPDATE_PROGRESS = true;
			
			setDataSource(song.getPath());
			prepare();
			start();
			
			FLAG_IS_DATASOURCE_LOADED = true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void play(){
		Log.e(TAG, "single play");
		try{
			FLAG_KEEP_THREAD_ALIVE 		= true;
			FLAG_NEW_THREAD        		= false;
			FLAG_SHOULD_UPDATE_PROGRESS = true;
			
			start();
		}catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		
		Log.e(TAG, "state: " + state);
		FLAG_IS_DATASOURCE_LOADED 	= false;
		state 						= STATUS_IDLE;
		FLAG_KEEP_THREAD_ALIVE		= false;
		FLAG_NEW_THREAD				= true;
		FLAG_SHOULD_UPDATE_PROGRESS	= true;
	}
	
	@Override
	public void release() {	
		super.release();
		
		Log.e(TAG, "state: " + state);
		FLAG_IS_DATASOURCE_LOADED 	= false;
		state					  	= STATUS_END;
		FLAG_KEEP_THREAD_ALIVE		= false;
		FLAG_NEW_THREAD				= true;
		FLAG_SHOULD_UPDATE_PROGRESS	= true;		
	}
	
	public boolean isDataSourceLoaded(){
		return FLAG_IS_DATASOURCE_LOADED;
	}
	
	public void setOnSmpPlayerProgressChanged(OnSmpPlayerProgressChangedListener onSmpPlayerProgressChanged_) {
		Log.e(TAG, "here1 " + onSmpPlayerProgressChanged_);
		onSmpPlayerProgressChanged = onSmpPlayerProgressChanged_;
	}

	public void setOnSmpPlayerCompletetionListener(OnSmpPlayerCompletetionListener onSmpPlayerCompletetionListener) {
		Log.e(TAG, "setOnSmpPlayerCompletetionListener: " + onSmpPlayerCompletetionListener + " " + currentSong);
		final Song 								localSong		= currentSong;
		final OnSmpPlayerCompletetionListener	localListener	= onSmpPlayerCompletetionListener; 
		
		setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				localListener.onPlayerComplete(mp, ((SmpPlayer)mp).getCurrentSong());
			}
		});
	}
	
	public Song getCurrentSong(){
		return currentSong;
	}

	public void pauseSmpPlayerProgressChangedListener(boolean pause) {
		FLAG_PAUSE_PROGRESS_UPDATE = pause;
		
	}
	
	
}
