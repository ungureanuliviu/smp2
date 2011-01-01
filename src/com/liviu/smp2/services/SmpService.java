package com.liviu.smp2.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.liviu.smp2.controller.interfaces.OnPlaylistStatusChanged;
import com.liviu.smp2.controller.interfaces.OnSmpPlayerCompletetionListener;
import com.liviu.smp2.controller.interfaces.OnSmpPlayerProgressChangedListener;
import com.liviu.smp2.managers.PlaylistManager;

public class SmpService extends Service implements OnErrorListener{
	
	// constants
	private final String 			TAG		=	"SmpService";
	
	// data
	private LocalBinder  			binder  = new LocalBinder();
	private SmpPlayer				player;
	private PlaylistManager			playlistManager;
	
	// listeners
	private OnSmpPlayerProgressChangedListener	onSmpPlayerProgressChangedListener;
	private OnSmpPlayerCompletetionListener		onSmpPlayerCompletetionListener;
	private boolean								pauseSmpPlayerProgressChangedListener;

	@Override
	public void onCreate() {	
		super.onCreate();
				
		Log.e(TAG, "onCreate()");
		
		// init
		player								  = new SmpPlayer(this);
		playlistManager						  = new PlaylistManager(this);
		pauseSmpPlayerProgressChangedListener = false;
		
	}
	
	public void test(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// ====================== OVERRIDED ===========================
	@Override
	public IBinder onBind(Intent intent) {		
		Log.e(TAG, "onBind " + intent);
		return binder;
	}
	
	@Override
	public void onDestroy() {	
		super.onDestroy();
		Log.e(TAG,  "ondestroy");		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand Intent = " + intent + " flags = " + flags + " startId = " + startId);
		return super.onStartCommand(intent, flags, startId);
	}
	
	// inner classes
	public class LocalBinder extends Binder{
		public SmpService getService(){ return SmpService.this; }
	}

	public boolean nextSong() {
		player.play(playlistManager.getNextSong());		
		return true;		
	}

	public void setOnPlaylistStatusChanged(
			OnPlaylistStatusChanged onPlaylistStatusChanged) {
		Log.e(TAG, "setOnPlaylistStatusChanged " + onPlaylistStatusChanged);
		playlistManager.setOnPlaylistStatusChanged(onPlaylistStatusChanged);
		
	}
	
	public boolean isPlaying() {
		return player.isPlaying();
	}

	public void sendPlayerCommand(int command, int progress) {
		switch (command) {
		case SmpPlayer.COMMAND_PLAY:
									Log.e(TAG, "sendPlayerCommand: PLAY " + command + " " + player.getCurrentPosition());
									if(!player.isDataSourceLoaded()){
										player = new SmpPlayer(this);
										setPlayerListeners();
										player.play(playlistManager.getCurrentSong());
									}
									else
										player.play();																			
									break;
		case SmpPlayer.COMMAND_PAUSE:
									Log.e(TAG, "sendPlayerCommand: PAUSE " + command);
									try{
										if(player.isPlaying())
											player.pause();
									}
									catch (IllegalStateException e) {
										e.printStackTrace();										
									}
									break;
		case SmpPlayer.COMMAND_NEXT:
									Log.e(TAG, "sendPlayerCommand: NEXT " + command );
									if(player.isPlaying()) player.stop();
									
									if(player.isDataSourceLoaded()){										
										player.reset();
										player.release();																												
									}
									
									player = new SmpPlayer(this);
									setPlayerListeners();
									player.play(playlistManager.getNextSong());
									
									break;
		case SmpPlayer.COMMAND_PREV:
									Log.e(TAG, "sendPlayerCommand: PREV " + command );
									if(player.isPlaying()) 
										player.stop();
									
									if(player.isDataSourceLoaded()){										
										player.reset();
										player.release();																												
									}
									
									player = new SmpPlayer(this);
									setPlayerListeners();
									player.play(playlistManager.getPrevSong());
									
									break;	
		case SmpPlayer.COMMAND_SEEK_TO:
									Log.e(TAG, "sendPlayerCommand: SEEK_TO " + command + " " + progress * 1000);
									player.seekTo(progress * 1000);
									break;
		default:
			break;
		}
	}

	public void setPlayerListeners(){
		
		// set listeners
		player.setOnErrorListener(this);
		player.pauseSmpPlayerProgressChangedListener(pauseSmpPlayerProgressChangedListener);
		player.setOnSmpPlayerProgressChanged(onSmpPlayerProgressChangedListener);
		player.setOnSmpPlayerCompletetionListener(onSmpPlayerCompletetionListener);		
		
	}
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.e(TAG, "onError: what = " + what + " extra = " + extra);
		return false;
	}

	public void setOnSmpPlayerProgressChanged(OnSmpPlayerProgressChangedListener onSmpPlayerProgressChanged_) {
		Log.e(TAG, "here: + " + onSmpPlayerProgressChanged_);
		onSmpPlayerProgressChangedListener = onSmpPlayerProgressChanged_;
		player.setOnSmpPlayerProgressChanged(onSmpPlayerProgressChangedListener);
	}

	public void setOnSmpPlayerCompletetionListener(OnSmpPlayerCompletetionListener listener) {
		onSmpPlayerCompletetionListener = listener;
		player.setOnSmpPlayerCompletetionListener(onSmpPlayerCompletetionListener);
		
	}

	public void pauseSmpPlayerProgressChangedListener(boolean pause) {
		
		player.pauseSmpPlayerProgressChangedListener(pause);
	}

}
