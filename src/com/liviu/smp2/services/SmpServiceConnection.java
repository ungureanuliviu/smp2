package com.liviu.smp2.services;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.liviu.smp2.controller.interfaces.OnPlaylistStatusChanged;
import com.liviu.smp2.controller.interfaces.OnSmpPlayerCompletetionListener;
import com.liviu.smp2.controller.interfaces.OnSmpPlayerProgressChangedListener;
import com.liviu.smp2.controller.interfaces.OnSmpServiceConnected;

public class SmpServiceConnection implements ServiceConnection{
	
	// constants
	private final String			TAG 		= "SmpServiceConnection";
	
	// data
	private SmpService				boundSmpService;
	private Context					context;
	private ArrayList<Runnable>		serviceTasks;
	
	
	// listeners
	private OnSmpServiceConnected	onSmpServiceConnected;
	
	// flags
	private boolean					isBound;
	
	public SmpServiceConnection(Context context_) {
		Log.e(TAG, "constructor");
		
		context 			= context_;
		isBound 			= false;
		serviceTasks		= new ArrayList<Runnable>();
		context.startService(new Intent(context, SmpService.class));
	}
	
	public void setOnSmpServiceConnected(OnSmpServiceConnected onSmpServiceConnected) {
		this.onSmpServiceConnected = onSmpServiceConnected;
	}
	
	public void bindSmpService(){
		context.bindService(new Intent(context, SmpService.class), this, Context.BIND_AUTO_CREATE);
		isBound = true;
	}
	
	public void unbindSmpService(){
		if(isBound){
			context.unbindService(this);
			isBound = false;
		}
	}
	

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		boundSmpService = ((SmpService.LocalBinder)service).getService();
		
		Log.e(TAG, "service bound");
		
		// run service tasks
		for(int i = 0; i < serviceTasks.size(); i++)
			serviceTasks.get(i).run();
		
		// all tasks are done. we can release them
		serviceTasks.clear();
		
		// trigger the listener
		if(onSmpServiceConnected != null)
			onSmpServiceConnected.onSmpServiceConnected(name, service, boundSmpService);
		
	}
	
	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.e(TAG, "onServiceDisconnected");
		boundSmpService = null;
	}


	public boolean nextSong() {
		if(isBound && boundSmpService == null){
			serviceTasks.add(new Runnable() {				
				@Override
				public void run() {
					Log.e(TAG, "in run:D");
					boundSmpService.nextSong();
				}
			});
			
			return true;
		}
		else if(isBound && boundSmpService != null){
			return boundSmpService.nextSong();
		}
		else
			return false;
	}

	// setam un listener pentru a sti daca playlist-ul este in curs de incarcare sau a fost incarcat
	public void setOnPlaylistStatusChanged( OnPlaylistStatusChanged onPlaylistStatusChanged ) {
		Log.e(TAG, "setOnPlaylistStatusChanged " + onPlaylistStatusChanged + " isBound " + isBound + " service: " + boundSmpService);
		final OnPlaylistStatusChanged playlistListener = onPlaylistStatusChanged;
		
		if(isBound && boundSmpService == null){
			serviceTasks.add(new Runnable() {				
				@Override
				public void run() {
					Log.e(TAG, "in run2:D");
					boundSmpService.setOnPlaylistStatusChanged(playlistListener);					
				}
			});		
		}
		else
			boundSmpService.setOnPlaylistStatusChanged(playlistListener);	
	}

	public boolean isPlaying() {
		if(boundSmpService == null)
			return false;
		else 
			return boundSmpService.isPlaying();
	}

	public void sendPlayerCommand(int command_, int progress_) {
		final int command  = command_;
		final int progress = progress_;
		
		if(isBound && boundSmpService == null){
			serviceTasks.add(new Runnable() {				
				@Override
				public void run() {
					Log.e(TAG, "in sendPlayerCommand1 " + command);
					boundSmpService.sendPlayerCommand(command, progress);
				}
			});
		}
		else if(isBound && boundSmpService != null){
			Log.e(TAG, "in sendPlayerCommand2 " + command);
			boundSmpService.sendPlayerCommand(command, progress);
		}
	}

	public void setOnSmpPlayerProgressChanged(OnSmpPlayerProgressChangedListener onSmpPlayerProgressChanged_) {
		final OnSmpPlayerProgressChangedListener onSmpPlayerProgressChanged = onSmpPlayerProgressChanged_;
		Log.e(TAG, "here: + " + onSmpPlayerProgressChanged);
		
		if(isBound && boundSmpService == null){
			serviceTasks.add(new Runnable() {				
				@Override
				public void run() {
					Log.e(TAG, "in run setOnSmpPlayerProgressChangedListener:D");
					boundSmpService.setOnSmpPlayerProgressChanged(onSmpPlayerProgressChanged);
				}
			});
		}
		else if(isBound && boundSmpService != null){
			boundSmpService.setOnSmpPlayerProgressChanged(onSmpPlayerProgressChanged);
		}
		else
			Log.e(TAG, "test");
	}

	public void setOnSmpPlayerCompletetionListener(OnSmpPlayerCompletetionListener listener) {
		final OnSmpPlayerCompletetionListener localListener = listener;
		Log.e(TAG, "here: + " + localListener);
		
		if(isBound && boundSmpService == null){
			serviceTasks.add(new Runnable() {				
				@Override
				public void run() {
					Log.e(TAG, "in run setOnSmpPlayerCompletetionListener");
					boundSmpService.setOnSmpPlayerCompletetionListener(localListener);
				}
			});
		}
		else if(isBound && boundSmpService != null){
			boundSmpService.setOnSmpPlayerCompletetionListener(localListener);
		}
		else
			Log.e(TAG, "test2");		
	}

	public void pauseSmpPlayerProgressChangedListener(boolean pause) {
		final boolean local = pause;
		Log.e(TAG, "here: + " + local);
		
		if(isBound && boundSmpService == null){
			serviceTasks.add(new Runnable() {				
				@Override
				public void run() {
					Log.e(TAG, "in run pauseSmpPlayerProgressChangedListener");
					boundSmpService.pauseSmpPlayerProgressChangedListener(local);
				}
			});
		}
		else if(isBound && boundSmpService != null){
			boundSmpService.pauseSmpPlayerProgressChangedListener(local);
		}
		else
			Log.e(TAG, "test3");				
		
	}	

}
