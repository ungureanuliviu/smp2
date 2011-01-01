package com.liviu.smp2.controller.interfaces;

import android.media.MediaPlayer;

public interface OnSmpPlayerProgressChangedListener {
	public void onProgressChanged(MediaPlayer mp, int newProgress, int duration);
}
