package com.liviu.smp2.controller.interfaces;

import com.liviu.smp2.data.Playlist;

public interface OnPlaylistStatusChanged {
	/*
	 * Status:
	 * STATUS_PLAYLIST_LOADING       = 1;
	 * STATUS_PLAYLISY_LOAD_FINISHED = 2;
	 */
	public void onPlaylistStatusChanged(Playlist playlist, int status);
}
