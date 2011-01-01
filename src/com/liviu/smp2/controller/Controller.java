package com.liviu.smp2.controller;

import android.content.Context;
import android.util.Log;

import com.liviu.smp2.controller.interfaces.OnPlaylistStatusChanged;
import com.liviu.smp2.controller.interfaces.OnSmpPlayerCompletetionListener;
import com.liviu.smp2.controller.interfaces.OnSmpPlayerProgressChangedListener;
import com.liviu.smp2.managers.DatabaseManager;
import com.liviu.smp2.services.SmpServiceConnection;

public class Controller {
	
	// constants	
	private final String 			TAG		= "Controller";
	private final Context			context;
	
	// flags
	public static final int			MAIN_ACTIVITY_FLAG		=	1;
	
	// service
	private SmpServiceConnection	smpServiceConnection;
	
	// managers
	private DatabaseManager			dbManager;


	public Controller(Context appContext_, int activityFlag_) {
		
		Log.e(TAG, "constructor: activityFlag = " + activityFlag_);

	// =========== common init =================		
		// main init
		context = appContext_;
		
		// managers init
		dbManager			 = new DatabaseManager(context);		
		
		// service init
		smpServiceConnection = new SmpServiceConnection(context);						
		smpServiceConnection.bindSmpService();
		
		switch (activityFlag_) {
		case MAIN_ACTIVITY_FLAG:
			
			break;

		default:
			break;
		}				
	}
	
	// player controls
	public boolean playNextSong(){
		Log.e(TAG, "playNextSong()");
		return smpServiceConnection.nextSong();
	}
	
	public boolean releaseController(int activityFlag){
		
		Log.e(TAG, "releaseController(" + activityFlag + ")");
		smpServiceConnection.unbindSmpService();
		return false;
	}

	public void setOnPlaylistStatusChanged(OnPlaylistStatusChanged onPlaylistStatusChanged) {
		Log.e(TAG, "setOnPlaylistStatusChanged " + onPlaylistStatusChanged);
		smpServiceConnection.setOnPlaylistStatusChanged(onPlaylistStatusChanged);
	}

	public void sendPlayerCommand(int command, int progress){
		Log.e(TAG, "sendPlayerCommand: " + command);
		smpServiceConnection.sendPlayerCommand(command, progress);
	}

	public boolean isPlaying() {
		return smpServiceConnection.isPlaying();
	}
	
	public void onResume(){
		smpServiceConnection.bindSmpService();
	}

	public void setOnSmpPlayerProgressChanged(OnSmpPlayerProgressChangedListener onSmpPlayerProgressChanged) {
		smpServiceConnection.setOnSmpPlayerProgressChanged(onSmpPlayerProgressChanged);
	}

	public void setOnSmpPlayerCompletetionsListener(OnSmpPlayerCompletetionListener listener) {
		smpServiceConnection.setOnSmpPlayerCompletetionListener(listener);
	}

	public void pauseSmpPlayerProgressChangedListener(boolean pause) {
		smpServiceConnection.pauseSmpPlayerProgressChangedListener(pause);
	}
}
