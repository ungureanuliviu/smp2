package com.liviu.smp2.controller.interfaces;

import android.media.MediaPlayer;

import com.liviu.smp2.data.Song;

public interface OnSmpPlayerCompletetionListener {
	public void onPlayerComplete(MediaPlayer mp, Song song);

}
