package com.liviu.smp2.data;

import java.util.HashMap;

public class Playlist {

	// constants
	private final 		 String		TAG		                            = "Playlist";
	public  static final String 	PLAYLIST_ALL 						= "All songs";	
    public  static final int        STATUS_PLAYLIST_IS_LOADING       	= 1;
    public  static final int        STATUS_PLAYLIST_LOAD_FINISHED 		= 2;
	
	// data
	private int						playlistID;
	private String					playlistName;
	private String					playlistData; // file path ?
	private int 					playlistSize;
	private HashMap<Integer, Song>  playlistSongs;
	
	public Playlist() {
		playlistID 		= -1;
		playlistName	= "no name";
		playlistSize	= 0;
		playlistSongs   = new HashMap<Integer, Song>();
		playlistData	= "";
	}
	
	public Playlist(int playlistID_, String playlistName_){
		playlistID 		= playlistID_;
		playlistName	= playlistName_;
		playlistSize	= 0;
		playlistSongs	= new HashMap<Integer, Song>();
		playlistData	= "";
	}
	
	public Playlist(int playlistID_, String playlistName_, HashMap<Integer, Song> playlistSongs_){
		playlistID		= playlistID_;
		playlistName	= playlistName_;
		playlistSongs	= playlistSongs_;
		playlistSize	= playlistSongs.size();
		playlistData	= "";
	}
	
	public int getID() {
		return playlistID;
	}
	
	public String getName(){
		return playlistName;
	}
	
	public int getCount(){
		return playlistSize;
	}
	
	public HashMap<Integer, Song> getSongs(){
		return playlistSongs;
	}
	
	public String getData(){
		return playlistData;
	}
	
	public Playlist setID(int  ID_){
		playlistID = ID_;
		return this;
	}
	
	public Playlist setName(String name_){
		playlistName = name_;
		return this;
	}
	
	public Playlist setSongsData(HashMap<Integer, Song> songs_){
		playlistSongs = songs_;
		try{ playlistSize = playlistSongs.size(); } catch(NullPointerException e){ e.printStackTrace(); playlistSize = 0; }
		return this;
	}
	
	public Playlist setData(String data_){
		playlistData = data_;
		return this;
	}
	
	public Playlist setSize(int size_){
		playlistSize = size_;
		return this;
	}

	public boolean isEmpty() {
		return playlistSize <= 0;
	}

	public Song getSongAt(int index) {		
		return playlistSongs.get(index);
	}
}
