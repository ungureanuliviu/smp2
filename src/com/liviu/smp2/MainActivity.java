package com.liviu.smp2;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.liviu.smp2.controller.Controller;
import com.liviu.smp2.controller.interfaces.OnPlaylistStatusChanged;
import com.liviu.smp2.controller.interfaces.OnSmpPlayerCompletetionListener;
import com.liviu.smp2.controller.interfaces.OnSmpPlayerProgressChangedListener;
import com.liviu.smp2.data.Playlist;
import com.liviu.smp2.data.Song;
import com.liviu.smp2.services.SmpPlayer;

public class MainActivity extends Activity implements
														OnPlaylistStatusChanged,
														OnClickListener,
														OnSmpPlayerProgressChangedListener,
														OnSmpPlayerCompletetionListener,
														OnSeekBarChangeListener{
	
	// constants
	private final String		TAG		=	"MainActivity";
	
	// data
	private Controller			controller;
	
	// views
	private Button				playSongButton;
	private Button				nextSongButton;
	private Button				prevSongButton;
	private SeekBar				progressBar;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);    
        setContentView(R.layout.main);
        
        playSongButton		= (Button)findViewById(R.id.button_play);
        nextSongButton		= (Button)findViewById(R.id.button_forward);
        prevSongButton		= (Button)findViewById(R.id.button_backward);
        progressBar			= (SeekBar)findViewById(R.id.seekBarsecond);
        
        // init
        controller 		= new Controller(this, Controller.MAIN_ACTIVITY_FLAG);     
        
        controller.setOnPlaylistStatusChanged(this);
        controller.setOnSmpPlayerProgressChanged(this);
        controller.setOnSmpPlayerCompletetionsListener(this);
        
        playSongButton.setOnClickListener(this);
        nextSongButton.setOnClickListener(this);
        progressBar.setOnSeekBarChangeListener(this);
        prevSongButton.setOnClickListener(this);
        
                
    }
        
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	controller.releaseController(Controller.MAIN_ACTIVITY_FLAG);
    }
    
    @Override
    protected void onPause() {    
    	super.onPause();
    	controller.releaseController(Controller.MAIN_ACTIVITY_FLAG);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {    
    	super.onConfigurationChanged(newConfig);
    }
        
    
    @Override
    protected void onResume() {
    	super.onResume();	
    	controller.onResume();
    }

	@Override
	public void onPlaylistStatusChanged(Playlist playlist, int status) {
		switch (status) {
		case Playlist.STATUS_PLAYLIST_IS_LOADING:
												 Log.e(TAG, "Loading playlist " + playlist.getName());
												 break;
		case Playlist.STATUS_PLAYLIST_LOAD_FINISHED:
												 Log.e(TAG, "playlist loaded: " + playlist.getName() + " " + playlist.getCount());
												 
												 if(!controller.isPlaying())
													 controller.sendPlayerCommand(SmpPlayer.COMMAND_PLAY, -1);
												 												 
												 break;

		default:
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_play:
							 Log.e(TAG, "play button clicked");
							 if(controller.isPlaying())
								 controller.sendPlayerCommand(SmpPlayer.COMMAND_PAUSE, -1);
							 else
								 controller.sendPlayerCommand(SmpPlayer.COMMAND_PLAY, -1);
							 break;
		case R.id.button_forward:
								Log.e(TAG, "play next song button");
								controller.sendPlayerCommand(SmpPlayer.COMMAND_NEXT, -1);
								break;
		case R.id.button_backward:
								Log.e(TAG, "play prev song button");
								controller.sendPlayerCommand(SmpPlayer.COMMAND_PREV, -1);
								break;

		default:
			break;
		}
		
	}

	@Override
	public void onProgressChanged(MediaPlayer mp, int newProgress, int duration) {
		if(progressBar.getMax() != duration)
			progressBar.setMax(duration);
		
		Log.e(TAG, "newProgress: " + newProgress + " duration: " + duration);
		progressBar.setProgress(newProgress);
	}

	@Override
	public void onPlayerComplete(MediaPlayer mp, Song song) {
		Log.e(TAG, "playing finished: song name: " + song);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// vrem ca atunci cand utilizatorul vrea sa deruleze melodia,
		// seekbar-ul nostru nu mai trebuie update-at pt a nu genera probleme
		controller.pauseSmpPlayerProgressChangedListener(true);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		controller.pauseSmpPlayerProgressChangedListener(false);
		controller.sendPlayerCommand(SmpPlayer.COMMAND_SEEK_TO, seekBar.getProgress());
	}

}