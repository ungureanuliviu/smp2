package com.liviu.smp2.managers;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

import com.liviu.smp2.data.Playlist;
import com.liviu.smp2.data.Song;
import com.liviu.smp2.util.Constants;

public class DatabaseManager {

	// constants 
	private final String 		TAG						= "DatabaseManager";
	private final String[] 		PROJECTION_ALL_SONGS  	=  {
															  MediaStore.Audio.Media.ALBUM,         //0
														  	  MediaStore.Audio.Media.ALBUM_ID, 		//1
															  MediaStore.Audio.Media.ARTIST, 		//2
															  MediaStore.Audio.Media.COMPOSER,		//3
															  MediaStore.Audio.Media.DATA,			//4
															  MediaStore.Audio.Media.DISPLAY_NAME,	//5
															  MediaStore.Audio.Media.DURATION,		//6
															  MediaStore.Audio.Media.TITLE,			//7
															  MediaStore.Audio.Media.YEAR,			//8
															  MediaStore.Audio.Media._ID			//9
															};
	private final String[]	PROJECTION_ALL_PLAYLISTS	= {
													 		  MediaStore.Audio.Playlists._ID,
															  MediaStore.Audio.Playlists.NAME,
															  MediaStore.Audio.Playlists.DATA
														  };
	// URIs
	private final Uri 			URI_SONGS					= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	private final Uri			URI_PLAYLISTS				= MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
	
	// data
	private SQLiteDatabase		db;
	private Context				context;
	private ContentResolver		contentResolver;
	
	public DatabaseManager(Context context_) {
		context 			= context_;
		contentResolver		= context.getContentResolver();
	}
	
	public boolean openDatabase(){
		try{
			db = context.openOrCreateDatabase(Constants.DB_NAME, Context.MODE_PRIVATE, null);
			db.execSQL(Constants.CREATE_TABLE_ALBUM_INFO_TABLE);
			db.execSQL(Constants.CREATE_TABLE_SONGS_INFO);
			
			return true;
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		catch (IllegalStateException e) {
			e.printStackTrace();
			closeDatabase();
			return false;
		}
	}
	
	public void closeDatabase(){		
		if(db.isOpen()) db.close();			
	}
		
	public void crPrintAllSongs(){
		Cursor 		c    = contentResolver.query(URI_SONGS, PROJECTION_ALL_SONGS, null, null, null);
		boolean 	flag = true;
		
		if(c == null){
			Log.e(TAG, "printAllSongs() c is null");
			return;
		}
		
		c.moveToFirst();
		while(flag){
			Log.e(TAG, "Song name: " + c.getString(5) + " " + c.getString(4));
			flag = c.moveToNext();			
		}
		
		c.close();
	}
	
	public HashMap<Integer, Song> getAllSongs() {
		
		Log.e(TAG, "getAllSongs()");
		HashMap<Integer, Song>  songsMap 	= new HashMap<Integer, Song>();
		Cursor 					c    		= contentResolver.query(URI_SONGS, PROJECTION_ALL_SONGS, null, null, null);
		boolean 				flag 		= true;
		Song					tempSong	= new Song();
		int 					index		= 0;
		
		if(c == null){
			Log.e(TAG, "printAllSongs() c is null");
			return songsMap;
		}
		
		c.moveToFirst();
		while(flag){
			tempSong = new Song();
			tempSong.setAlbumName(c.getString(0));
			tempSong.setAlbumId(c.getLong(1));
			tempSong.setArtist(c.getString(2));
			tempSong.setComposer(c.getString(3));
			tempSong.setPath(c.getString(4));
			tempSong.setDisplayName(c.getString(5));
			tempSong.setDuration(c.getInt(6));
			tempSong.setTitle(c.getString(7));
			tempSong.setYear(c.getInt(8));
			tempSong.setId(c.getInt(9));
			
			Log.e(TAG, "song: " + tempSong.toString());
			songsMap.put(index, tempSong);
			index++;
			
			flag = c.moveToNext();			
		}
		Log.e(TAG, "songs loaded: " + songsMap.size());				
		c.close();
		return songsMap;
	}	
	
	
	
	
	
	
	
	
	
	//============================== PLAYLISTS ====================================
	public  ArrayList<Playlist> crGetAllPlaylists() {
		ArrayList<Playlist> playlistsArray 			 = new ArrayList<Playlist>();		
		Playlist			emptyPlaylist			 = new Playlist();
		boolean				flag					 = true;
		
		Cursor c = contentResolver.query(URI_PLAYLISTS,
										 PROJECTION_ALL_PLAYLISTS,
										 null, 
										 null,
										 MediaStore.Audio.Playlists._ID + " DESC");
		if(c ==  null){
			Log.e(TAG, "cursor is null in getAllPlaylists");
			return playlistsArray;
		}
		
		c.moveToFirst();	
		while(flag){
			emptyPlaylist = new Playlist();
			Log.e(TAG, "playlist id: " + c.getLong(0) +  " playlist name: " + c.getString(1) + " data: " + c.getString(2));
			emptyPlaylist.setName(c.getString(1));
			emptyPlaylist.setID(c.getInt(0));
			emptyPlaylist.setData(c.getString(2));
			emptyPlaylist.setSize(getPlaylistSize(emptyPlaylist.getID()));
			
			playlistsArray.add(emptyPlaylist);
			flag = c.moveToNext();
		}
		
		c.close();
		
		return playlistsArray;
	}	
	
	public int getPlaylistSize(long playlistID) {
		Log.e(TAG, "getPlaylistSize: " + playlistID);
		
		if(playlistID == -1)
			return 0;
		
		int count 						  = 0;					
		String[] playlistSongsProjections = new String[]{ MediaStore.Audio.Playlists.Members.AUDIO_ID };
		Cursor c 						  = contentResolver.query(
																	MediaStore.Audio.Playlists.Members.getContentUri("external", playlistID),
																	playlistSongsProjections,
																	null,
																	null,
																	null
																 );
		if(c == null){
			Log.e(TAG, "songCursor is null");
			return 0;
		}
		
		count = c.getCount();		
		c.close();				
		
		Log.e(TAG, "song count for playlist: " + playlistID + " is : " + count);
		return count;
	}
}