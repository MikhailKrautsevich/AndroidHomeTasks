package com.example.hometask_05_music_player;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

class SongsManager {

    private final String MEDIA_PATH = Environment.getExternalStorageDirectory() + "/";
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<>();

    SongsManager() {
    }

    /**
     * Function to read all mp3 files and store the details in
     * ArrayList
     * */
    ArrayList<HashMap<String, String>> getPlayList() {
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
        String mp3Pattern = ".mp3";
        if (song.getName().endsWith(mp3Pattern)) {
            HashMap<String, String> songMap = new HashMap<>();
            //   final String MEDIA_PATH2 = "/storage/emulated/0/Music/Faun/2013 - Von den Elben/";
            String SONGTITLE = "songTitle";
            songMap.put(SONGTITLE,
                    song.getName().substring(0, (song.getName().length() - 4)));
            String SONGPATH = "songPath";
            songMap.put(SONGPATH, song.getPath());

            // Adding each song to SongList
            songsList.add(songMap);
        }
    }
}
