package com.liviu.smp2.util;

public class Constants {
 
	// database
	public static final String	DB_NAME				    = "smp_db.db";
	public static final String  TABLE_SONGS_INFO 	    = "songs_info";
	public static final String  TABLE_ALBUM_INFO		= "album_info";
	public static final String  CREATE_TABLE_SONGS_INFO = "create table if not exists songs_info(id_field integer not null primary key,"+
																						   		"albumName_field text not null,"+																						 																							  
																						   		"rate_field integer not null,"+
																						   		"playedCount_field integer not null,"+
																							    "isFavorite_field integer not null," +																							
																							    "colorTag_field integer not null," +
																							    "isIgnored_field integer not null);";
	
	public static final String CREATE_TABLE_ALBUM_INFO_TABLE = "create table if not exists album_info( album_id integer not null,"+
																									  "album_art text not null, " +
																									  "album_description text not null,"+
																									  "album_release_date text not null);";
	public static final String SONG_ID_FIELD 		   = "id_field";
	public static final String SONG_ALBUM_NAME_FIELD   = "albumName_field";
	public static final String SONG_PLAYED_COUNT_FIELD = "playedCount_field";
	public static final String SONG_RATE_FIELD 		   = "rate_field";
	public static final String SONG_COLOR_TAG_FIELD    = "colorTag_field";
	public static final String SONG_IS_FAVORITE_FIELD  = "isFavorite_field";
	public static final String SONG_IS_IGNORED_FIELD   = "isIgnored_field";
	public static final String ALBUM_ID			  	   = "album_id";
	public static final String ALBUM_ART		  	   = "album_art";
	public static final String ALBUM_DESC		  	   = "album_description";
	public static final String ALBUM_RELEASE_DATE 	   = "album_release_date";
	public static final String ALBUM_NO_ART   		   = "-1";		


}
