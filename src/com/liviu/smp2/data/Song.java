package com.liviu.smp2.data;

import android.graphics.Color;

public class Song {
	
	//data
	private String  TAG	= "Song";
	private Object  tag;
	private String 	title;      			   // song title
	private String  artist;					   // song artist
	private String 	path;  	     			   // song path
	private String 	composer;    			   // song composer
	private String 	albumName;   			   // song album name
	private String 	imagePath;  			   // song image path
	private String  displayName;			   // the display name of this file
	private boolean isIgnored;   			   // false: song is not ignored; true: song is ignored
	private boolean isFavorite;  			   // false: song is not favorite; true: song is favorite
	private long   	albumId;     			   // an id for song album
	private int    	duration;    			   // the duration of this song
	private int    	year;        			   // the year when this song was released
	private int    	id;         			   // an unique id for this song: this will be useful when i want to search this song 
	private int    	rate;	     		       // user rating 
	private int 	playCount;   			   // how many time this song was played
	private int     colorTag;    			   // a color tag
	private int		position;				   // the position of this song in a playlist
	private boolean hasInfo; 	
	
	public Song() { // create an empty song
		
		id 		   = 0;
		title 	   = "";
		path  	   = "";
		composer   = "";
		albumName  = "";
		imagePath  = "";
		artist     = "";
		isFavorite = false;
		isIgnored  = false;
		hasInfo	   = false;
		albumId    = -1;							// -1 == unknown
		duration   = 0;
		year       = 0; 							// 0 == no year detected
		rate	   = 0;							    // no rate yet
		playCount  = 0; 							// 0 played time
		colorTag   = Color.WHITE;
		position   = -1;	
	}
	
	public Song(int songID_) { // create an empty song
		
		id 		   = songID_;
		title 	   = "";
		path  	   = "";
		composer   = "";
		albumName  = "";
		imagePath  = "";
		artist     = "";
		isFavorite = false;
		isIgnored  = false;
		hasInfo	   = false;
		albumId    = -1;							// -1 == unknown
		duration   = 0;
		year       = 0; 							// 0 == no year detected
		rate	   = 0;							    // no rate yet
		playCount  = 0; 							// 0 played time
		colorTag   = Color.WHITE;
		position   = -1;	
	}
	
	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}
	
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public void setColorTag(int colorTag) {
		this.colorTag = colorTag;
	}
	
	
	public void setComposer(String composer) {
		this.composer = composer;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setIgnored(boolean isIgnored) {
		this.isIgnored = isIgnored;
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public long getAlbumId() {
		return albumId;
	}
	
	public String getAlbumName() {
		return albumName;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public int getColorTag() {
		return colorTag;
	}
	
	public String getComposer() {
		return composer;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getId() {
		return id;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public String getPath() {
		return path;
	}
	
	public int getPlayCount() {
		return playCount;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getRate() {
		return rate;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getYear() {
		return year;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setTag(Object tag) {
		this.tag = tag;
	}
	
	public Object getTag() {
		return tag;
	}
	
	@Override
	public String toString() 
	{
		
		String strTemp = "-------------------Song---------------\n" +
						 "Song ID: " + id + "\n" +
						 "Song Title: " + title + "\n" +
						 "Song Artist: " + artist + "\n" + 
						 "Song Path: " + path + "\n" +
						 "Song Album Name: " + albumName + "\n" + 
						 "Song Album ID: " + albumId + "\n" +
						 "Song Composer: " + composer + "\n" +
						 "Song Duration: " + duration + "\n" +
						 "Song Image Path: " + imagePath + "\n" + 
						 "Song Play Count: " + playCount + "\n" +
						 "Song Rate: " + rate + "\n" +
						 "Song Year: " + year + "\n" +
						 "Song Color Tag: " + colorTag + "\n" + 
						 "Song isIgnored: " + isIgnored + "\n" + 
						 "Song isFavorite: " + isFavorite;
		return strTemp;
	}

	public boolean isFavorite() {
		return isFavorite;
	}
	
	public boolean isIgnored(){
		return isIgnored;
	}
	
	public boolean hasInfo(){
		if(hasInfo == true || (albumName.length() > 0 && 
							   playCount >= 0		  &&
							   colorTag	!= -1 		  &&
							   rate		>= 0))
			return true;
		else
			return false;
	}
	

}
