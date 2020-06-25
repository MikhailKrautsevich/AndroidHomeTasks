package com.example.hometask_05_music_player;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

class SongsManager {

    private final String MEDIA_PATH = Environment.getExternalStorageDirectory() + "/";
    private ArrayList<Song> songsList = new ArrayList<>();

    SongsManager() {
    }

    /**
     * Function to read all mp3 files and store the details in
     * ArrayList
     * */
    ArrayList<Song> getPlayList() {
        if (MEDIA_PATH != null) {
            File home = new File(MEDIA_PATH);
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
        // return songs list array
        return songsList;
    }

    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
    }

    private void addSongToList(File song) {
        String mp3Pattern = ".mp3" ;
        if (song.getName().endsWith(mp3Pattern)) {
            String path = song.getPath() ;
            String title = song.getName().substring(0, (song.getName().length() - 4));
            Song newSong = new Song(title , path) ;
            songsList.add(newSong) ;
        }
    }
}
